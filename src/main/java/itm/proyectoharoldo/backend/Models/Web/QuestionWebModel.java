package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.Enums.QuestionType;
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
    private Long parentQuestionId;
    private String parentAnswerTrigger;
    private Integer displayOrder;
    private String section;
    private List<QuestionWebModel> children;
}
