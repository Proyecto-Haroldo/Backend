package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import itm.proyectoharoldo.backend.Models.DTO.AIClientAnalysesDTO;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analisis")
public class AnalysisController {

    private final ClientAnswerService clientAnswerService;
    private final ClientRepository clientRepository;

    public AnalysisController(ClientAnswerService clientAnswerService,
                              ClientRepository clientRepository) {
        this.clientAnswerService = clientAnswerService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<AIClientAnalysesDTO>> getAllUserSummaries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        List<AIClientAnalysesDTO> result = client.getQuestionnaires().stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    private AIClientAnalysesDTO toDto(ClientQuestionnaire a) {
        return new AIClientAnalysesDTO(
                a.getConteo(),
                a.getTimeWhenSolved(),
                a.getCategory() != null ? a.getCategory().getCategory() : null,
                a.getRecomendacionUsuario(),
                a.getColorSemaforo()
        );
    }
}

