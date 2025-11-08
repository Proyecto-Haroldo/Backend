package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(name = "recomendacioninicial", columnDefinition = "TEXT")
    private String recomendacionInicial;

    @Column(name = "contenidorevision", columnDefinition = "TEXT")
    private String contenidoRevision;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Column(name = "timewhensolved")
    private LocalDateTime timeWhenSolved;

    @Column(name = "timewhenchecked")
    private LocalDateTime timeWhenChecked;

}
