package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answersofquestionnaire")
@AllArgsConstructor
@NoArgsConstructor
public class AnswersOfQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire", referencedColumnName = "clientquestionnaireid")
    private ClientQuestionnaire questionnaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question", referencedColumnName = "questionid", nullable = false)
    private Question question;

    @Column(name = "answertext", columnDefinition = "TEXT")
    private String answerText;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public ClientQuestionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(ClientQuestionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
