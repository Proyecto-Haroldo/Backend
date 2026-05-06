package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    List<Questionnaire> findByCategory(Category category);

    List<Questionnaire> findByCreator(User creator);

    Questionnaire findByTitle(String title);

    @Query("SELECT q FROM Questionnaire q ORDER BY q.id DESC")
    List<Questionnaire> findAllOrdered();

    @Query("SELECT q FROM Questionnaire q WHERE q.category = :category ORDER BY q.id DESC")
    List<Questionnaire> findByCategoryOrdered(Category category);
}
