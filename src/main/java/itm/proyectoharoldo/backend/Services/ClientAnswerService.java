package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.QuestionnaireAnswer;
import itm.proyectoharoldo.backend.Models.Web.QuestionnaireResult;
import itm.proyectoharoldo.backend.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ClientAnswerService {

    private final ClientRepository clientRepository;
    private final QuestionRepository questionRepository;
    private final ClientAnswerRepository clientAnswerRepository;
    private final AiClientAnalysisRepository aiClientAnalysisRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    public ClientAnswerService(
            ClientRepository clientRepository,
            QuestionRepository questionRepository,
            ClientAnswerRepository clientAnswerRepository,
            AiClientAnalysisRepository aiClientAnalysisRepository,
            CategoryRepository categoryRepository,
            AIService aiService
    ) {
        this.clientRepository = clientRepository;
        this.questionRepository = questionRepository;
        this.clientAnswerRepository = clientAnswerRepository;
        this.aiClientAnalysisRepository = aiClientAnalysisRepository;
        this.categoryRepository = categoryRepository;
        this.aiService = aiService;
    }

    @Transactional
    public void saveQuestionnaireResult(QuestionnaireResult result) {
        Client client = clientRepository.findById(1L).get();

        LocalDateTime timestamp = LocalDateTime.parse(result.getMetadata().getTimestamp());

        String categoryName = result.getMetadata().getCategory();
        Category category = categoryRepository.findByCategory(categoryName).get();

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Categoría: ").append(categoryName).append("\n\n");

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionRepository.findById((long) qa.getQuestionId()).get();

            String answerText = String.join(" | ", qa.getAnswer());

            ClientAnswer clientAnswer = new ClientAnswer();
            clientAnswer.setClient(client);
            clientAnswer.setQuestion(question);
            clientAnswer.setAnswerText(answerText);
            clientAnswer.setTimestampWhenAnswered(timestamp);
            clientAnswerRepository.save(clientAnswer);

            promptBuilder.append("Pregunta: ").append(question.getQuestion()).append("\n");
            promptBuilder.append("Respuesta: ").append(answerText).append("\n\n");
        }

        String prompt = promptBuilder.toString();
        String recomendacion = aiService.getRecommendationAsText(prompt);

        // Guardar análisis en BD
        AiClientAnalysis analysis = new AiClientAnalysis();
        analysis.setClient(client);
        analysis.setCategory(category);
        analysis.setRecommendation(recomendacion);
        analysis.setTimestamp(LocalDateTime.now());
        aiClientAnalysisRepository.save(analysis);
    }

}
