package itm.proyectoharoldo.backend.Models.DTO.Auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    
    private String email;
    private String password;

}
