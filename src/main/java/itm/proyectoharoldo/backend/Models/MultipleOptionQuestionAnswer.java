package itm.proyectoharoldo.backend.Models;

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
    public Long optionanswerid;

    @Column(name = "answertext", nullable = false, columnDefinition = "TEXT")
    public String answertext;

    @ManyToOne
    @JoinColumn(name = "question", referencedColumnName = "questionid", nullable = false)
    public Question question;
}
