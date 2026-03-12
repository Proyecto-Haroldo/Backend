package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "userid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "cedulaornit", nullable = false, unique = true)
    private String cedulaOrNIT;

    @Column(name = "legalname", nullable = false)
    private String legalName;

    @Column(name = "clienttype", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "sector", nullable = false)
    private String sector;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @JsonManagedReference("user-questionnaires")
    private List<Questionnaire> questionnaires;

    @ManyToMany
    @JoinTable(
            name = "userspecialities",
            joinColumns = @JoinColumn(name = "user", referencedColumnName = "userid"),
            inverseJoinColumns = @JoinColumn(name = "category", referencedColumnName = "categoryid")
    )
    private Set<Category> specialities;

}
