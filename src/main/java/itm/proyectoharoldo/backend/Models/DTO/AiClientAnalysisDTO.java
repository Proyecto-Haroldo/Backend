package itm.proyectoharoldo.backend.Models.DTO;

import java.time.LocalDateTime;

public class AiClientAnalysisDTO {
    private Long questionnaireId;
    private LocalDateTime timestamp;
    private String recommendation;

    public AiClientAnalysisDTO(Long questionnaireId, LocalDateTime timestamp, String recommendation) {
        this.questionnaireId = questionnaireId;
        this.timestamp = timestamp;
        this.recommendation = recommendation;
    }

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}

