package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionnaireDTO;
import itm.proyectoharoldo.backend.Services.QuestionnaireService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @GetMapping
    public ResponseEntity<List<QuestionnaireDTO>> getAllQuestionnaires() {
        return ResponseEntity.ok(questionnaireService.getAllQuestionnaires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> getQuestionnaireById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(questionnaireService.getQuestionnaireDTOById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<QuestionnaireDTO>> getByCategory(@PathVariable @NonNull Long categoryId) {
        return ResponseEntity.ok(questionnaireService.getByCategory(categoryId));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<QuestionnaireDTO>> getByCreator(@PathVariable @NonNull Long creatorId) {
        return ResponseEntity.ok(questionnaireService.getByCreator(creatorId));
    }

    @PostMapping
    public ResponseEntity<QuestionnaireDTO> create(@RequestBody QuestionnaireDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionnaireService.createQuestionnaire(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionnaireDTO> update(@PathVariable Long id, @RequestBody QuestionnaireDTO dto) {
        return ResponseEntity.ok(questionnaireService.updateQuestionnaire(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
        questionnaireService.deleteQuestionnaire(id);
        return ResponseEntity.noContent().build();
    }
}