package itm.proyectoharoldo.backend.Services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Creates Google Calendar events for virtual appointments and attaches a manually generated
 * meeting URL in the event description (no {@code conferenceData} / Google Meet API).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleCalendarMeetService {

    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR);
    private static final String MEET_LINK_PREFIX = "https://meet.google.com/";
    private static final ZoneId SCHEDULING_ZONE = ZoneId.of("America/Bogota");

    private final ResourceLoader resourceLoader;

    @Value("${google.calendar.service-account-key-path:}")
    private String serviceAccountKeyPath;

    @Value("${google.calendar.calendar-id}")
    private String calendarId;

    private Calendar calendarClient;
    private String serviceAccountEmail;

    @PostConstruct
    public void init() {
        if (serviceAccountKeyPath == null || serviceAccountKeyPath.isBlank()) {
            log.warn("google.calendar.service-account-key-path is empty; calendar sync is disabled until configured.");
            return;
        }
        if (calendarId == null || calendarId.isBlank()) {
            log.warn("google.calendar.calendar-id is empty; calendar sync is disabled. Share a calendar with the service account and set its ID.");
            return;
        }
        try (InputStream in = openCredentialsStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(in).createScoped(SCOPES);
            if (credentials instanceof ServiceAccountCredentials sac) {
                this.serviceAccountEmail = sac.getClientEmail();
                log.info("Google Calendar service account: {}", serviceAccountEmail);
                if ("primary".equalsIgnoreCase(calendarId.trim())) {
                    log.warn(
                            "calendar-id is 'primary', which usually fails for service accounts. "
                                    + "Share a dedicated calendar with {} and set GOOGLE_CALENDAR_ID to that calendar's ID.",
                            serviceAccountEmail);
                }
            }
            var transport = GoogleNetHttpTransport.newTrustedTransport();
            var jsonFactory = GsonFactory.getDefaultInstance();
            this.calendarClient = new Calendar.Builder(
                    transport,
                    jsonFactory,
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("Haroldo Backend")
                    .build();
            log.info("Google Calendar client ready (calendar-id: {}, key: {})", calendarId.trim(), serviceAccountKeyPath.trim());
        } catch (GeneralSecurityException | IOException e) {
            log.error("Failed to initialize Google Calendar client (key path: {})", serviceAccountKeyPath, e);
            this.calendarClient = null;
        }
    }

    public boolean isConfigured() {
        return calendarClient != null && calendarId != null && !calendarId.isBlank();
    }

    /**
     * Generates a manual meeting URL, creates a calendar event (no conference data), and returns the URL
     * for persistence in {@code schedules.meet_link}.
     */
    public String createMeetEvent(String summary, String description, ZonedDateTime start, ZonedDateTime end) {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "Google Calendar no está configurado. Defina GOOGLE_CALENDAR_SERVICE_ACCOUNT_KEY_PATH y "
                            + "GOOGLE_CALENDAR_ID (calendario compartido con la cuenta de servicio).");
        }

        String meetLink = generateManualMeetLink();
        String descriptionWithMeet = appendMeetLink(description, meetLink);

        try {
            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(descriptionWithMeet)
                    .setStart(toEventDateTime(start))
                    .setEnd(toEventDateTime(end));

            Event created = calendarClient.events()
                    .insert(calendarId.trim(), event)
                    .setSendUpdates("none")
                    .setFields("id,htmlLink")
                    .execute();

            log.info("Calendar event created with manual Meet link (eventId={}, meetLink={})", created.getId(), meetLink);
            return meetLink;
        } catch (IOException e) {
            log.error("Google Calendar API error on calendar '{}': {}", calendarId, e.getMessage(), e);
            throw new IllegalStateException("No se pudo crear el evento en Google Calendar: " + e.getMessage(), e);
        }
    }

    /**
     * Builds a Meet-style URL using a UUID-derived code ({@code xxx-yyyy-zzz}).
     * This does not call Google APIs; the host should open the link to start the meeting.
     */
    static String generateManualMeetLink() {
        String raw = UUID.randomUUID().toString().replace("-", "");
        String code = raw.substring(0, 3) + "-" + raw.substring(3, 7) + "-" + raw.substring(7, 10);
        return MEET_LINK_PREFIX + code;
    }

    static String appendMeetLink(String description, String meetLink) {
        String base = description != null ? description.stripTrailing() : "";
        if (base.isEmpty()) {
            return "Meet link: " + meetLink;
        }
        return base + "\n\nMeet link: " + meetLink;
    }

    private InputStream openCredentialsStream() throws IOException {
        String configured = serviceAccountKeyPath.trim();

        if (configured.startsWith("classpath:")) {
            Resource resource = resourceLoader.getResource(configured);
            if (!resource.exists()) {
                throw new IOException("Classpath resource not found: " + configured);
            }
            return resource.getInputStream();
        }

        Path filePath = Path.of(configured);
        if (Files.isRegularFile(filePath)) {
            return Files.newInputStream(filePath);
        }

        Resource fileResource = resourceLoader.getResource("file:" + configured);
        if (fileResource.exists()) {
            return fileResource.getInputStream();
        }

        String classpathLocation = configured;
        if (classpathLocation.startsWith("./")) {
            classpathLocation = classpathLocation.substring(2);
        }
        if (classpathLocation.startsWith("src/main/resources/")) {
            classpathLocation = classpathLocation.substring("src/main/resources/".length());
        }

        Resource classpath = resourceLoader.getResource("classpath:" + classpathLocation);
        if (classpath.exists()) {
            log.info("Resolved Google credentials from classpath:{}", classpathLocation);
            return classpath.getInputStream();
        }

        throw new IOException(
                "Google service account key not found at '" + configured
                        + "'. Use classpath:google-service-account.json in Docker, or mount a file path.");
    }

    private static EventDateTime toEventDateTime(ZonedDateTime zdt) {
        String rfc = zdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return new EventDateTime()
                .setDateTime(new DateTime(rfc))
                .setTimeZone(zdt.getZone().getId());
    }

    public ZoneId zoneId() {
        return SCHEDULING_ZONE;
    }

    public static boolean looksVirtual(String modality) {
        String m = modality != null ? modality.toLowerCase(Locale.ROOT) : "";
        return m.contains("virtual");
    }
}
