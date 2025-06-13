package itm.proyectoharoldo.backend.Models.Web;

/*
export interface QuestionnaireResult {
  metadata: QuestionnaireMetadata;
  answers: QuestionnaireAnswer[];
}
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionnaireResult {

    QuestionnaireMetadata metadata;
    List<QuestionnaireAnswer> answers;

    public QuestionnaireResult(QuestionnaireMetadata metadata, List<QuestionnaireAnswer> answers) {
        this.metadata = metadata;
        this.answers = answers;
    }

    public QuestionnaireResult() {
    }

}
