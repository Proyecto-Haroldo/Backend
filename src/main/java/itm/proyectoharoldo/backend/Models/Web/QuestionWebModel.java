package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.QuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionWebModel {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String question;
    private QuestionType questionType;
    private Long questionnaireId;
    private List<AnswersOptionWebModel> options;
    private List<GlossaryWord> keywords;
}
