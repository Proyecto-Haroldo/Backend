package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.ClientType;
import itm.proyectoharoldo.backend.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(Category category);
    List<Question> findByCategory_CategoryAndClientType(String category, ClientType clientType);
}
