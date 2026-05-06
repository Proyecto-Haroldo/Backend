package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Services.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
@RequiredArgsConstructor
public class WebQuestionController {

    private final WebQuestionService webQuestionService;

    @GetMapping
    public ResponseEntity<List<QuestionWebModel>> getAll() {
        return ResponseEntity.ok(webQuestionService.getAllQuestions());
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<QuestionWebModel>> getByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(webQuestionService.getQuestionsByCategory(categoryName));
    }

    @GetMapping("/questionnaire/{questionnaireId}")
    public ResponseEntity<List<QuestionWebModel>> getByQuestionnaire(@PathVariable Long questionnaireId) {
        return ResponseEntity.ok(webQuestionService.getQuestionsByQuestionnaire(questionnaireId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionWebModel> getById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(webQuestionService.getQuestionById(id));
    }

    @PostMapping
    public ResponseEntity<Question> create(@RequestBody QuestionWebModel webModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(webQuestionService.createQuestion(webModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> update(@PathVariable Long id, @RequestBody QuestionWebModel webModel) {
        return ResponseEntity.ok(webQuestionService.updateQuestion(id, webModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
        webQuestionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}