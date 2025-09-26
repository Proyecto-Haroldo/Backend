package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.DTO.AIAnalysisResultDTO;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Utility.AIAnalysisParser;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientAnswerService {

    private final ClientRepository clientRepository;
    private final QuestionRepository questionRepository;
    private final ClientQuestionnaireRepository clientQuestionnaireRepository;
    private final AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    public ClientAnswerService(
            ClientRepository clientRepository,
            QuestionRepository questionRepository,
            ClientQuestionnaireRepository clientQuestionnaireRepository,
            AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository,
            CategoryRepository categoryRepository,
            AIService aiService
    ) {
        this.clientRepository = clientRepository;
        this.questionRepository = questionRepository;
        this.clientQuestionnaireRepository = clientQuestionnaireRepository;
        this.answersOfQuestionnaireRepository = answersOfQuestionnaireRepository;
        this.categoryRepository = categoryRepository;
        this.aiService = aiService;
    }

    @Transactional
    public AIAnalysisResultDTO saveQuestionnaireResult(QuestionnaireResult result, Long clientId) {
        Category category = categoryRepository.findByCategory(result.getMetadata().getCategory()).orElseThrow();
        String AiPrompt = buildAiPrompt(result);

        ResponseEntity<Map> recomendationResponse = aiService.getAiRecommendation(AiPrompt);
        AIAnalysisResultDTO responseDTO = processRecomendation(recomendationResponse);

        ClientQuestionnaire questionnaire = saveNewQuestionnaire(result, clientId, responseDTO);
        saveAnswersOfQuestionnaire(result, questionnaire);

        return responseDTO;
    }

    private AIAnalysisResultDTO processRecomendation(ResponseEntity<Map> recomendationResponse) {
        if (recomendationResponse.getStatusCode().is5xxServerError()) {
            return null;
        } else {
            String cleanResponse = cleanJsonResponse(recomendationResponse.getBody().get("response").toString());
            try{
                return new AIAnalysisParser().parseResponseToAnalysis(cleanResponse);
            } catch (Exception ex){
                return null;
            }
        }
    }

    private LocalDateTime dateTimeParser(String timestamp) {
        try {
            return ZonedDateTime.parse(timestamp).toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.parse(timestamp);
        }
    }

    private ClientQuestionnaire saveNewQuestionnaire(QuestionnaireResult result, Long clientId, AIAnalysisResultDTO aiAnalysisResultDTO) {

        Client client = clientRepository.findById(clientId).orElseThrow();
        Category category = categoryRepository.findByCategory(result.getMetadata().getCategory()).orElseThrow();

        ClientQuestionnaire questionnaire = new ClientQuestionnaire();
        questionnaire.setClient(client);
        questionnaire.setCategory(category);
        questionnaire.setTimeWhenSolved(dateTimeParser(result.getMetadata().getTimestamp()));
        questionnaire.setState(QuestionnaireState.pending);
        questionnaire.setRecomendacionUsuario(aiAnalysisResultDTO.getResumenUsuario());
        questionnaire.setColorSemaforo(aiAnalysisResultDTO.getColorSemaforo());
        questionnaire.setAnalisisAsesor(aiAnalysisResultDTO.getAnalisisAsesor());
        questionnaire.setConteo(clientQuestionnaireRepository.countByClientAndCategory(client, category) + 1);
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

        List<AnswersOfQuestionnaire> questionnaireAnswerList = new ArrayList<>();

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionRepository.findById((long) qa.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", qa.getAnswer());

            AnswersOfQuestionnaire answer = new AnswersOfQuestionnaire();
            answer.setQuestionnaire(questionnaire);
            answer.setQuestion(question);
            answer.setAnswerText(answerText);

            questionnaireAnswerList.add(answer);
        }

        answersOfQuestionnaireRepository.saveAll(questionnaireAnswerList);
    }

    private String cleanJsonResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return response;
        }

        String cleaned = response.trim();

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "```(?:json)?\\s*\\n?([\\s\\S]*?)```",
                java.util.regex.Pattern.DOTALL
        );
        java.util.regex.Matcher matcher = pattern.matcher(cleaned);

        if (matcher.find()) {
            cleaned = matcher.group(1).trim();
        }

        if (!cleaned.startsWith("{") || !cleaned.endsWith("}")) {
            int startIndex = cleaned.indexOf("{");
            int endIndex = cleaned.lastIndexOf("}");

            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                cleaned = cleaned.substring(startIndex, endIndex + 1);
            }
        }

        return cleaned.trim();
    }

}
