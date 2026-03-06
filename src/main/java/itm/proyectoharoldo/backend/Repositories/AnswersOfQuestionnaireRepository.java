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
    List<QuestionAnswerProjection> findAnswersByAnalysisId(@Param("analysisId") Long analysisId);

    /** Projection for query result (aliases match getter names). */
    interface QuestionAnswerProjection {
        long getQuestionid();
        String getQuestiontext();
        String getAnswertext();
    }
}
