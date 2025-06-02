package itm.proyectoharoldo.backend.Models.Web;

/*
export interface QuestionnaireMetadata {
  category: string;
  clientType: string;
  timestamp: string;
}
 */

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
