package itm.proyectoharoldo.backend.Models.DTO.Auth;

import itm.proyectoharoldo.backend.Models.Enums.*;
import itm.proyectoharoldo.backend.Models.DTO.CategoryDTO;

import java.util.Set;

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
    private String network;
    private String phone;
    private String location;
    private UserStatus status;
    private Set<CategoryDTO> specialities;

}
