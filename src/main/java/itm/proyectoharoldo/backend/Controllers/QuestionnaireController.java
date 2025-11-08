package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.DTOs.QuestionnaireDTO;
import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Services.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @GetMapping("/all")
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

    @PostMapping
    public ResponseEntity<Questionnaire> create(@RequestBody Questionnaire questionnaire) {
        return ResponseEntity.ok(questionnaireService.createQuestionnaire(questionnaire));
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
