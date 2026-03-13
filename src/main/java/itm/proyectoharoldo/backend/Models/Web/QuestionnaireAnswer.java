package itm.proyectoharoldo.backend.Models.Web;

import java.util.List;

import itm.proyectoharoldo.backend.Models.Enums.QuestionType;

public class QuestionnaireAnswer {

    int questionId;
    String questionTitle;
    List<String> answer;
    QuestionType questionType;

    public QuestionnaireAnswer(int questionId, String questionTitle, List<String> answer, QuestionType questionType) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.answer = answer;
        this.questionType = questionType;
    }

    public QuestionnaireAnswer() {
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
