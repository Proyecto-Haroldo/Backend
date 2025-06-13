package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.AiClientAnalysis;
import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.ClientQuestionnaire;
import itm.proyectoharoldo.backend.Models.DTO.AiClientAnalysisDTO;
import itm.proyectoharoldo.backend.Repositories.AiClientAnalysisRepository;
import itm.proyectoharoldo.backend.Repositories.ClientQuestionnaireRepository;
import itm.proyectoharoldo.backend.Repositories.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class AiAdviceController {

    private final AiClientAnalysisRepository aiClientAnalysisRepository;
    private final ClientRepository clientRepository;
    private final ClientQuestionnaireRepository clientQuestionnaireRepository;

    @GetMapping
    public List<AiClientAnalysisDTO> getAnalysisByClientId(@RequestParam Long clientid) {
        Client client = clientRepository.findById(clientid).get();

        List<ClientQuestionnaire> questionnaires = clientQuestionnaireRepository.findByClient(client);

        return questionnaires.stream()
                .map(q -> {
                    AiClientAnalysis analysis = aiClientAnalysisRepository.findByQuestionnaire(q);
                    return new AiClientAnalysisDTO(
                            q.getId(),
                            analysis.getTimestamp(),
                            analysis.getRecommendation()
                    );
                })
                .toList();
    }
}

