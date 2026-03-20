package itm.proyectoharoldo.backend.Models.Web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireMetadata {

    String category;
    String clientType;
    String timestamp;
    String title;

}
