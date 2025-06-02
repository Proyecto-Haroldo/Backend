package itm.proyectoharoldo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
