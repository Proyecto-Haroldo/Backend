package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Services.KeywordsService;
import itm.proyectoharoldo.backend.Services.MultipleOptionAnswersService;
import itm.proyectoharoldo.backend.Services.WebQuestionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
@CrossOrigin(origins = "*") // Add CORS support
public class WebQuestionController {

    private final WebQuestionService webQuestionService;
    private final QuestionRepository questionRepository;
    private final MultipleOptionAnswersService multipleOptionAnswersService;
    private final CategoryRepository categoryRepository;
    private final KeywordsService keywordsService;

    public WebQuestionController(WebQuestionService webQuestionService,
                                 QuestionRepository questionRepository,
                                 MultipleOptionAnswersService multipleOptionAnswersService,
                                 CategoryRepository categoryRepository,
                                 KeywordsService keywordsService) {
        this.webQuestionService = webQuestionService;
        this.questionRepository = questionRepository;
        this.multipleOptionAnswersService = multipleOptionAnswersService;
        this.categoryRepository = categoryRepository;
        this.keywordsService = keywordsService;
    }

    @GetMapping
    public List<QuestionWebModel> getEmAll(){
        // Use optimized query with JOIN FETCH
        List<Question> questions = questionRepository.findAllWithOptions();
        List<QuestionWebModel> questionWebModels = new ArrayList<>();

        // Convert to web models
        for(Question question : questions){
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of()
            );
            questionWebModels.add(model);
        }

        // Batch process keywords for all questions
        keywordsService.enrichQuestionsWithKeywords(questionWebModels);

        return questionWebModels;
    }

    @GetMapping("/categoria/{categoryName}")
    public List<QuestionWebModel> getQuestionsByCategory(@PathVariable String categoryName) {
        // Use optimized query with JOIN FETCH
        List<Question> questions = questionRepository.findByCategoryNameWithOptions(categoryName);
        List<QuestionWebModel> questionWebModels = new ArrayList<>();
        
        // Convert to web models
        for(Question question : questions) {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of()
            );
            questionWebModels.add(model);
        }
        
        // Batch process keywords for all questions
        keywordsService.enrichQuestionsWithKeywords(questionWebModels);
        
        return questionWebModels;
    }

    // Paginated endpoints for better performance with large datasets
    @GetMapping("/paginated")
    public Page<QuestionWebModel> getQuestionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByCategoryWithOptions(null, pageable);
        
        return questionPage.map(question -> {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of()
            );
            keywordsService.enrichQuestionWithKeywords(model);
            return model;
        });
    }

    @GetMapping("/categoria/{categoryName}/paginated")
    public Page<QuestionWebModel> getQuestionsByCategoryPaginated(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Category category = categoryRepository.findByCategory(categoryName)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByCategoryWithOptions(category, pageable);
        
        return questionPage.map(question -> {
            QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                    question,
                    question.getQuestionType(),
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of()
            );
            keywordsService.enrichQuestionWithKeywords(model);
            return model;
        });
    }

    @GetMapping("/{id}")
    public QuestionWebModel getquestionbyid(@PathVariable Long id){
        Question question = questionRepository.findById(id).get();
        QuestionWebModel model = webQuestionService.CreateQuestionWebModel(
                question,
                question.getQuestionType(),
                multipleOptionAnswersService.getAnswersAsWebModel(question),
                List.of()
        );

        keywordsService.enrichQuestionWithKeywords(model);

        return model;
    }

    @PostMapping
    public Question createQuestion(@RequestBody QuestionWebModel webModel) {
        Question savedQuestion = webQuestionService.createQuestionOnDatabase(webModel);
        return savedQuestion;
    }
}
