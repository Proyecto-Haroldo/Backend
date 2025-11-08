package itm.proyectoharoldo.backend.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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
