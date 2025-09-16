package itm.proyectoharoldo.backend.Models.DTO;

public class AIAnalysisResultDTO {

    private String resumenUsuario;
    private String colorSemaforo;
    private String analisisAsesor;

    public AIAnalysisResultDTO(){

    }

    public String getResumenUsuario() {
        return resumenUsuario;
    }

    public void setResumenUsuario(String resumenUsuario) {
        this.resumenUsuario = resumenUsuario;
    }

    public String getColorSemaforo() {
        return colorSemaforo;
    }

    public void setColorSemaforo(String colorSemaforo) {
        this.colorSemaforo = colorSemaforo;
    }

    public String getAnalisisAsesor() {
        return analisisAsesor;
    }

    public void setAnalisisAsesor(String analisisAsesor) {
        this.analisisAsesor = analisisAsesor;
    }

    @Override
    public String toString() {
        return "{" +
                "resumenUsuario='" + resumenUsuario + '\'' +
                ", colorSemaforo='" + colorSemaforo + '\'' +
                ", cnalisisAsesor='" + analisisAsesor + '\'' +
                '}';
    }

}
