package itm.proyectoharoldo.backend.Services;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Models.Enums.UserStatus;
import itm.proyectoharoldo.backend.Models.DTO.Auth.*;
import itm.proyectoharoldo.backend.Repositories.RoleRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Utility.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @NonNull
    private final Long USER_ROLE_ID = 2L;

    @NonNull
    private final Long ADVISER_ROLE_ID = 1L;

    @Transactional
    public AuthResponse processLogin(AuthRequest authRequest){

        User authenticatingUser = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(
            () -> new UsernameNotFoundException("El correo no está registrado")
        );

        authenticateUserFromRequest(authRequest);

        String message = "Inicio de sesión exitoso.";

        return toAuthResponse(authenticatingUser, message);

    }

    @SuppressWarnings("null")
    @Transactional
    public AuthResponse processRegister(RegisterRequest registerRequest){

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("Ya existe un usuario con el correo ingresado");
        }

        if(userRepository.findByCedulaOrNIT(registerRequest.getCedulaOrNIT()).isPresent()){
            throw new UserAlreadyExistsException("Ya existe un usuario con la cédula/NIT ingresado");
        }

        User registeredUser = userRepository.save(createUserFromRegisterRequest(registerRequest));

        return toAuthResponse(registeredUser, "Usuario registrado existosamente");

    }

    private AuthResponse toAuthResponse(User user, String message){
        AuthResponse dto = new AuthResponse();

        dto.setId(user.getUserId());
        dto.setMessage(message);
        dto.setRole(user.getRole());
        dto.setToken(jwtUtil.generateToken(user.getEmail()));

        return dto;

    }

    private void authenticateUserFromRequest(AuthRequest authRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Las credenciales ingresadas no son válidas");
        }
    }

    @SuppressWarnings("null")
    private User createUserFromRegisterRequest(RegisterRequest registerRequest){

        User newUser = new User();
        newUser.setCedulaOrNIT(registerRequest.getCedulaOrNIT());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setClientType(registerRequest.getClientType());
        newUser.setLegalName(registerRequest.getLegalName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setPhone(registerRequest.getPhone() != null ? registerRequest.getPhone() : "");
        newUser.setNetwork(registerRequest.getNetwork() != null ? registerRequest.getNetwork() : "");
        newUser.setRole(registerRequest.getRole().getId() == USER_ROLE_ID ? roleRepository.findById(USER_ROLE_ID).orElseThrow() : roleRepository.findById(registerRequest.getRole().getId()).orElseThrow());
        newUser.setStatus(registerRequest.getRole().getId() == ADVISER_ROLE_ID ? UserStatus.UNAUTHORIZED : UserStatus.AUTHORIZED);
        newUser.setLocation(registerRequest.getLocation() != null ? registerRequest.getLocation() : "");
        newUser.setSector(registerRequest.getSector() != null ? registerRequest.getSector() : "No especificado");
        
        return newUser;

    }

}