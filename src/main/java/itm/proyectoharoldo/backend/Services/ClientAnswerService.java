package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.internalServerError;

@Service
public class ClientAnswerService {

    private final ClientRepository clientRepository;
    private final QuestionRepository questionRepository;
    private final ClientQuestionnaireRepository clientQuestionnaireRepository;
    private final AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository;
    private final AiClientAnalysisRepository aiClientAnalysisRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    public ClientAnswerService(
            ClientRepository clientRepository,
            QuestionRepository questionRepository,
            ClientQuestionnaireRepository clientQuestionnaireRepository,
            AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository,
            AiClientAnalysisRepository aiClientAnalysisRepository,
            CategoryRepository categoryRepository,
            AIService aiService
    ) {
        this.clientRepository = clientRepository;
        this.questionRepository = questionRepository;
        this.clientQuestionnaireRepository = clientQuestionnaireRepository;
        this.answersOfQuestionnaireRepository = answersOfQuestionnaireRepository;
        this.aiClientAnalysisRepository = aiClientAnalysisRepository;
        this.categoryRepository = categoryRepository;
        this.aiService = aiService;
    }

    @Transactional
    public ResponseEntity<String> saveQuestionnaireResult(QuestionnaireResult result, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        
        // Parse timestamp with timezone support
        LocalDateTime timestamp;
        try {
            // Try to parse as ISO 8601 with timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(result.getMetadata().getTimestamp());
            timestamp = zonedDateTime.toLocalDateTime();
        } catch (Exception e) {
            // Fallback to LocalDateTime if no timezone
            timestamp = LocalDateTime.parse(result.getMetadata().getTimestamp());
        }
        
        String categoryName = result.getMetadata().getCategory();
        Category category = categoryRepository.findByCategory(categoryName).orElseThrow();

        ClientQuestionnaire questionnaire = new ClientQuestionnaire();
        questionnaire.setClient(client);
        questionnaire.setCategory(category);
        questionnaire.setTimeWhenSolved(timestamp);
        clientQuestionnaireRepository.save(questionnaire);

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Cliente: ").append(result.getMetadata().getClientType()).append("\n\n");
        promptBuilder.append("Categoría: ").append(categoryName).append("\n\n");

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionRepository.findById((long) qa.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", qa.getAnswer());

            AnswersOfQuestionnaire answer = new AnswersOfQuestionnaire();
            answer.setQuestionnaire(questionnaire);
            answer.setQuestion(question);
            answer.setAnswerText(answerText);
            answersOfQuestionnaireRepository.save(answer);

            promptBuilder.append("Pregunta: ").append(question.getQuestion()).append("\n");
            promptBuilder.append("Respuesta: ").append(answerText).append("\n\n");
        }

        String prompt = promptBuilder.toString();

        ResponseEntity<Map> recomendationResponse = aiService.getAiRecommendation(prompt);
        ResponseEntity<String> responseEntity = processRecomendation(recomendationResponse);

        AiClientAnalysis analysis = new AiClientAnalysis();
        analysis.setQuestionnaire(questionnaire);
        analysis.setCategory(category);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            analysis.setRecommendation(responseEntity.getBody());
        } else {
            analysis.setRecommendation("Error al realizar recomendación, inténtelo nuevamente.");
        }
        analysis.setTimestamp(LocalDateTime.now());
        aiClientAnalysisRepository.save(analysis);


        return responseEntity;
    }

    private ResponseEntity<String> processRecomendation(ResponseEntity<Map> recomendationResponse){
        if(recomendationResponse.getStatusCode().is5xxServerError()){
            return ResponseEntity.internalServerError().body(
                    "No se pudo obtener la recomendación de IA en este momento. " +
                            Objects.requireNonNull(recomendationResponse.getBody()).get("response").toString()
            );
        } else {
            return ResponseEntity.ok(
                    Objects.requireNonNull(recomendationResponse.getBody()).get("response").toString()
            );
        }
    }
}
