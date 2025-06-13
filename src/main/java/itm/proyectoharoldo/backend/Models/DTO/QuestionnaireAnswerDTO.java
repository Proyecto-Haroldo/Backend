package itm.proyectoharoldo.backend.Models.DTO;

public class QuestionnaireAnswerDTO {
    private Long questionId;
    private String questionText;
    private String answer;

    public QuestionnaireAnswerDTO(Long questionId, String questionText, String answer) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}

