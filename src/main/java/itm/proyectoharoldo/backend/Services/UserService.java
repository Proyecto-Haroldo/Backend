package itm.proyectoharoldo.backend.Services;

import java.util.List;
import java.util.NoSuchElementException;
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
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.RoleRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(@NonNull Long id) {
        return userRepository.findById(id).map(this::toUserDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(@NonNull String email) {
        return userRepository.findByEmail(email)
                .stream().map(this::toUserDTO).findFirst();
    }

    @SuppressWarnings("null")
    @Transactional
    public UserDTO updateUser(@NonNull UserDTO dto) {
        User existing = userRepository.findById(dto.getUserId())
                .orElseThrow();

        if (dto.getCedulaOrNIT() != null)
            existing.setCedulaOrNIT(dto.getCedulaOrNIT());
        if (dto.getLegalName() != null)
            existing.setLegalName(dto.getLegalName());
        if (dto.getClientType() != null)
            existing.setClientType(dto.getClientType());
        if (dto.getEmail() != null)
            existing.setEmail(dto.getEmail());
        if (dto.getSector() != null)
            existing.setSector(dto.getSector());
        if (dto.getNetwork() != null)
            existing.setNetwork(dto.getNetwork());
        if (dto.getLocation() != null)
            existing.setLocation(dto.getLocation());
        if (dto.getPhone() != null)
            existing.setPhone(dto.getPhone());
        if (dto.getStatus() != null)
            existing.setStatus(dto.getStatus());

        if (dto.getRoleName() != null) {
            existing.setRole(roleRepository.findByName(dto.getRoleName()).orElseThrow());
        }

        if (dto.getSpecialities() != null) {
            existing.setSpecialities(dto.getSpecialities()
                    .stream()
                    .map(categoryDTO -> categoryRepository.findById(categoryDTO.getCategoryId())
                            .orElseThrow())
                    .collect(Collectors.toSet()));
        }

        return toUserDTO(userRepository.save(existing));
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteUserById(long userId){
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + userId));
        userRepository.delete(existing);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();

        Set<Category> specialities = user.getSpecialities();

        dto.setUserId(user.getUserId());
        dto.setCedulaOrNIT(user.getCedulaOrNIT());
        dto.setLegalName(user.getLegalName());
        dto.setClientType(user.getClientType());
        dto.setEmail(user.getEmail());
        dto.setSector(user.getSector());
        dto.setRoleName(user.getRole() != null ? user.getRole().getName() : null);
        dto.setNetwork(user.getNetwork());
        dto.setLocation(user.getLocation());
        dto.setStatus(user.getStatus());
        dto.setPhone(user.getPhone());

        dto.setSpecialities(specialities != null ? specialities
                .stream().map(category -> new CategoryDTO(
                        category.getCategoryid(), category.getTitle(), category.getDescription(), category.getIcon()))
                .collect(Collectors.toSet()) : Set.of());

        return dto;
    }

}
