package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answersofquestionnaire")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswersOfQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid")
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question", referencedColumnName = "questionid", nullable = false)
    private Question question;

    @Column(name = "answertext", columnDefinition = "TEXT")
    private String answerText;
}
