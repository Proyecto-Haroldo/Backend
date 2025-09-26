package itm.proyectoharoldo.backend.Models.DTO;

import java.time.LocalDateTime;

public class AIClientAnalysesDTO {
    private Integer conteo;
    private LocalDateTime timestamp;
    private String categoria;
    private String recomendacionUsuario;
    private String colorSemaforo;

    public AIClientAnalysesDTO(Integer conteo, LocalDateTime timestamp, String categoria, String recomendacionUsuario, String colorSemaforo) {
        this.conteo = conteo;
        this.timestamp = timestamp;
        this.categoria = categoria;
        this.recomendacionUsuario = recomendacionUsuario;
        this.colorSemaforo = colorSemaforo;
    }

    public Integer getConteo(){
        return this.conteo;
    }

    public void setConteo(Integer conteo){
        this.conteo = conteo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRecomendacionUsuario() {
        return recomendacionUsuario;
    }

    public void setRecomendacionUsuario(String recomendacionUsuario) {
        this.recomendacionUsuario = recomendacionUsuario;
    }

    public String getColorSemaforo() {
        return colorSemaforo;
    }

    public void setColorSemaforo(String colorSemaforo) {
        this.colorSemaforo = colorSemaforo;
    }

}
