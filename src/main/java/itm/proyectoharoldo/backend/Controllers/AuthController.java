package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.DTO.AuthRequest;
import itm.proyectoharoldo.backend.Models.DTO.RegisterRequest;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import itm.proyectoharoldo.backend.Utility.JwtUtil;
import lombok.RequiredArgsConstructor;
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
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Client client = clientRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(client.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (clientRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        }

        if(clientRepository.findByCedulaOrNIT(request.getCedulaOrNIT()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con esta cédula/NIT");
        }

        Client newClient = new Client();
        newClient.setEmail(request.getEmail());
        newClient.setPassword(passwordEncoder.encode(request.getPassword()));
        newClient.setCedulaOrNIT(request.getCedulaOrNIT());
        newClient.setLegalName(request.getLegalName());
        newClient.setClientType(request.getClientType());
        newClient.setRole(request.getRole());

        clientRepository.save(newClient);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }

}



