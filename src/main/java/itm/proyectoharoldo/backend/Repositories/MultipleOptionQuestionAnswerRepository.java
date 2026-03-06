package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleOptionQuestionAnswerRepository extends JpaRepository<MultipleOptionQuestionAnswer, Long> {
    List<MultipleOptionQuestionAnswer> findByQuestion_questionid(Long questionid);
}
