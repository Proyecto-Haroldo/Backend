package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Services.MultipleOptionAnswersService;
import itm.proyectoharoldo.backend.Services.WebQuestionService;
import org.springframework.web.bind.annotation.*;

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

    public WebQuestionController(
        WebQuestionService webQuestionService, 
        QuestionRepository questionRepository, 
        MultipleOptionAnswersService multipleOptionAnswersService,
        CategoryRepository categoryRepository
    ) {
        this.webQuestionService = webQuestionService;
        this.questionRepository = questionRepository;
        this.multipleOptionAnswersService = multipleOptionAnswersService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<QuestionWebModel> getEmAll(){
        List<QuestionWebModel> questionWebModels = new ArrayList<QuestionWebModel>();
        List<Question> questions = questionRepository.findAll();

        for(Question question : questions){
            Category category = question.getCategory();
            QuestionType questionType = question.getQuestionType();

            questionWebModels.add(
                    webQuestionService.CreateQuestionWebModel(
                            question,
                            questionType,
                            multipleOptionAnswersService.getAnswersAsWebModel(question),
                            List.of()
                    )
            );
        }

        return questionWebModels;
    }

    @GetMapping("/categoria/{categoryName}")
    public List<QuestionWebModel> getQuestionsByCategory(@PathVariable String categoryName) {
        List<QuestionWebModel> questionWebModels = new ArrayList<>();
        
        Category category = categoryRepository.findByCategory(categoryName)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            
        List<Question> questions = questionRepository.findByCategory(category);
        
        for(Question question : questions) {
            QuestionType questionType = question.getQuestionType();
            
            questionWebModels.add(
                webQuestionService.CreateQuestionWebModel(
                    question,
                    questionType,
                    multipleOptionAnswersService.getAnswersAsWebModel(question),
                    List.of()
                )
            );
        }
        
        return questionWebModels;
    }

    @GetMapping("/categoriaycliente")
    public List<QuestionWebModel> getQuestionsByCategoryAndClientType(
            @RequestParam String category,
            @RequestParam ClientType clienttype) {

        List<Question> questions = questionRepository.findByCategory_CategoryAndClientType(category, clienttype);
        List<QuestionWebModel> questionWebModels = new ArrayList<>();

        for (Question question : questions) {
            QuestionType questionType = question.getQuestionType();

            questionWebModels.add(
                    webQuestionService.CreateQuestionWebModel(
                            question,
                            questionType,
                            multipleOptionAnswersService.getAnswersAsWebModel(question),
                            List.of()
                    )
            );
        }

        return questionWebModels;
    }

    @GetMapping("/{id}")
    public QuestionWebModel getquestionbyid(@PathVariable Long id){
        Question question = questionRepository.findById(id).get();
        QuestionType questionType = question.getQuestionType();
        return webQuestionService.CreateQuestionWebModel(
                question,
                questionType,
                multipleOptionAnswersService.getAnswersAsWebModel(question),
                List.of()
        );
    }

    @PostMapping
    public Question createQuestion(@RequestBody QuestionWebModel webModel) {
        Question savedQuestion = webQuestionService.createQuestionOnDatabase(webModel);
        return savedQuestion;
    }
}
