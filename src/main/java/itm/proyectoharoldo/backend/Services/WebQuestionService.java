package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.Question;
import itm.proyectoharoldo.backend.Models.QuestionType;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionRepository;
import itm.proyectoharoldo.backend.Repositories.QuestionnaireRepository;
import itm.proyectoharoldo.backend.Utility.QuestionConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WebQuestionService {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;

    public WebQuestionService(CategoryRepository categoryRepository, QuestionRepository questionRepository, QuestionnaireRepository questionnaireRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.questionnaireRepository = questionnaireRepository;
    }

    public QuestionWebModel CreateQuestionWebModel (Question question, QuestionType questionType, List<AnswersOptionWebModel> possibleOptions, List<GlossaryWord> glossaryWords)
    {
        return QuestionWebModel.builder()
                .id(question.getQuestionid())
                .category(question.getQuestionnaire().getCategory().getCategory())
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

        Question question = QuestionConverter.convertWebModelToEntity(webModel, questionnaireRepository.findByCategory(category).getFirst());
        return questionRepository.save(question);
    }

    @Transactional
    public Question updateQuestionOnDatabase(Long id, QuestionWebModel webModel) {
        Question question = questionRepository.findById(id).get();
        Question updatedQuestion = QuestionConverter.convertWebModelToEntity(webModel, question.getQuestionnaire());
        return questionRepository.save(updatedQuestion);
    }

    @Transactional
    public void deleteQuestionOnDatabase(Long id) {
        questionRepository.deleteById(id);
    }

}
