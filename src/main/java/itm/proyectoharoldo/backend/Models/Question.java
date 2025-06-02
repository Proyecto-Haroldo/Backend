package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionid")
    private Long questionid;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name="clienttype", nullable = true)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "category", referencedColumnName = "categoryid", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultipleOptionQuestionAnswer> options;

    public Long getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Long questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<MultipleOptionQuestionAnswer> getOptions() {
        return options;
    }

    public void setOptions(List<MultipleOptionQuestionAnswer> options) {
        this.options = options;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

}
