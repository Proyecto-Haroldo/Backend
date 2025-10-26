package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.Question;
import itm.proyectoharoldo.backend.Models.QuestionType;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Utility.QuestionConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WebQuestionService {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    public WebQuestionService(CategoryRepository categoryRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    public QuestionWebModel CreateQuestionWebModel (Question question, QuestionType questionType, List<AnswersOptionWebModel> possibleOptions, List<GlossaryWord> glossaryWords)
    {
        return new QuestionWebModel.Builder()
                .id(question.getQuestionid())
                .category(question.getCategory().getCategory())
                .question(question.getQuestion())
                .type(questionType)
                .options(possibleOptions)
                .keywords(glossaryWords)
                .build();
    }

    @Transactional
    public Question createQuestionOnDatabase(QuestionWebModel webModel) {
        String categoryName = webModel.getCategory();
        Category category = categoryRepository.findByCategory(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Question question = QuestionConverter.convertWebModelToEntity(webModel, category);
        return questionRepository.save(question);
    }

}
