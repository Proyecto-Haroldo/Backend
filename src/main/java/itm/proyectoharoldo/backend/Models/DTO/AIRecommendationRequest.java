package itm.proyectoharoldo.backend.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIRecommendationRequest {

    private String prompt;

    public AIRecommendationRequest(String prompt){
        this.prompt = prompt;
    }

    public AIRecommendationRequest(){

    }

}
