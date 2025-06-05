package itm.proyectoharoldo.backend.Models.Web;

/*
export interface QuestionnaireMetadata {
  category: string;
  clientType: string;
  timestamp: string;
}
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireMetadata {

    String category;
    String clientType;
    String timestamp;

    public QuestionnaireMetadata(String category, String clientType, String timestamp) {
        this.category = category;
        this.clientType = clientType;
        this.timestamp = timestamp;
    }

    public QuestionnaireMetadata() {
    }

}
