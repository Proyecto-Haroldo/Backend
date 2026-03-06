package itm.proyectoharoldo.backend.Models.DTO.Auth;

import itm.proyectoharoldo.backend.Models.ClientType;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;    
    private String cedulaOrNIT;
    private String legalName;
    private ClientType clientType;
    private String email;
    private String sector;
    private String roleName;

}
