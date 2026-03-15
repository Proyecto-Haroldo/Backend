package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import itm.proyectoharoldo.backend.Models.Enums.AnalysisStatus;

@Entity
@Table(name = "analyses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysisid")
    private Long analysisId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asesor", referencedColumnName = "userid")
    private User asesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire", referencedColumnName = "id")
    private Questionnaire questionnaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioresponde", referencedColumnName = "userid")
    private User usuarioResponde;

    @Column(name = "colorsemaforo", columnDefinition = "TEXT")
    private String colorSemaforo;

    @Column(name = "analisisIA", columnDefinition = "TEXT")
    private String analisisIA;

    @Column(name = "resumenIA", columnDefinition = "TEXT")
    private String resumenIA;

    @Column(name = "comentarioAsesor", columnDefinition = "TEXT")
    private String comentarioAsesor;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Column(name = "timewhensolved")
    private LocalDateTime timeWhenSolved;

    @Column(name = "timewhenchecked")
    private LocalDateTime timeWhenChecked;

}
