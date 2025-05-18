package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionid")
    public Long questionid;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    public String question;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "categoryid", nullable = false)
    public Category category;

}
