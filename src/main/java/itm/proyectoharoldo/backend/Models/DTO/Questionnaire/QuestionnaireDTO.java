package itm.proyectoharoldo.backend.Models.DTO.Questionnaire;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDTO {

    private Long id;
    private Long categoryId;
    private Long creatorId;
    private String creatorName;
    private String categoryName;
    private String title;
    
}
