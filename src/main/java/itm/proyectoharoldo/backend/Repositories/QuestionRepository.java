package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
