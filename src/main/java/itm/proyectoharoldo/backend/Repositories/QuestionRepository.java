package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.ClientType;
import itm.proyectoharoldo.backend.Models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Optimized queries with JOIN FETCH to avoid N+1 problem
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category " +
           "WHERE q.category = :category")
    List<Question> findByCategoryWithOptions(@Param("category") Category category);
    
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category " +
           "WHERE q.category.category = :categoryName AND q.clientType = :clientType")
    List<Question> findByCategoryAndClientTypeWithOptions(@Param("categoryName") String categoryName, 
                                                          @Param("clientType") ClientType clientType);
    
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category")
    List<Question> findAllWithOptions();
    
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category " +
           "WHERE q.category.category = :categoryName")
    List<Question> findByCategoryNameWithOptions(@Param("categoryName") String categoryName);
    
    // Paginated versions for better performance with large datasets
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category " +
           "WHERE q.category = :category")
    Page<Question> findByCategoryWithOptions(@Param("category") Category category, Pageable pageable);
    
    @Query("SELECT DISTINCT q FROM Question q " +
           "LEFT JOIN FETCH q.options " +
           "LEFT JOIN FETCH q.category " +
           "WHERE q.category.category = :categoryName AND q.clientType = :clientType")
    Page<Question> findByCategoryAndClientTypeWithOptions(@Param("categoryName") String categoryName, 
                                                          @Param("clientType") ClientType clientType, 
                                                          Pageable pageable);
    
    // Legacy methods for backward compatibility
    List<Question> findByCategory(Category category);
    List<Question> findByCategory_CategoryAndClientType(String category, ClientType clientType);
}
