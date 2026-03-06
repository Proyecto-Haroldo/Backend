package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "multipleoptionquestionanswers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

}
