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

public class QuestionnaireResult {

    QuestionnaireMetadata metadata;
    List<QuestionnaireAnswer> answers;

    public QuestionnaireResult(QuestionnaireMetadata metadata, List<QuestionnaireAnswer> answers) {
        this.metadata = metadata;
        this.answers = answers;
    }

    public QuestionnaireResult() {
    }

    public QuestionnaireMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(QuestionnaireMetadata metadata) {
        this.metadata = metadata;
    }

    public List<QuestionnaireAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionnaireAnswer> answers) {
        this.answers = answers;
    }
}
