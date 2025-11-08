package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    List<Questionnaire> findByCategory(Category category);
}