package itm.proyectoharoldo.backend.Models;

import lombok.*;

/* According to Johann's model:
export interface Keyword {
  title: string;
  description: string;
}
 */

public class GlossaryWord {
    public String title;
    public String description;

    public GlossaryWord(){

    }

    public GlossaryWord(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
