package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionnaireDTO;

import lombok.RequiredArgsConstructor;

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
    public Optional<QuestionnaireDTO> getQuestionnaireById(Long id) {
        return questionnaireRepository.findById(id)
                .map(this::toQuestionnaireDTO);
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> getByCategory(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(cat -> questionnaireRepository.findByCategory(cat)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<QuestionnaireDTO> getByCreator(Long creatorId) {
        Optional<User> user = userRepository.findById(creatorId);
        return user.map(creator -> questionnaireRepository.findByCreator(creator)
                .stream()
                .map(this::toQuestionnaireDTO)
                .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Transactional
    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    @Transactional
    public Questionnaire updateQuestionnaire(Long id, Questionnaire questionnaire) {
        questionnaire.setId(id);
        return questionnaireRepository.save(questionnaire);
    }

    @Transactional
    public void deleteQuestionnaire(Long id) {
        questionnaireRepository.deleteById(id);
    }
    
    public QuestionnaireDTO toQuestionnaireDTO(Questionnaire questionnaire) {
        QuestionnaireDTO dto = new QuestionnaireDTO();

        Category category = questionnaire.getCategory();
        User creator = questionnaire.getCreator();
        
        dto.setId(questionnaire.getId());
        dto.setTitle(questionnaire.getTitle());

        if(category != null){
            dto.setCategoryId(category.getCategoryid());
            dto.setCategoryName(category.getCategory());
        }
        
        if(creator != null){
            dto.setCreatorId(creator.getUserId());
            dto.setCreatorName(creator.getLegalName());
        }
        
        return dto;
    }
}
