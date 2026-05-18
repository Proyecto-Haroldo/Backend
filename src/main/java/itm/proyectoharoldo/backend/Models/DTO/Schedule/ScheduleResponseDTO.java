package itm.proyectoharoldo.backend.Models.DTO.Schedule;

import com.fasterxml.jackson.annotation.JsonInclude;

import itm.proyectoharoldo.backend.Models.Schedule;
import itm.proyectoharoldo.backend.Utility.ScheduleDisplayLabels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {

    private static final DateTimeFormatter TIME_DISPLAY =
            DateTimeFormatter.ofPattern("h:mm a", Locale.US);

    private Long id;
    private Long advisorId;
    private Long clientId;
    private String advisorEmail;
    private String clientEmail;
    private String clientType;
    private String serviceType;
    private LocalDate date;
    private String time;
    private String modality;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String additionalNotes;
    private String status;
    private String meetLink;

    public static ScheduleResponseDTO fromEntity(Schedule s) {
        return ScheduleResponseDTO.builder()
                .id(s.getId())
                .advisorId(s.getAdvisorId())
                .clientId(s.getClientId())
                .advisorEmail(s.getAdvisorEmail())
                .clientEmail(s.getClientEmail())
                .clientType(ScheduleDisplayLabels.clientTypeLabel(s.getClientType()))
                .serviceType(s.getServiceType())
                .date(s.getScheduledDate())
                .time(formatTime(s.getMeetingTime()))
                .modality(ScheduleDisplayLabels.modalityLabel(s.getModality()))
                .additionalNotes(s.getAdditionalNotes())
                .status(ScheduleDisplayLabels.statusLabel(s.getStatus()))
                .meetLink(s.getMeetLink())
                .build();
    }

    private static String formatTime(LocalTime meetingTime) {
        return meetingTime == null ? "" : meetingTime.format(TIME_DISPLAY);
    }
}
