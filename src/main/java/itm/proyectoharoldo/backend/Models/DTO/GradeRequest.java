package itm.proyectoharoldo.backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeRequest {

    /** Adviser comment / feedback for the client (required when grading). */
    private String contenidoRevision;

    /** Optional: override traffic light color (verde, amarillo, rojo). */
    private String colorSemaforo;
}
