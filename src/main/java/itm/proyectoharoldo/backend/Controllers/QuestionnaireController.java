package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.DTO.ClientQuestionnaireDTO;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import itm.proyectoharoldo.backend.Models.QuestionnaireState;
import itm.proyectoharoldo.backend.Repositories.ClientQuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questionnaires")
public class QuestionnaireController {

    @Autowired
    private ClientQuestionnaireRepository clientQuestionnaireRepository;

    private ClientQuestionnaireDTO toDto(ClientQuestionnaire q) {
        return new ClientQuestionnaireDTO(
                q.getId(),
                q.getCategory() != null ? q.getCategory().getCategory() : "Sin categoría",
                q.getClient() != null ? q.getClient().getLegalName() : "Sin cliente",
                q.getTimeWhenSolved(),
                q.getState() != null ? q.getState().name() : "NULL",
                q.getRecomendacionUsuario(),
                q.getColorSemaforo(),
                q.getAnalisisAsesor(),
                q.getConteo()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientQuestionnaireDTO>> getAllQuestionnaires() {
        List<ClientQuestionnaireDTO> questionnaires = clientQuestionnaireRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ClientQuestionnaireDTO>> getPendingQuestionnaires() {
        List<ClientQuestionnaireDTO> questionnaires = clientQuestionnaireRepository.findByState(QuestionnaireState.pending)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }
}
