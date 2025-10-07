package itm.proyectoharoldo.backend.Models.DTO;

import itm.proyectoharoldo.backend.Models.ClientType;
import itm.proyectoharoldo.backend.Models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String cedulaOrNIT;
    private String legalName;
    private ClientType clientType;
    private Role role;
    private String sector;
}
