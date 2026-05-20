package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.DTO.Schedule.CreateScheduleRequest;
import itm.proyectoharoldo.backend.Models.DTO.Schedule.ScheduleResponseDTO;
import itm.proyectoharoldo.backend.Models.Schedule;
import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Repositories.ScheduleRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final Long CLIENT_ROLE_ID = 2L;
    private static final Long ADVISER_ROLE_ID = 3L;
    private static final int DEFAULT_MEETING_MINUTES = 45;
    private static final DateTimeFormatter TIME_DISPLAY = DateTimeFormatter.ofPattern("h:mm a", Locale.US);

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final GoogleCalendarMeetService googleCalendarMeetService;

    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> listMySchedules(String actorEmail, LocalDate from, LocalDate to) {
        User user = userRepository.findByEmail(actorEmail).orElseThrow();
        if (user.getRole() == null) {
            throw new AccessDeniedException("Rol no válido.");
        }
        
        LocalDate fromDate = from != null ? from : LocalDate.now(googleCalendarMeetService.zoneId());
        LocalDate toDate = to != null ? to : fromDate.plusDays(90);
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException("El parámetro 'to' debe ser mayor o igual que 'from'.");
        }

        Long roleId = user.getRole().getId();
        if (ADVISER_ROLE_ID.equals(roleId)) {
            return scheduleRepository.findForAdvisorInRange(user.getUserId(), fromDate, toDate).stream()
                    .map(ScheduleResponseDTO::fromEntity)
                    .toList();
        } else if (CLIENT_ROLE_ID.equals(roleId)) {
            return scheduleRepository.findForClientInRange(user.getUserId(), fromDate, toDate).stream()
                    .map(ScheduleResponseDTO::fromEntity)
                    .toList();
        } else {
            throw new AccessDeniedException("Este rol no puede consultar citas.");
        }
    }

    @Transactional
    public ScheduleResponseDTO create(String actorEmail, CreateScheduleRequest request) {
        User actor = userRepository.findByEmail(actorEmail).orElseThrow();
        User advisor = userRepository.findById(request.getAdvisorId()).orElseThrow();
        User client = userRepository.findById(request.getClientId()).orElseThrow();

        authorizeCreate(actor, request.getAdvisorId(), request.getClientId());

        boolean withMeet = GoogleCalendarMeetService.looksVirtual(request.getModality());
        if (withMeet && !googleCalendarMeetService.isConfigured()) {
            throw new IllegalStateException(
                    "Las citas virtuales requieren Google Calendar: configure GOOGLE_CALENDAR_SERVICE_ACCOUNT_KEY_PATH "
                            + "y GOOGLE_CALENDAR_ID (ID de un calendario compartido con la cuenta de servicio).");
        }

        LocalTime meetingTime = parseMeetingTime(request.getTime());
        ZonedDateTime start = parseStart(request.getDate(), meetingTime);
        ZonedDateTime end = start.plusMinutes(DEFAULT_MEETING_MINUTES);

        String summary = "Consulta — " + request.getServiceType();
        String description = buildCalendarDescription(request, advisor, client);

        String meetLink = null;
        if (withMeet) {
            meetLink = googleCalendarMeetService.createMeetEvent(summary, description, start, end);
        }

        Schedule row = Schedule.builder()
                .advisorId(advisor.getUserId())
                .clientId(client.getUserId())
                .advisorEmail(advisor.getEmail())
                .clientEmail(client.getEmail())
                .clientType(request.getClientType())
                .serviceType(request.getServiceType())
                .scheduledDate(request.getDate())
                .meetingTime(meetingTime)
                .modality(request.getModality())
                .additionalNotes(request.getAdditionalNotes())
                .status("CONFIRMED")
                .meetLink(meetLink)
                .build();

        Schedule saved = scheduleRepository.save(row);
        return ScheduleResponseDTO.fromEntity(saved);
    }

    private void authorizeCreate(User actor, Long advisorId, Long clientId) {
        if (actor.getRole() == null) {
            throw new AccessDeniedException("Rol no válido.");
        }
        Long roleId = actor.getRole().getId();
        if (CLIENT_ROLE_ID.equals(roleId)) {
            if (!actor.getUserId().equals(clientId)) {
                throw new AccessDeniedException("Un cliente solo puede agendar citas para sí mismo.");
            }
            return;
        }
        if (ADVISER_ROLE_ID.equals(roleId)) {
            if (!actor.getUserId().equals(advisorId)) {
                throw new AccessDeniedException("Un asesor solo puede crear citas donde él sea el asesor asignado.");
            }
            return;
        }
        throw new AccessDeniedException("Este rol no puede crear citas.");
    }

    private LocalTime parseMeetingTime(String timeText) {
        try {
            return LocalTime.parse(timeText.trim(), TIME_DISPLAY);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora no válido; use por ejemplo 3:30 PM.", e);
        }
    }

    private ZonedDateTime parseStart(LocalDate date, LocalTime meetingTime) {
        return ZonedDateTime.of(date, meetingTime, googleCalendarMeetService.zoneId());
    }

    private static String buildCalendarDescription(CreateScheduleRequest r, User advisor, User client) {
        StringBuilder sb = new StringBuilder();
        sb.append("Asesor: ").append(advisor.getEmail()).append('\n');
        sb.append("Cliente: ").append(client.getEmail()).append('\n');
        sb.append("Tipo de cliente: ").append(r.getClientType()).append('\n');
        sb.append("Modalidad: ").append(r.getModality()).append('\n');
        if (r.getAdditionalNotes() != null && !r.getAdditionalNotes().isBlank()) {
            sb.append("Notas: ").append(r.getAdditionalNotes());
        }
        return sb.toString();
    }
}
