package itm.proyectoharoldo.backend.Utility;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import itm.proyectoharoldo.backend.Models.Question;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static Question convertWebModelToEntity(QuestionWebModel webModel, Category category) {
        Question question = new Question();
        question.setQuestion(webModel.getDescription());
        question.setQuestionType(webModel.getType());
        question.setCategory(category);
        List<MultipleOptionQuestionAnswer> options = webModel.getOptions().stream()
                .map(optionWeb -> convertOptionWebModel(optionWeb, question))
                .collect(Collectors.toList());
        question.setOptions(options);

        return question;
    }

    private static MultipleOptionQuestionAnswer convertOptionWebModel(AnswersOptionWebModel optionWeb, Question question) {
        MultipleOptionQuestionAnswer option = new MultipleOptionQuestionAnswer();
        option.setAnswertext(optionWeb.getText());
        option.setQuestion(question);  // Asociar la pregunta
        return option;
    }
}
