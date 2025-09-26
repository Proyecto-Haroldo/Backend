package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientQuestionnaireRepository extends JpaRepository<ClientQuestionnaire, Long> {
    List<ClientQuestionnaire> findByClient(Client client);
    Integer countByClientAndCategory(Client client, Category category);
}
