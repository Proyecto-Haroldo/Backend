package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Auth.UserDTO;
import itm.proyectoharoldo.backend.Services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@NonNull @PathVariable Long userId) {
        return userService.getUserById(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @SuppressWarnings("null")
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getUserStatusByEmailFromSecurityContext() {
        UserDTO userDTO = userService.getUserByEmail(
            SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(Map.of("status", userDTO.getStatus().name()));
    }

    @SuppressWarnings("null")
    @GetMapping("{userId}/status")
    public ResponseEntity<Map<String, String>> getUserStatusByUserId(@PathVariable Long userId) {
        UserDTO userDTO = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(Map.of("status", userDTO.getStatus().name()));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @NonNull UserDTO userToSave) {
        return ResponseEntity.ok(userService.updateUser(userToSave));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}