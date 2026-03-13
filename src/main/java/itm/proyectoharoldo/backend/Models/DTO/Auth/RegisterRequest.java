package itm.proyectoharoldo.backend.Models.DTO.Auth;

import itm.proyectoharoldo.backend.Models.Role;
import itm.proyectoharoldo.backend.Models.Enums.ClientType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private String cedulaOrNIT;
    private String legalName;
    private ClientType clientType;
    private Role role;
    private String sector;

}
