package itm.proyectoharoldo.backend.Models.DTO;

import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebAnswersDTO {

    private Long userId;
    private QuestionnaireResult questionnaireData;
    
}
