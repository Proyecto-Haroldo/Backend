package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/respuestas")
public class WebAnswersController {

    private final ClientAnswerService clientAnswerService;

    public WebAnswersController(ClientAnswerService clientAnswerService) {
        this.clientAnswerService = clientAnswerService;
    }

    @PostMapping
    public ResponseEntity<String> saveAnswers(@RequestBody QuestionnaireResult result) {
        clientAnswerService.saveQuestionnaireResult(result);
        return ResponseEntity.ok("Listo calisto");
    }
}

