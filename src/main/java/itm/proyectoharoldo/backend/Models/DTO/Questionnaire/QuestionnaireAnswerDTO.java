package itm.proyectoharoldo.backend.Models.DTO.Questionnaire;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireAnswerDTO {

    private Long questionId;
    private String questionText;
    private String answer;
    
}

