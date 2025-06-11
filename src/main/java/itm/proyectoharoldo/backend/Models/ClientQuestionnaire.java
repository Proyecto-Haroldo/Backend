package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientquestionnaire")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Client client;

    @Column(name = "timewhensolved")
    private LocalDateTime timeWhenSolved;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<AiClientAnalysis> analyses;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswersOfQuestionnaire> answers;


}
