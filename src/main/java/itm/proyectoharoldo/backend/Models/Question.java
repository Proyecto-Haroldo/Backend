package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import itm.proyectoharoldo.backend.Models.Enums.ClientType;
import itm.proyectoharoldo.backend.Models.Enums.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionid")
    private Long questionid;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name = "clienttype", nullable = true)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "questionnaire", referencedColumnName = "id")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultipleOptionQuestionAnswer> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_question_id")
    private Question parentQuestion;

    @Column(name = "parent_answer_trigger")
    private String parentAnswerTrigger;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "section")
    private String section;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswersOfQuestionnaire> answersInQuestionnaires;

}