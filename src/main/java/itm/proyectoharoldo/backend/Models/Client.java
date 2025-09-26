package itm.proyectoharoldo.backend.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @Column(name="clientid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(name="cedulaornit", nullable = false, unique = true)
    private String cedulaOrNIT;

    @Column(name="legalname", nullable = false)
    private String legalName;

    @Column(name="clienttype", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ClientQuestionnaire> questionnaires;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getCedulaOrNIT() {
        return cedulaOrNIT;
    }

    public void setCedulaOrNIT(String cedulaOrNIT) {
        this.cedulaOrNIT = cedulaOrNIT;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ClientQuestionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<ClientQuestionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }
}
