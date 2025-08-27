package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.AiClientAnalysis;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiClientAnalysisRepository extends JpaRepository<AiClientAnalysis, Long> {
    AiClientAnalysis findByQuestionnaire(ClientQuestionnaire questionnaire);
}
