package itm.proyectoharoldo.backend.Models.DTO.Questionnaire;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerDTO {

    private Long questionId;
    private String questionText;
    private String answerText;
    
}
