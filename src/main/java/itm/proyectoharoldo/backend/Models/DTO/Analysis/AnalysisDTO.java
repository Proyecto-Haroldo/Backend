package itm.proyectoharoldo.backend.Models.DTO.Analysis;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {

    private Long analysisId;
    private String asesorName;
    private String clientName;
    private String status;
    private String recomendacionInicial;
    private String colorSemaforo;
    private String contenidoRevision;
    private LocalDateTime timeWhenSolved;
    private LocalDateTime timeWhenChecked;
    private Integer conteo;
    private String categoria;

}
