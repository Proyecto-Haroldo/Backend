package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import itm.proyectoharoldo.backend.Models.Question;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Repositories.MultipleOptionQuestionAnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MultipleOptionAnswersService {

    private final MultipleOptionQuestionAnswerRepository answerRepository;

    public MultipleOptionAnswersService(MultipleOptionQuestionAnswerRepository answerRepository){
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public List<MultipleOptionQuestionAnswer> getAnswersByQuestionId(Long questionid){
        return answerRepository.findByQuestion_questionid(questionid);
    }

    public List<AnswersOptionWebModel> getAnswersAsWebModel(Question question){
        List<AnswersOptionWebModel> answersOptionWebModel = new ArrayList<AnswersOptionWebModel>();
        List<MultipleOptionQuestionAnswer> originalAnswers = question.getOptions();

        for(MultipleOptionQuestionAnswer answer : originalAnswers){
            AnswersOptionWebModel convertedAnswer = new AnswersOptionWebModel();
            convertedAnswer.setId(answer.getOptionanswerid());
            convertedAnswer.setText(answer.getAnswertext());
            answersOptionWebModel.add(convertedAnswer);
        }

        return answersOptionWebModel;
    }

}
