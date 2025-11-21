package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;
import itm.proyectoharoldo.backend.Models.DTO.WebAnswersDTO;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/respuestas")
@AllArgsConstructor
public class WebAnswersController {

    private final ClientAnswerService clientAnswerService;
    private final UserRepository userRepository;

    public WebAnswersController(ClientAnswerService clientAnswerService, UserRepository userRepository) {
        this.clientAnswerService = clientAnswerService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map> saveAnswers(@RequestBody WebAnswersDTO request) {

        if (request.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AIAnalysisResultDTO aiAnalysisResultDTO =
                clientAnswerService.saveQuestionnaireResult(
                        request.getQuestionnaireData(),
                        request.getUserId()
                );

        Map<String, String> response = Map.of(
                "resumenUsuario", aiAnalysisResultDTO.getResumenUsuario(),
                "colorSemaforo", aiAnalysisResultDTO.getColorSemaforo().toUpperCase()
        );

        return ResponseEntity.ok(response);
    }
}
