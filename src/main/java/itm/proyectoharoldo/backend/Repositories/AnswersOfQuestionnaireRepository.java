package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.AnswersOfQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersOfQuestionnaireRepository extends JpaRepository<AnswersOfQuestionnaire, Long> {

    List<AnswersOfQuestionnaire> findByAnalysis_AnalysisIdOrderByQuestion_Questionid(Long analysisId);
}
