package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Auth.UserDTO;
import itm.proyectoharoldo.backend.Models.DTO.*;
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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAllWithSpecialities().get().stream()
            .map(user -> new UserDTO(
                    user.getUserId(),
                    user.getCedulaOrNIT(),
                    user.getLegalName(),
                    user.getClientType(),
                    user.getEmail(),
                    user.getSector(),
                    user.getRole() != null ? user.getRole().getName() : null,
                    user.getNetwork(),
                    user.getPhone(),
                    user.getSpecialities().stream()
                        .map(category -> new CategoryDTO(
                            category.getCategoryid(),
                            category.getCategory(),
                            category.getDescription()
                        )).collect(Collectors.toSet())
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userRepository.findUserWithSpecialities(userId)
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getCedulaOrNIT(),
                        user.getLegalName(),
                        user.getClientType(),
                        user.getEmail(),
                        user.getSector(),
                        user.getRole() != null ? user.getRole().getName() : null,
                        user.getNetwork(),
                        user.getPhone(),
                        user.getSpecialities().stream()
                        .map(category -> new CategoryDTO(
                            category.getCategoryid(),
                            category.getCategory(),
                            category.getDescription()
                        )).collect(Collectors.toSet())
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
