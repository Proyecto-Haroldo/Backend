package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Questionnaire;
import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Models.Analysis;
import itm.proyectoharoldo.backend.Models.AnalysisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
   List<Analysis> findByUsuarioResponde(User user);

   Integer countByUsuarioRespondeAndQuestionnaire(User user, Questionnaire questionnaire);

   List<Analysis> findByStatus(AnalysisStatus status);

   @Query(value = """
         SELECT COUNT(*) + 1
         FROM analyses a
         WHERE a.usuarioresponde = :usuarioId
         AND a.questionnaire = :questionnaireId
         """, nativeQuery = true)
   Integer findNextAnalysisCount(@Param("usuarioId") Long userId,
         @Param("questionnaireId") Long questionnaireId);

   @Query("""
         SELECT DISTINCT an
         FROM Analysis an
         LEFT JOIN FETCH an.questionnaire
         WHERE an.usuarioResponde = :user
         ORDER BY an.timeWhenSolved DESC
         """)
   List<Analysis> findByUsuarioRespondeOrderByTimeWhenSolvedDesc(@Param("user") User user);

   @Query("""
         SELECT DISTINCT an
         FROM Analysis an
         LEFT JOIN FETCH an.questionnaire
         WHERE an.asesor = :asesor
         ORDER BY an.timeWhenChecked DESC
         """)
   List<Analysis> findByAsesorOrderByTimeWhenCheckedDesc(@Param("asesor") User asesor);

   @Query("""
         SELECT DISTINCT an
         FROM Analysis an
         LEFT JOIN FETCH an.questionnaire
         WHERE an.usuarioResponde = :user
           AND an.questionnaire = :questionnaire
         ORDER BY an.timeWhenSolved DESC
         """)
   List<Analysis> findByUsuarioRespondeAndQuestionnaireOrderByTimeWhenSolvedDesc(
         @Param("user") User user,
         @Param("questionnaire") Questionnaire questionnaire);

}
