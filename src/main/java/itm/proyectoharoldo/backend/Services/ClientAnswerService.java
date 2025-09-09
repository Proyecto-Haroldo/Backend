package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

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

        Category category = categoryRepository.findByCategory(result.getMetadata().getCategory()).orElseThrow();
        ClientQuestionnaire questionnaire = saveNewQuestionnaire(result, clientId);
        saveAnswersOfQuestionnaire(result, questionnaire);
        String AiPrompt = buildAiPrompt(result);

        ResponseEntity<Map> recomendationResponse = aiService.getAiRecommendation(AiPrompt);
        ResponseEntity<String> responseEntity = processRecomendation(recomendationResponse);

        saveAnalysis(category, questionnaire, responseEntity);

        return responseEntity;
    }

    private ResponseEntity<String> processRecomendation(ResponseEntity<Map> recomendationResponse) {
        if (recomendationResponse.getStatusCode().is5xxServerError()) {
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

    private LocalDateTime dateTimeParser(String timestamp) {
        try {
            return ZonedDateTime.parse(timestamp).toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.parse(timestamp);
        }
    }

    private ClientQuestionnaire saveNewQuestionnaire(QuestionnaireResult result, Long clientId) {

        Client client = clientRepository.findById(clientId).orElseThrow();
        Category category = categoryRepository.findByCategory(result.getMetadata().getCategory()).orElseThrow();

        ClientQuestionnaire questionnaire = new ClientQuestionnaire();
        questionnaire.setClient(client);
        questionnaire.setCategory(category);
        questionnaire.setTimeWhenSolved(dateTimeParser(result.getMetadata().getTimestamp()));
        return clientQuestionnaireRepository.save(questionnaire);
    }

    private String buildAiPrompt(QuestionnaireResult result){

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Cliente: ").append(result.getMetadata().getClientType()).append("\n\n");
        promptBuilder.append("Categoría: ").append(result.getMetadata().getCategory()).append("\n\n");

        for(QuestionnaireAnswer answerData : result.getAnswers()){
            Question question = questionRepository.findById((long) answerData.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", answerData.getAnswer());

            promptBuilder.append("Pregunta: ").append(question.getQuestion()).append("\n");
            promptBuilder.append("Respuesta: ").append(answerText).append("\n\n");
        }

        return promptBuilder.toString();

    }

    private void saveAnswersOfQuestionnaire(QuestionnaireResult result, ClientQuestionnaire questionnaire){

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionRepository.findById((long) qa.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", qa.getAnswer());

            AnswersOfQuestionnaire answer = new AnswersOfQuestionnaire();
            answer.setQuestionnaire(questionnaire);
            answer.setQuestion(question);
            answer.setAnswerText(answerText);
            answersOfQuestionnaireRepository.save(answer);
        }
    }

    private void saveAnalysis(Category category, ClientQuestionnaire questionnaire, ResponseEntity<String> aiServiceResponse){
        AiClientAnalysis analysis = new AiClientAnalysis();
        analysis.setQuestionnaire(questionnaire);
        analysis.setCategory(category);
        if (aiServiceResponse.getStatusCode().is2xxSuccessful()) {
            analysis.setRecommendation(aiServiceResponse.getBody());
        } else {
            analysis.setRecommendation("Error al realizar recomendación, inténtelo nuevamente.");
        }
        analysis.setTimestamp(LocalDateTime.now());
        aiClientAnalysisRepository.save(analysis);
    }
}
