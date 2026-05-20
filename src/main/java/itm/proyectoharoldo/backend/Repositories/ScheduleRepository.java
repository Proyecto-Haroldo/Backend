package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("""
            SELECT s FROM Schedule s
            WHERE s.advisorId = :advisorId
              AND s.scheduledDate >= :fromDate
              AND s.scheduledDate <= :toDate
            ORDER BY s.scheduledDate ASC, s.meetingTime ASC
            """)
    List<Schedule> findForAdvisorInRange(
            @Param("advisorId") Long advisorId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("""
            SELECT s FROM Schedule s
            WHERE s.clientId = :clientId
              AND s.scheduledDate >= :fromDate
              AND s.scheduledDate <= :toDate
            ORDER BY s.scheduledDate ASC, s.meetingTime ASC
            """)
    List<Schedule> findForClientInRange(
            @Param("clientId") Long clientId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
