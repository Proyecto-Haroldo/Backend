package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import itm.proyectoharoldo.backend.Models.Enums.ClientType;
import itm.proyectoharoldo.backend.Models.Enums.UserStatus;
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

    @Column(name = "cedulaornit", nullable = false)
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "network")
    private String network;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @OneToMany(mappedBy = "creator")
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