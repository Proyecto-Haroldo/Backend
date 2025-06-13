package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientanswers")
@AllArgsConstructor
@NoArgsConstructor
public class ClientAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "clientid")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question", referencedColumnName = "questionid")
    private Question question;

    @Column(name = "answertext", columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "timestampwhenanswered")
    private LocalDateTime timestampWhenAnswered;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public LocalDateTime getTimestampWhenAnswered() {
        return timestampWhenAnswered;
    }

    public void setTimestampWhenAnswered(LocalDateTime timestampWhenAnswered) {
        this.timestampWhenAnswered = timestampWhenAnswered;
    }
}
