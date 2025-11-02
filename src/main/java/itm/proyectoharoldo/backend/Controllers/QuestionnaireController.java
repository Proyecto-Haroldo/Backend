package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.ClientQuestionnaireDTO;
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

    @GetMapping("/proofread")
    public ResponseEntity<List<ClientQuestionnaireDTO>> getProofreadQuestionnaires() {
        List<ClientQuestionnaireDTO> questionnaires = clientQuestionnaireRepository.findByState(QuestionnaireState.proofread)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientQuestionnaireDTO> updateQuestionnaire(@PathVariable Long id, @RequestBody ClientQuestionnaireDTO questionnaireDTO) {
        ClientQuestionnaire questionnaire = clientQuestionnaireRepository.findById(id).orElseThrow(() -> new RuntimeException("Questionnaire not found"));
        questionnaire.setState(QuestionnaireState.valueOf(questionnaireDTO.getState()));
        questionnaire.setRecomendacionUsuario(questionnaireDTO.getRecomendacionUsuario());
        questionnaire.setAnalisisAsesor(questionnaireDTO.getAnalisisAsesor());
        clientQuestionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(toDto(questionnaire));
    }

    @PutMapping("/setproofread/{id}")
    public ResponseEntity<ClientQuestionnaireDTO> proofreadQuestionnaire(@PathVariable Long id) {
        ClientQuestionnaire questionnaire = clientQuestionnaireRepository.findById(id).orElseThrow(() -> new RuntimeException("Questionnaire not found"));
        questionnaire.setState(QuestionnaireState.proofread);
        clientQuestionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(toDto(questionnaire));
    }
}
