package itm.proyectoharoldo.backend.Interfaces;

import itm.proyectoharoldo.backend.Models.DTO.Analysis.AIRawResponse;

public interface IAIService {
    public AIRawResponse getAiRecommendation(String prompt);
}
