package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Models.DTO.AuthRequest;
import itm.proyectoharoldo.backend.Models.DTO.RegisterRequest;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Utility.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole(),
                "id", user.getUserId(),
                "message", "Inicio de sesión exitoso."));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        }

        if (userRepository.findByCedulaOrNIT(request.getCedulaOrNIT()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con esta cédula/NIT");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setCedulaOrNIT(request.getCedulaOrNIT());
        newUser.setLegalName(request.getLegalName());
        newUser.setClientType(request.getClientType());
        newUser.setRole(request.getRole());

        String token = jwtUtil.generateToken(newUser.getEmail());

        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", newUser.getRole(),
                "id", newUser.getUserId(),
                "message", "Registro de usuario exitoso."));
    }

}
