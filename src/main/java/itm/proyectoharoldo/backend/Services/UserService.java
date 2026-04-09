package itm.proyectoharoldo.backend.Services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Models.DTO.CategoryDTO;
import itm.proyectoharoldo.backend.Models.DTO.Auth.UserDTO;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::toUserDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(@NonNull Long id){
        return userRepository.findById(id).map(this::toUserDTO);
    }

    private UserDTO toUserDTO(User user){
        UserDTO dto = new UserDTO();

        Set<Category> specialities = user.getSpecialities();

        dto.setUserId(user.getUserId());
        dto.setCedulaOrNIT(user.getCedulaOrNIT());
        dto.setLegalName(user.getLegalName());
        dto.setClientType(user.getClientType());
        dto.setEmail(user.getEmail());
        dto.setSector(user.getSector());
        dto.setRoleName(user.getRole().getName());
        dto.setNetwork(user.getNetwork());
        dto.setLocation(user.getLocation());
        dto.setStatus(user.getStatus());
        dto.setPhone(user.getPhone());

        dto.setSpecialities(specialities != null ? specialities
            .stream().map(category -> new CategoryDTO(
                category.getCategoryid(), category.getTitle(), category.getDescription(), category.getIcon()
            )).collect(Collectors.toSet()) : Set.of());

        return dto;
    }

}
