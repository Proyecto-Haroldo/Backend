package itm.proyectoharoldo.backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionnaireDTO {
    private Long id;
    private String categoryName;  // Nombre de la categoría
    private Long categoryId;      // ID de la categoría
    private String creatorName;   // Nombre del creador (user)
    private Long creatorId;       // ID del creador
    private List<Question> questions; // Preguntas asociadas
}
