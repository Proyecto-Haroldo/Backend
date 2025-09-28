package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientQuestionnaireRepository extends JpaRepository<ClientQuestionnaire, Long> {
    List<ClientQuestionnaire> findByClient(Client client);
    Integer countByClientAndCategory(Client client, Category category);
    
    @Query("SELECT DISTINCT cq FROM ClientQuestionnaire cq " +
           "LEFT JOIN FETCH cq.category " +
           "WHERE cq.client = :client " +
           "ORDER BY cq.timeWhenSolved DESC")
    List<ClientQuestionnaire> findByClientOrderByTimeWhenSolvedDesc(@Param("client") Client client);

    @Query("SELECT DISTINCT cq FROM ClientQuestionnaire cq " +
           "LEFT JOIN FETCH cq.category " +
           "WHERE cq.client = :client AND cq.category = :category " +
           "ORDER BY cq.timeWhenSolved DESC")
    List<ClientQuestionnaire> findByClientAndCategoryOrderByTimeWhenSolvedDesc(
            @Param("client") Client client, 
            @Param("category") Category category);
}
