package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import itm.proyectoharoldo.backend.Models.DTO.ClientQuestionnaireDTO;
import itm.proyectoharoldo.backend.Models.DTO.QuestionnaireAnswerDTO;
import itm.proyectoharoldo.backend.Repositories.ClientQuestionnaireRepository;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cuestionarios")
@RequiredArgsConstructor
public class QuestionnaireController {

    private final ClientRepository clientRepository;
    private final ClientQuestionnaireRepository clientQuestionnaireRepository;

    @GetMapping
    public List<ClientQuestionnaireDTO> getQuestionnaires(@RequestParam Long clientid) {
        Client client = clientRepository.findById(clientid).get();

        return clientQuestionnaireRepository.findByClient(client).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ClientQuestionnaireDTO mapToDTO(ClientQuestionnaire questionnaire) {
        List<QuestionnaireAnswerDTO> answerDTOs = questionnaire.getAnswers().stream()
                .map(ans -> new QuestionnaireAnswerDTO(
                        ans.getQuestion().getQuestionid(),
                        ans.getQuestion().getQuestion(),
                        ans.getAnswerText()))
                .toList();

        return new ClientQuestionnaireDTO(
                questionnaire.getId(),
                questionnaire.getCategory().getCategory(),
                questionnaire.getTimeWhenSolved(),
                answerDTOs
        );
    }
}

