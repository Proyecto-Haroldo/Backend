package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.User;
import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;
import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/respuestas")
public class WebAnswersController {

    private final ClientAnswerService clientAnswerService;
    private final UserRepository userRepository;

    public WebAnswersController(ClientAnswerService clientAnswerService, UserRepository userRepository) {
        this.clientAnswerService = clientAnswerService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map> saveAnswers(@RequestBody QuestionnaireResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        AIAnalysisResultDTO aiAnalysisResultDTO = clientAnswerService.saveQuestionnaireResult(result, user.getUserId());

        Map<String, String> response = Map.of(
                "resumenUsuario", aiAnalysisResultDTO.getResumenUsuario(),
                "colorSemaforo", aiAnalysisResultDTO.getColorSemaforo().toUpperCase()
        );

        return ResponseEntity.ok(response);
    }

}

