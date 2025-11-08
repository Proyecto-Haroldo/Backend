package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestionWebModel {
    private Long id;
    private String category;
    private String question;
    private QuestionType type;
    private List<AnswersOptionWebModel> options;
    private List<GlossaryWord> keywords;
}
