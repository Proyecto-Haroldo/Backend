package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.ClientType;
import itm.proyectoharoldo.backend.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

       @Query("""
                     SELECT DISTINCT q
                     FROM Question q
                     LEFT JOIN FETCH q.options
                     LEFT JOIN FETCH q.questionnaire qu
                     LEFT JOIN FETCH qu.category c
                     WHERE c = :category
                     """)
       List<Question> findByCategoryWithOptions(@Param("category") Category category);

       @Query("""
                     SELECT DISTINCT q
                     FROM Question q
                     LEFT JOIN FETCH q.options
                     LEFT JOIN FETCH q.questionnaire qu
                     LEFT JOIN FETCH qu.category c
                     WHERE c.category = :categoryName
                     AND q.clientType = :clientType
                     """)
       List<Question> findByCategoryAndClientTypeWithOptions(@Param("categoryName") String categoryName,
                     @Param("clientType") ClientType clientType);

       @Query("""
                     SELECT DISTINCT q
                     FROM Question q
                     LEFT JOIN FETCH q.options
                     LEFT JOIN FETCH q.questionnaire qu
                     LEFT JOIN FETCH qu.category
                     LEFT JOIN FETCH qu.creator
                     """)
       List<Question> findAllWithOptions();

       @Query("""
                     SELECT DISTINCT q
                     FROM Question q
                     LEFT JOIN FETCH q.options
                     LEFT JOIN FETCH q.questionnaire qu
                     LEFT JOIN FETCH qu.category c
                     WHERE c.category = :categoryName
                     """)
       List<Question> findByCategoryNameWithOptions(@Param("categoryName") String categoryName);

       @Query("""
                     SELECT DISTINCT q
                     FROM Question q
                     LEFT JOIN FETCH q.options
                     LEFT JOIN FETCH q.questionnaire qu
                     LEFT JOIN FETCH qu.category
                     WHERE qu.id = :questionnaireId
                     """)
       List<Question> findByQuestionnaireWithOptions(@Param("questionnaireId") Long questionnaireId);

       List<Question> findByQuestionnaire_Category(Category category);

       List<Question> findByQuestionnaire_Category_CategoryAndClientType(String category, ClientType clientType);
}
