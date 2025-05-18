package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multipleoptionquestionanswers")
@Data
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
}
