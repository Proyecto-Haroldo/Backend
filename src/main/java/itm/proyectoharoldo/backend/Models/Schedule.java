package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "advisor_id", nullable = false)
    private Long advisorId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "advisor_email", nullable = false)
    private String advisorEmail;

    @Column(name = "client_email", nullable = false)
    private String clientEmail;

    @Column(name = "client_type", nullable = false)
    private String clientType;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "meeting_time", nullable = false)
    private LocalTime meetingTime;

    @Column(name = "modality", nullable = false)
    private String modality;

    @Column(name = "additional_notes")
    private String additionalNotes;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "meet_link")
    private String meetLink;
}
