package itm.proyectoharoldo.backend.Models.DTO;

import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class WebAnswersDTO {
    private Long userId;
    private QuestionnaireResult questionnaireData;
}
