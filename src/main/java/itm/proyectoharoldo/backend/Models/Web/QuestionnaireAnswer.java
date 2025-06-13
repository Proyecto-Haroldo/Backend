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
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

}
