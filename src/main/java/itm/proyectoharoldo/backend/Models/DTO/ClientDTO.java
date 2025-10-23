package itm.proyectoharoldo.backend.Models.DTO;

import itm.proyectoharoldo.backend.Models.ClientType;

public class ClientDTO {

    private Long clientId;
    private String cedulaOrNIT;
    private String legalName;
    private ClientType clientType;
    private String email;
    private String sector;
    private String roleName;

    public ClientDTO(Long clientId, String cedulaOrNIT, String legalName,
                     ClientType clientType, String email, String sector, String roleName) {
        this.clientId = clientId;
        this.cedulaOrNIT = cedulaOrNIT;
        this.legalName = legalName;
        this.clientType = clientType;
        this.email = email;
        this.sector = sector;
        this.roleName = roleName;
    }

    // Getters y Setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getCedulaOrNIT() { return cedulaOrNIT; }
    public void setCedulaOrNIT(String cedulaOrNIT) { this.cedulaOrNIT = cedulaOrNIT; }

    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }

    public ClientType getClientType() { return clientType; }
    public void setClientType(ClientType clientType) { this.clientType = clientType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
