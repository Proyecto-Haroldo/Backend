package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionnaireDTO;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Services.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
@CrossOrigin(origins = "*") 
@RequiredArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<QuestionnaireDTO>> getAllQuestionnaires() {
        return ResponseEntity.ok(questionnaireService.getAllQuestionnaires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> getQuestionnaireById(@PathVariable Long id) {
        return questionnaireService.getQuestionnaireById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<QuestionnaireDTO>> getByCategory(@PathVariable Long categoryId) {
        List<QuestionnaireDTO> questionnaires = questionnaireService.getByCategory(categoryId);
        if (questionnaires.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<QuestionnaireDTO>> getByCreator(@PathVariable Long creatorId) {
        List<QuestionnaireDTO> questionnaires = questionnaireService.getByCreator(creatorId);
        if (questionnaires.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questionnaires);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<QuestionnaireDTO> create(@RequestBody QuestionnaireDTO questionnaire) {
        Questionnaire newQuestionnaire = new Questionnaire();
        newQuestionnaire.setCategory(categoryRepository.findById(questionnaire.getCategoryId()).get());
        newQuestionnaire.setTitle(questionnaire.getTitle());
        newQuestionnaire.setCreator(userRepository.findById(questionnaire.getCreatorId()).get());

        return ResponseEntity.ok(questionnaireService.toQuestionnaireDTO(questionnaireService.createQuestionnaire(newQuestionnaire)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Questionnaire> update(@PathVariable Long id, @RequestBody Questionnaire questionnaire) {
        if (questionnaireService.getQuestionnaireById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questionnaireService.updateQuestionnaire(id, questionnaire));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (questionnaireService.getQuestionnaireById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        questionnaireService.deleteQuestionnaire(id);
        return ResponseEntity.noContent().build();
    }
    
}
