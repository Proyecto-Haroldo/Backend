package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.*;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Utility.AIAnalysisParser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ClientAnswerService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final AnalysisRepository analysisRepository;
    private final AnalysisService analysisService;
    private final AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository;
    private final CategoryRepository categoryRepository;
    private final AIService aiService;

    @Transactional
    public AIAnalysisResultDTO saveQuestionnaireResult(QuestionnaireResult result, Long clientId) {
        String AiPrompt = buildAiPrompt(result);

        ResponseEntity<Map> recomendationResponse = aiService.getAiRecommendation(AiPrompt);
        AIAnalysisResultDTO responseDTO = processRecomendation(recomendationResponse);

        Analysis savedAnalysis = saveNewAnalysis(result, clientId, responseDTO);
        saveAnswersOfQuestionnaire(result, savedAnalysis);

        return responseDTO;
    }

    @SuppressWarnings("unchecked")
    private AIAnalysisResultDTO processRecomendation(ResponseEntity<Map> recomendationResponse) {
        if (recomendationResponse.getStatusCode().is5xxServerError()) {
            return null;
        } else {
            Map<String, Object> responseBody = (Map<String, Object>) recomendationResponse.getBody();
            if (responseBody == null || !responseBody.containsKey("response")) {
                return null;
            }
            String cleanResponse = cleanJsonResponse(responseBody.get("response").toString());
            try {
                return new AIAnalysisParser().parseResponseToAnalysis(cleanResponse);
            } catch (Exception ex) {
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

    private Analysis saveNewAnalysis(QuestionnaireResult result, Long userId, AIAnalysisResultDTO aiAnalysisResultDTO) {

        User client = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findByCategory(result.getMetadata().getCategory()).orElseThrow();

        Analysis analysis = new Analysis();
        analysis.setUsuarioResponde(client);
        analysis.setQuestionnaire(questionnaireRepository.findByCategory(category).getFirst());
        analysis.setTimeWhenSolved(dateTimeParser(result.getMetadata().getTimestamp()));
        analysis.setStatus(AnalysisStatus.PENDING);
        analysis.setRecomendacionInicial(aiAnalysisResultDTO.getResumenUsuario());
        analysis.setColorSemaforo(aiAnalysisResultDTO.getColorSemaforo());
        analysis.setContenidoRevision(aiAnalysisResultDTO.getAnalisisAsesor());
        return analysisRepository.save(analysis);
    }

    private String buildAiPrompt(QuestionnaireResult result) {

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Cliente: ").append(result.getMetadata().getClientType()).append("\n\n");
        promptBuilder.append("Categoría: ").append(result.getMetadata().getCategory()).append("\n\n");

        for (QuestionnaireAnswer answerData : result.getAnswers()) {
            Question question = questionRepository.findById((long) answerData.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", answerData.getAnswer());

            promptBuilder.append("Pregunta: ").append(question.getQuestion()).append("\n");
            promptBuilder.append("Respuesta: ").append(answerText).append("\n\n");
        }

        return promptBuilder.toString();

    }

    private void saveAnswersOfQuestionnaire(QuestionnaireResult result, Analysis analysis) {

        List<AnswersOfQuestionnaire> questionnaireAnswerList = new ArrayList<>();

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionRepository.findById((long) qa.getQuestionId()).orElseThrow();
            String answerText = String.join(" | ", qa.getAnswer());

            AnswersOfQuestionnaire answer = new AnswersOfQuestionnaire();
            answer.setQuestion(question);
            answer.setAnswerText(answerText);
            answer.setAnalysis(analysis);

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
                java.util.regex.Pattern.DOTALL);
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

    public List<AnalysisDTO> getUserAnalysis(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

        List<Analysis> analysis = analysisRepository
                .findByUsuarioRespondeOrderByTimeWhenSolvedDesc(user);

        return analysis.stream()
                .map(analysisService::toAnalysisDTO)
                .toList();
    }

    public List<AnalysisDTO> getUserAnalysisByCategory(String userEmail, String categoryName) {
        User client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

        Category category = categoryRepository.findByCategory(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoryName));

        List<Analysis> analysis = analysisRepository
                .findByUsuarioRespondeAndQuestionnaireOrderByTimeWhenSolvedDesc(client,
                        questionnaireRepository.findByCategory(category).getFirst());

        return analysis.stream()
                .map(analysisService::toAnalysisDTO)
                .toList();
    }

}
