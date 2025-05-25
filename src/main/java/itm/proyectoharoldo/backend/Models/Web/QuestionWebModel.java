package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import itm.proyectoharoldo.backend.Models.QuestionType;

import java.util.List;

public class QuestionWebModel {
    private Long id;
    private String title; //Category of the question
    private String description; //Text of the question
    private QuestionType type;
    private List<AnswersOptionWebModel> options;
    private List<GlossaryWord> keywords;

    public QuestionWebModel(){

    }

    // Constructor privado para forzar el uso del builder
    private QuestionWebModel(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.type = builder.type;
        this.options = builder.options;
        this.keywords = builder.keywords;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestionType getType() { return type; }
    public List<AnswersOptionWebModel> getOptions() { return options; }
    public List<GlossaryWord> getKeywords() { return keywords; }

    // Builder estático
    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private QuestionType type;
        private List<AnswersOptionWebModel> options;
        private List<GlossaryWord> keywords;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(QuestionType type) {
            this.type = type;
            return this;
        }

        public Builder options(List<AnswersOptionWebModel> options) {
            this.options = options;
            return this;
        }

        public Builder keywords(List<GlossaryWord> keywords) {
            this.keywords = keywords;
            return this;
        }

        public QuestionWebModel build() {
            return new QuestionWebModel(this);
        }
    }
}
