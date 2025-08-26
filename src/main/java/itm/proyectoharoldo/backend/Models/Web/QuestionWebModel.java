package itm.proyectoharoldo.backend.Models.Web;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.QuestionType;

import java.util.List;

public class QuestionWebModel {
    private Long id;
    private String title;
    private QuestionType type;
    private List<AnswersOptionWebModel> options;
    private List<GlossaryWord> keywords;

    public QuestionWebModel(){

    }

    // Constructor privado para forzar el uso del builder
    private QuestionWebModel(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.type = builder.type;
        this.options = builder.options;
        this.keywords = builder.keywords;
    }

    // Builder estático
    public static class Builder {
        private Long id;
        private String title;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public List<AnswersOptionWebModel> getOptions() {
        return options;
    }

    public void setOptions(List<AnswersOptionWebModel> options) {
        this.options = options;
    }

    public List<GlossaryWord> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<GlossaryWord> keywords) {
        this.keywords = keywords;
    }
}
