package itm.proyectoharoldo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIRecommendationRequest {

    private String clientAnswers;

    public AIRecommendationRequest(String prompt){
        this.clientAnswers = prompt;
    }

    public AIRecommendationRequest(){

    }

}
