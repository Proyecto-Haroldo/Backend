package itm.proyectoharoldo.backend.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;

public class AIAnalysisParser {

    public AIAnalysisResultDTO parseResponseToAnalysis(String json){
        try{
            return new ObjectMapper().readValue(json, AIAnalysisResultDTO.class);
        } catch (Exception ex){
            return null;
        }

    }

}
