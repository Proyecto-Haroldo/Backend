package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Repositories.QuestionnaireRepository;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questionnaires")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class QuestionnaireController {

    private final QuestionnaireRepository questionnaireRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires() {
        List<Questionnaire> questionnaires = questionnaireRepository.findAll();
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Questionnaire> getQuestionnaireById(@PathVariable Long id) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
        return questionnaire.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Questionnaire>> getQuestionnairesByCategory(@PathVariable Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            List<Questionnaire> questionnaires = questionnaireRepository.findByCategory(category.get());
            return ResponseEntity.ok(questionnaires);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody Questionnaire questionnaire) {
        Questionnaire savedQuestionnaire = questionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(savedQuestionnaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Questionnaire> updateQuestionnaire(@PathVariable Long id, @RequestBody Questionnaire questionnaire) {
        if (!questionnaireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        questionnaire.setId(id);
        Questionnaire updatedQuestionnaire = questionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(updatedQuestionnaire);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionnaire(@PathVariable Long id) {
        if (!questionnaireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        questionnaireRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
