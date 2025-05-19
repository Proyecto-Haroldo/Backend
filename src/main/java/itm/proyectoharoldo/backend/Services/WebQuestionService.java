package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import itm.proyectoharoldo.backend.Models.Question;
import itm.proyectoharoldo.backend.Models.QuestionType;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebQuestionService {

    public QuestionWebModel CreateQuestionWebModel (Category category, Question question, QuestionType questionType, List<AnswersOptionWebModel> possibleOptions, List<GlossaryWord> glossaryWords)
    {
        return new QuestionWebModel.Builder()
                .id(question.getQuestionid())
                .title(category.getCategory())
                .description(question.getQuestion())
                .type(questionType)
                .options(possibleOptions)
                .keywords(glossaryWords)
                .build();
    }

}
