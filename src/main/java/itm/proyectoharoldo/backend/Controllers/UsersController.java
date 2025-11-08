package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.UserDTO;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllClients() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(client -> new UserDTO(
                        client.getUserId(),
                        client.getCedulaOrNIT(),
                        client.getLegalName(),
                        client.getClientType(),
                        client.getEmail(),
                        client.getSector(),
                        client.getRole() != null ? client.getRole().getName() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getClientById(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(client -> new UserDTO(
                        client.getUserId(),
                        client.getCedulaOrNIT(),
                        client.getLegalName(),
                        client.getClientType(),
                        client.getEmail(),
                        client.getSector(),
                        client.getRole() != null ? client.getRole().getName() : null
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
