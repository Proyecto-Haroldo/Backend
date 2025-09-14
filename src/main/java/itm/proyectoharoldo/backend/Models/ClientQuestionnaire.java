package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientquestionnaire")
@AllArgsConstructor
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
    private Client client;

    @Column(name = "timewhensolved")
    private LocalDateTime timeWhenSolved;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<AiClientAnalysis> analyses;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswersOfQuestionnaire> answers;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private QuestionnaireState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getTimeWhenSolved() {
        return timeWhenSolved;
    }

    public void setTimeWhenSolved(LocalDateTime timeWhenSolved) {
        this.timeWhenSolved = timeWhenSolved;
    }

    public List<AiClientAnalysis> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<AiClientAnalysis> analyses) {
        this.analyses = analyses;
    }

    public List<AnswersOfQuestionnaire> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersOfQuestionnaire> answers) {
        this.answers = answers;
    }

    public QuestionnaireState getState(){
        return state;
    }

    public void setState(QuestionnaireState  state){
        this.state = state;
    }
}
