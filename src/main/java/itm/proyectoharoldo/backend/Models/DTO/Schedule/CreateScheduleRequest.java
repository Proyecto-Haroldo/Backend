package itm.proyectoharoldo.backend.Models.DTO.Schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Booking form fields plus {@code advisorId} and {@code clientId} so invites and FK-style
 * columns always reference real users from the existing {@code users} table.
 */
@Getter
@Setter
public class CreateScheduleRequest {

    @NotBlank
    private String clientType;
    @NotBlank
    private String serviceType;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String time;
    @NotBlank
    private String modality;
    private String additionalNotes;

    @NotNull
    private Long advisorId;
    @NotNull
    private Long clientId;
}
