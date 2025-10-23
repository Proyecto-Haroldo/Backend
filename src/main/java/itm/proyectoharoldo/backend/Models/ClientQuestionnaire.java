package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "clientquestionnaire")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ClientQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientquestionnaireid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "categoryid")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "clientid")
    @JsonBackReference
    private Client client;

    @Column(name = "timewhensolved")
    private LocalDateTime timeWhenSolved;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswersOfQuestionnaire> answers;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private QuestionnaireState state;

    @Column(name = "recomendacionusuario", columnDefinition = "TEXT")
    private String recomendacionUsuario;

    @Column(name = "colorsemaforo", columnDefinition = "TEXT")
    private String colorSemaforo;

    @Column(name = "analisisasesor", columnDefinition = "TEXT")
    private String analisisAsesor;

    @Column(name = "conteo")
    private Integer conteo;

}

