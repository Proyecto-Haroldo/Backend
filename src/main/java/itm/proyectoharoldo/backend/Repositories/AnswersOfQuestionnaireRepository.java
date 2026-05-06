package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.AnswersOfQuestionnaire;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswersOfQuestionnaireRepository extends JpaRepository<AnswersOfQuestionnaire, Long> {

    @Query("""
        SELECT new itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO(
               a.question.questionid,
               a.question.question,
               a.answerText)
        FROM AnswersOfQuestionnaire a
        WHERE a.analysis.analysisId = :analysisId
        ORDER BY a.question.questionid
        """)
    List<QuestionAnswerDTO> findByAnalysisId(@Param("analysisId") Long analysisId);

}