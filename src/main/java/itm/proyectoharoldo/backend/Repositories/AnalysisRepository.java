package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Enums.AnalysisStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

        // Buscar todos los análisis de un usuario que responde
        List<Analysis> findByUsuarioResponde(User usuarioResponde);

        // Contar cuántos análisis tiene un usuario para un cuestionario específico
        Integer countByUsuarioRespondeAndQuestionnaire(User usuarioResponde, Questionnaire questionnaire);

        // Buscar análisis por estado
        List<Analysis> findByStatus(AnalysisStatus status);

        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.asesor
                        LEFT JOIN FETCH a.usuarioResponde
                        LEFT JOIN FETCH a.questionnaire q
                        LEFT JOIN FETCH q.category
                        """
                )
        List<Analysis> findAllWithDetails();

        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.asesor
                        LEFT JOIN FETCH a.usuarioResponde
                        LEFT JOIN FETCH a.questionnaire q
                        LEFT JOIN FETCH q.category
                        WHERE a.analysisId = :id
                        """

                )
        Optional<Analysis> findByIdWithDetails(@Param("id") Long id);

        // Próximo conteo de análisis para un usuario y cuestionario (consulta nativa)
        @Query("""
                SELECT COUNT(a) + 1
                FROM Analysis a
                WHERE a.usuarioResponde.userId = :usuarioId
                AND a.questionnaire.id = :questionnaireId
                """)
        Integer findNextAnalysisCount(@Param("usuarioId") Long usuarioId,
        @Param("questionnaireId") Long questionnaireId);

        // Obtener análisis de un usuario con cuestionario cargado, orden descendente
        // por resolución
        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.usuarioResponde ur
                        LEFT JOIN FETCH a.asesor
                        LEFT JOIN FETCH a.questionnaire q
                        LEFT JOIN FETCH q.category
                        WHERE a.usuarioResponde = :usuarioResponde
                        ORDER BY a.timeWhenSolved DESC
                        """)
        List<Analysis> findByUsuarioRespondeOrderByTimeWhenSolvedDesc(@Param("usuarioResponde") User usuarioResponde);

        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.questionnaire q
                        LEFT JOIN FETCH q.category
                        LEFT JOIN FETCH a.usuarioResponde
                        LEFT JOIN FETCH a.asesor
                        WHERE a.usuarioResponde.userId = :userId
                        ORDER BY a.timeWhenSolved DESC
                        """)
        List<Analysis> findByUsuarioRespondeUserIdOrderByTimeWhenSolvedDesc(@Param("userId") Long userId);

        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.questionnaire q
                        LEFT JOIN FETCH q.category c
                        LEFT JOIN FETCH a.usuarioResponde
                        LEFT JOIN FETCH a.asesor
                        WHERE a.usuarioResponde.userId = :userId
                          AND c.title = :title
                        ORDER BY a.timeWhenSolved DESC
                        """)
        List<Analysis> findByUsuarioRespondeUserIdAndCategoryOrderByTimeWhenSolvedDesc(
                        @Param("userId") Long userId,
                        @Param("title") String title);

        @Query("""
                SELECT DISTINCT a
                FROM Analysis a
                LEFT JOIN FETCH a.asesor
                LEFT JOIN FETCH a.usuarioResponde
                LEFT JOIN FETCH a.questionnaire q
                LEFT JOIN FETCH q.category
                WHERE a.asesor.userId = :adviserId
                ORDER BY a.timeWhenChecked DESC
                """)
        List<Analysis> findByAsesorIdOrderByTimeWhenCheckedDesc(@Param("adviserId") Long adviserId);

        // Obtener análisis de un usuario y un cuestionario específico, orden
        // descendente por resolución
        @Query("""
                        SELECT DISTINCT a
                        FROM Analysis a
                        LEFT JOIN FETCH a.questionnaire
                        WHERE a.usuarioResponde = :usuarioResponde
                          AND a.questionnaire = :questionnaire
                        ORDER BY a.timeWhenSolved DESC
                        """)
        List<Analysis> findByUsuarioRespondeAndQuestionnaireOrderByTimeWhenSolvedDesc(
                        @Param("usuarioResponde") User usuarioResponde,
                        @Param("questionnaire") Questionnaire questionnaire);

}
