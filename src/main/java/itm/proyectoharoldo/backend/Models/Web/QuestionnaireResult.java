package itm.proyectoharoldo.backend.Models.Web;

/*
export interface QuestionnaireResult {
  metadata: QuestionnaireMetadata;
  answers: QuestionnaireAnswer[];
}
 */

public class QuestionnaireResult {

    QuestionnaireMetadata metadata;
    QuestionnaireAnswer answers;

    public QuestionnaireResult(QuestionnaireMetadata metadata, QuestionnaireAnswer answers) {
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

    public QuestionnaireAnswer getAnswers() {
        return answers;
    }

    public void setAnswers(QuestionnaireAnswer answers) {
        this.answers = answers;
    }

}
