package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultipleOptionQuestionAnswerRepository extends JpaRepository<MultipleOptionQuestionAnswer, Long> {
    List<MultipleOptionQuestionAnswer> findByQuestion_questionid(Long questionid);
}
