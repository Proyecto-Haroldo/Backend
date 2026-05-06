package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionnaireDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> getAllQuestionnaires() {
        return questionnaireRepository.findAll()
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<QuestionnaireDTO> getQuestionnaireById(@NonNull Long id) {
        return questionnaireRepository.findById(id)
                .map(this::toQuestionnaireDTO);
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> getByCategory(@NonNull Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(cat -> questionnaireRepository.findByCategory(cat)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> getByCreator(@NonNull Long creatorId) {
        Optional<User> user = userRepository.findById(creatorId);
        return user.map(creator -> questionnaireRepository.findByCreator(creator)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Transactional
    @SuppressWarnings("null")
    public QuestionnaireDTO createQuestionnaire(QuestionnaireDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(
                        () -> new NoSuchElementException("Categoría no encontrada con id: " + dto.getCategoryId()));
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getCreatorId()));

        Questionnaire newQuestionnaire = new Questionnaire();
        newQuestionnaire.setTitle(dto.getTitle());
        newQuestionnaire.setCategory(category);
        newQuestionnaire.setCreator(creator);

        return toQuestionnaireDTO(questionnaireRepository.save(newQuestionnaire));
    }

    @Transactional
    @SuppressWarnings("null")
    public QuestionnaireDTO updateQuestionnaire(Long id, QuestionnaireDTO dto) {
        Questionnaire existing = questionnaireRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cuestionario no encontrado con id: " + id));

        if (dto.getTitle() != null)
            existing.setTitle(dto.getTitle());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(
                            () -> new NoSuchElementException("Categoría no encontrada con id: " + dto.getCategoryId()));
            existing.setCategory(category);
        }

        if (dto.getCreatorId() != null) {
            User creator = userRepository.findById(dto.getCreatorId())
                    .orElseThrow(
                            () -> new NoSuchElementException("Usuario no encontrado con id: " + dto.getCreatorId()));
            existing.setCreator(creator);
        }

        return toQuestionnaireDTO(questionnaireRepository.save(existing));
    }

    @Transactional
    public void deleteQuestionnaire(@NonNull Long id) {
        if (!questionnaireRepository.existsById(id)) {
            throw new NoSuchElementException("Cuestionario no encontrado con id: " + id);
        }
        questionnaireRepository.deleteById(id);
    }

    public QuestionnaireDTO getQuestionnaireDTOById(@NonNull Long id) {
        return questionnaireRepository.findById(id)
                .map(this::toQuestionnaireDTO)
                .orElseThrow(() -> new NoSuchElementException("Cuestionario no encontrado con id: " + id));
    }

    public QuestionnaireDTO toQuestionnaireDTO(Questionnaire questionnaire) {
        QuestionnaireDTO dto = new QuestionnaireDTO();

        Category category = questionnaire.getCategory();
        User creator = questionnaire.getCreator();

        dto.setId(questionnaire.getId());
        dto.setTitle(questionnaire.getTitle());

        if (category != null) {
            dto.setCategoryId(category.getCategoryid());
            dto.setCategoryName(category.getTitle());
        }

        if (creator != null) {
            dto.setCreatorId(creator.getUserId());
            dto.setCreatorName(creator.getLegalName());
        }

        return dto;
    }
}