package itm.proyectoharoldo.backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionnaireDTO {

    private Long id;
    private Long categoryId;
    private Long creatorId;
    private String creatorName;
    private String categoryName;
}
