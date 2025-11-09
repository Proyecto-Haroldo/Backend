package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.DTO.QuestionnaireDTO;
import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionnaireRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<QuestionnaireDTO> getAllQuestionnaires() {
        return questionnaireRepository.findAll()
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList());
    }

    public Optional<QuestionnaireDTO> getQuestionnaireById(Long id) {
        return questionnaireRepository.findById(id)
                .map(this::toQuestionnaireDTO);
    }

    public List<QuestionnaireDTO> getByCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(cat -> questionnaireRepository.findByCategory(cat)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    public List<QuestionnaireDTO> getByCreator(Long creatorId) {
        Optional<User> user = userRepository.findById(creatorId);
        return user.map(creator -> questionnaireRepository.findByCreator(creator)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    public Questionnaire updateQuestionnaire(Long id, Questionnaire questionnaire) {
        questionnaire.setId(id);
        return questionnaireRepository.save(questionnaire);
    }

    public void deleteQuestionnaire(Long id) {
        questionnaireRepository.deleteById(id);
    }

    // Conversión a DTO
    public QuestionnaireDTO toQuestionnaireDTO(Questionnaire questionnaire) {
        Long categoryId = questionnaire.getCategory() != null ? questionnaire.getCategory().getCategoryid() : null;
        Long creatorId = questionnaire.getCreator() != null ? questionnaire.getCreator().getUserId() : null;

        String categoryName = null;
        String creatorName = null;

        // Obtener nombre de la categoría si existe
        if (categoryId != null) {
            categoryName = categoryRepository.findById(categoryId)
                    .map(Category::getCategory)
                    .orElse(null);
        }

        // Obtener nombre del creador si existe
        if (creatorId != null) {
            creatorName = userRepository.findById(creatorId)
                    .map(User::getLegalName)
                    .orElse(null);
        }

        return new QuestionnaireDTO(
                questionnaire.getId(),
                categoryId,
                creatorId,
                creatorName,
                categoryName);
    }
}
