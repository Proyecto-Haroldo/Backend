package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid")
    private Long categoryid;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "decimalvalue")
    private BigDecimal decimalvalue;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Question> questions;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<AiClientAnalysis> aiAnalyses;

}
