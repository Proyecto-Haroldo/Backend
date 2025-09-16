package itm.proyectoharoldo.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;
import itm.proyectoharoldo.backend.Models.DTO.AiClientAnalysisDTO;
import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import itm.proyectoharoldo.backend.Utility.AIAnalysisParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/respuestas")
public class WebAnswersController {

    private final ClientAnswerService clientAnswerService;
    private final ClientRepository clientRepository;

    public WebAnswersController(ClientAnswerService clientAnswerService, ClientRepository clientRepository) {
        this.clientAnswerService = clientAnswerService;
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public ResponseEntity<Map> saveAnswers(@RequestBody QuestionnaireResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();
        AIAnalysisResultDTO aiAnalysisResultDTO = new AIAnalysisParser().parseResponseToAnalysis(clientAnswerService.saveQuestionnaireResult(result, client.getClientId()));

        Map<String, String> response = Map.of(
                "resumenUsuario", aiAnalysisResultDTO.getResumenUsuario(),
                "colorSemaforo", aiAnalysisResultDTO.getColorSemaforo()
        );

        return ResponseEntity.ok(response);
    }
}

