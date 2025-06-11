package itm.proyectoharoldo.backend.Models.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class ClientQuestionnaireDTO {
    private Long questionnaireId;
    private String category;
    private LocalDateTime timestamp;
    private List<QuestionnaireAnswerDTO> answers;

    public ClientQuestionnaireDTO(Long questionnaireId, String category, LocalDateTime timestamp, List<QuestionnaireAnswerDTO> answers) {
        this.questionnaireId = questionnaireId;
        this.category = category;
        this.timestamp = timestamp;
        this.answers = answers;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<QuestionnaireAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionnaireAnswerDTO> answers) {
        this.answers = answers;
    }
}

