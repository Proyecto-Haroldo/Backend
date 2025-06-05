package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multipleoptionquestionanswers")
@AllArgsConstructor
@NoArgsConstructor
public class MultipleOptionQuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "optionanswerid")
    private Long optionanswerid;

    @Column(name = "answertext", nullable = false, columnDefinition = "TEXT")
    private String answertext;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "question", referencedColumnName = "questionid", nullable = false)
    private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswertext() {
        return answertext;
    }

    public void setAnswertext(String answertext) {
        this.answertext = answertext;
    }

    public Long getOptionanswerid() {
        return optionanswerid;
    }

    public void setOptionanswerid(Long optionanswerid) {
        this.optionanswerid = optionanswerid;
    }
}
