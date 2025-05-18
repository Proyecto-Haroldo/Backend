package itm.proyectoharoldo.backend.Models;

import lombok.Data;

/* According to Johann's model:
export interface Keyword {
  title: string;
  description: string;
}
 */

@Data
public class GlossaryWord {
    public String title;
    public String description;
}
