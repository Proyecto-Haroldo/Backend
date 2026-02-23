package itm.proyectoharoldo.backend.Models.DTO.Analysis;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AIAnalysisResultDTO {

    private String resumenUsuario;
    private String colorSemaforo;
    private String analisisAsesor;

    @Override
    public String toString() {
        return "{" +
                "resumenUsuario='" + resumenUsuario + '\'' +
                ", colorSemaforo='" + colorSemaforo + '\'' +
                ", cnalisisAsesor='" + analisisAsesor + '\'' +
                '}';
    }

}
