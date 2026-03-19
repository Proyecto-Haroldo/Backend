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

    private LocalDateTime timeWhenSolved;
    private LocalDateTime timeWhenChecked;

    private String status;
    private String analisisIA;
    private String colorSemaforo;
    private String resumenIA;
    private String comentarioAsesor;
    private Integer conteo;
    
    private String categoryName;
    private String questionnaireTitle;
}
