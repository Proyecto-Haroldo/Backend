package itm.proyectoharoldo.backend.Models.DTO;

import itm.proyectoharoldo.backend.Models.ClientType;
import itm.proyectoharoldo.backend.Models.Role;

public class RegisterRequest {
    private String email;
    private String password;
    private String cedulaOrNIT;
    private String legalName;
    private ClientType clientType;
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
