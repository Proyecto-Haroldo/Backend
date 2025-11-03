package itm.proyectoharoldo.backend.Models.DTO;

import java.time.LocalDateTime;

public class ClientQuestionnaireDTO {

    private Long id;
    private String categoryName;
    private String clientName;
    private LocalDateTime timeWhenSolved;
    private String state;
    private String recomendacionUsuario;
    private String colorSemaforo;
    private String analisisAsesor;
    private Integer conteo;

    public ClientQuestionnaireDTO(Long id, String categoryName, String clientName, LocalDateTime timeWhenSolved,
                                  String state, String recomendacionUsuario, String colorSemaforo,
                                  String analisisAsesor, Integer conteo) {
        this.id = id;
        this.categoryName = categoryName;
        this.clientName = clientName;
        this.timeWhenSolved = timeWhenSolved;
        this.state = state;
        this.recomendacionUsuario = recomendacionUsuario;
        this.colorSemaforo = colorSemaforo;
        this.analisisAsesor = analisisAsesor;
        this.conteo = conteo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public LocalDateTime getTimeWhenSolved() { return timeWhenSolved; }
    public void setTimeWhenSolved(LocalDateTime timeWhenSolved) { this.timeWhenSolved = timeWhenSolved; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getRecomendacionUsuario() { return recomendacionUsuario; }
    public void setRecomendacionUsuario(String recomendacionUsuario) { this.recomendacionUsuario = recomendacionUsuario; }

    public String getColorSemaforo() { return colorSemaforo; }
    public void setColorSemaforo(String colorSemaforo) { this.colorSemaforo = colorSemaforo; }

    public String getAnalisisAsesor() { return analisisAsesor; }
    public void setAnalisisAsesor(String analisisAsesor) { this.analisisAsesor = analisisAsesor; }

    public Integer getConteo() { return conteo; }
    public void setConteo(Integer conteo) { this.conteo = conteo; }
}
