package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
According to Johann's model:
export interface QuestionOption {
  id: string; ???????
  text: string;
  keywords?: string[];
}
 */

public class AnswersOptionWebModel {
    private Long id;
    private String text;

    public AnswersOptionWebModel(Long id, String text, List<GlossaryWord> keywords) {
        this.id = id;
        this.text = text;
    }

    public AnswersOptionWebModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
