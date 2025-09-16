package itm.proyectoharoldo.backend.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;
import itm.proyectoharoldo.backend.Models.DTO.AiClientAnalysisDTO;
import org.springframework.http.ResponseEntity;

public class AIAnalysisParser {

    public AIAnalysisResultDTO parseResponseToAnalysis(ResponseEntity<String> response){
        try{
            return new ObjectMapper().readValue(response.getBody(), AIAnalysisResultDTO.class);
        } catch (Exception ex){
            return null;
        }

    }

}
