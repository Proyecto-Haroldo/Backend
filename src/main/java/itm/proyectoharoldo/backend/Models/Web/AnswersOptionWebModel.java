package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import lombok.Data;

import java.util.List;

/*
According to Johann's model:
export interface QuestionOption {
  id: string; ???????
  text: string;
  keywords?: string[];
}
 */

@Data
public class AnswersOptionWebModel {
    public String id;
    public String text;
    public List<GlossaryWord> keywords;
}
