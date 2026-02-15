package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.AnswersOfQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswersOfQuestionnaireRepository extends JpaRepository<AnswersOfQuestionnaire, Long> {

    /**
     * Table answersofquestionnaire only has columns: answerid, question, answertext.
     * There is no analysis FK, so we cannot filter by analysisId. Returns empty until the table has that column.
     */
    default List<QuestionAnswerProjection> findAnswersByAnalysisId(Long analysisId) {
        return List.of();
    }

    /** Projection for native query result (column aliases must match getter names). */
    interface QuestionAnswerProjection {
        long getQuestionid();
        String getQuestiontext();
        String getAnswertext();
    }
}
