package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.AnswersOfQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswersOfQuestionnaireRepository extends JpaRepository<AnswersOfQuestionnaire, Long> {

    @Query("""
        SELECT a.question.questionid AS questionid,
               a.question.question AS questiontext,
               a.answerText AS answertext
        FROM AnswersOfQuestionnaire a
        WHERE a.analysis.analysisId = :analysisId
        ORDER BY a.question.questionid
        """)
    List<QuestionAnswerProjection> findByAnalysisId(@Param("analysisId") Long analysisId);

    public record QuestionAnswerProjection(Long questionId, String questionText, String answerText) {};

}