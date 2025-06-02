package itm.proyectoharoldo.backend.Models.Web;

/*
export interface QuestionnaireAnswer {
  questionId: number;
  questionTitle: string;
  answer: string | string[] | null;
  type: QuestionType;
}
 */

import itm.proyectoharoldo.backend.Models.QuestionType;

import java.util.List;

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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

}
