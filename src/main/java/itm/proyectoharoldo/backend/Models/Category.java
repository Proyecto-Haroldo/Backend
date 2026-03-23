package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "decimalvalue")
    private BigDecimal decimalvalue;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference("category-questionnaires")
    private List<Questionnaire> questionnaires;

    @ManyToMany(mappedBy = "specialities")
    @JsonIgnore
    private Set<User> usersWithSpeciality;

}
