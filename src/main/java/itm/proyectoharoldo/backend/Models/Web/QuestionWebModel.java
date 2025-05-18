package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import lombok.Data;

import java.util.List;

/*
According to Johann's model:
export interface Question {
    id: string; Bruh
    title: string;
    description: string;
    type: QuestionType;
    options?: QuestionOption[];
    keywords: Keyword[];
}
*/

@Data
public class QuestionWebModel {
    public Long id;
    public String category;
    public String question;
    public QuestionType type;
    public List<MultipleOptionQuestionAnswer> possibleAnswers;
    public List<GlossaryWord> glossaryWords;
}
