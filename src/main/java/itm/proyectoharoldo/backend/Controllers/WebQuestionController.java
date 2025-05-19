package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Services.MultipleOptionAnswersService;
import itm.proyectoharoldo.backend.Services.WebQuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class WebQuestionController {

    private final WebQuestionService webQuestionService;
    private final QuestionRepository questionRepository;
    private final MultipleOptionAnswersService multipleOptionAnswersService;

    public WebQuestionController(WebQuestionService webQuestionService, QuestionRepository questionRepository, MultipleOptionAnswersService multipleOptionAnswersService) {
        this.webQuestionService = webQuestionService;
        this.questionRepository = questionRepository;
        this.multipleOptionAnswersService = multipleOptionAnswersService;
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
                            category,
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
        Category category = question.getCategory();
        QuestionType questionType = question.getQuestionType();
        return webQuestionService.CreateQuestionWebModel(
                category,
                question,
                questionType,
                multipleOptionAnswersService.getAnswersAsWebModel(question),
                List.of()
        );
    }

}
