package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> saveAnswers(@RequestBody QuestionnaireResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();
        return clientAnswerService.saveQuestionnaireResult(result, client.getClientId());
    }
}

