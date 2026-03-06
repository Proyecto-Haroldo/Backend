package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Services.*;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class WebQuestionController {

    private final WebQuestionService webQuestionService;
    private final QuestionRepository questionRepository;
    private final MultipleOptionAnswersService multipleOptionAnswersService;
    private final KeywordsService keywordsService;

    @GetMapping
    public List<QuestionWebModel> getEmAll() {
        // Use optimized query with JOIN FETCH
        List<Question> questions = questionRepository.findAllWithOptions();
        List<QuestionWebModel> questionWebModels = new ArrayList<>();

        // Convert to web models
        for (Question question : questions) {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of());
            questionWebModels.add(model);
        }

        // Batch process keywords for all questions
        keywordsService.enrichQuestionsWithKeywords(questionWebModels);

        return questionWebModels;
    }

    @GetMapping("/category/{categoryName}")
    public List<QuestionWebModel> getQuestionsByCategory(@PathVariable String categoryName) {
        // Use optimized query with JOIN FETCH
        List<Question> questions = questionRepository.findByCategoryNameWithOptions(categoryName);
        List<QuestionWebModel> questionWebModels = new ArrayList<>();

        // Convert to web models
        for (Question question : questions) {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of());
            questionWebModels.add(model);
        }

        // Batch process keywords for all questions
        keywordsService.enrichQuestionsWithKeywords(questionWebModels);

        return questionWebModels;
    }

    @GetMapping("/questionnaire/{questionnaireId}")
    public List<QuestionWebModel> getQuestionsByQuestionnaire(@PathVariable Long questionnaireId) {
        // Use optimized query with JOIN FETCH
        List<Question> questions = questionRepository.findByQuestionnaireWithOptions(questionnaireId);
        List<QuestionWebModel> questionWebModels = new ArrayList<>();

        // Convert to web models
        for (Question question : questions) {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of());
            questionWebModels.add(model);
        }

        // Batch process keywords for all questions
        keywordsService.enrichQuestionsWithKeywords(questionWebModels);

        return questionWebModels;
    }

    @GetMapping("/{id}")
    public QuestionWebModel getquestionbyid(@PathVariable Long id) {
        Question question = questionRepository.findById(id).get();
        QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                question,
                question.getQuestionType(),
                multipleOptionAnswersService.getAnswersAsWebModel(question),
                List.of());

        keywordsService.enrichQuestionWithKeywords(model);

        return model;
    }

    @PostMapping
    public Question createQuestion(@RequestBody QuestionWebModel webModel) {
        Question savedQuestion = webQuestionService.createQuestionOnDatabase(webModel);
        return savedQuestion;
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody QuestionWebModel webModel) {
        Question updatedQuestion = webQuestionService.updateQuestionOnDatabase(id, webModel);
        return updatedQuestion;
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        webQuestionService.deleteQuestionOnDatabase(id);
    }

}
