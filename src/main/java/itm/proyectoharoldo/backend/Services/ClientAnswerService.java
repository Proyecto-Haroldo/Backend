package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.*;
import itm.proyectoharoldo.backend.Models.Enums.AnalysisStatus;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;

import itm.proyectoharoldo.backend.Utility.AIAnalysisParser;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.time.*;
import java.util.*;
import java.util.stream.*;

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
    public AIAnalysisResultDTO saveQuestionnaireResult(QuestionnaireResult result, @NonNull Long clientId) {
        String aiPrompt = buildAiPrompt(result);

        AIRawResponse recommendationResponse = aiService.getAiRecommendation(aiPrompt);
        AIAnalysisResultDTO responseDTO = processRecommendation(recommendationResponse);

        if (responseDTO == null) {
            throw new ResourceAccessException("Ocurrió un fallo con la respuesta de la IA");
        }

        Analysis savedAnalysis = saveNewAnalysis(result, clientId, responseDTO);
        saveAnswersOfQuestionnaire(result, savedAnalysis);

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getUserAnalysis(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + userEmail));

        return analysisRepository
                .findByUsuarioRespondeOrderByTimeWhenSolvedDesc(user)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getUserAnalysisByCategory(String userEmail, String categoryName) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + userEmail));

        Category category = categoryRepository.findByTitle(categoryName)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada: " + categoryName));

        Questionnaire questionnaire = questionnaireRepository.findByCategory(category)
                .stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cuestionario no encontrado para categoría: " + categoryName));

        return analysisRepository
                .findByUsuarioRespondeAndQuestionnaireOrderByTimeWhenSolvedDesc(user, questionnaire)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .toList();
    }

    private Analysis saveNewAnalysis(QuestionnaireResult result, @NonNull Long userId, AIAnalysisResultDTO dto) {
        User client = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + userId));

        Analysis analysis = new Analysis();
        analysis.setUsuarioResponde(client);
        analysis.setQuestionnaire(questionnaireRepository.findByTitle(result.getMetadata().getTitle()));
        analysis.setTimeWhenSolved(dateTimeParser(result.getMetadata().getTimestamp()));
        analysis.setStatus(AnalysisStatus.PENDING);
        analysis.setAnalisisIA(dto.getAnalisisAsesor());
        analysis.setColorSemaforo(dto.getColorSemaforo());
        analysis.setResumenIA(dto.getResumenUsuario());

        return analysisRepository.save(analysis);
    }

    @SuppressWarnings("null")
    private void saveAnswersOfQuestionnaire(QuestionnaireResult result, Analysis analysis) {
        List<Long> questionIds = result.getAnswers().stream()
                .map(qa -> (long) qa.getQuestionId())
                .toList();

        Map<Long, Question> questionMap = questionRepository.findAllById(questionIds)
                .stream()
                .collect(Collectors.toMap(Question::getQuestionid, q -> q));

        List<AnswersOfQuestionnaire> answers = result.getAnswers().stream()
                .map(qa -> {
                    Question question = questionMap.get((long) qa.getQuestionId());
                    if (question == null) throw new NoSuchElementException("Pregunta no encontrada: " + qa.getQuestionId());

                    AnswersOfQuestionnaire answer = new AnswersOfQuestionnaire();
                    answer.setQuestion(question);
                    answer.setAnswerText(String.join(" | ", qa.getAnswer()));
                    answer.setAnalysis(analysis);
                    return answer;
                })
                .toList();

        answersOfQuestionnaireRepository.saveAll(answers);
    }

    private AIAnalysisResultDTO processRecommendation(AIRawResponse response) {
        if (response == null || response.getResponse() == null) return null;
    
        String cleaned = cleanJsonResponse(response.getResponse());
        try {
            return new AIAnalysisParser().parseResponseToAnalysis(cleaned);
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("null")
    private String buildAiPrompt(QuestionnaireResult result) {
        List<Long> questionIds = result.getAnswers().stream()
                .map(qa -> (long) qa.getQuestionId())
                .toList();

        Map<Long, Question> questionMap = questionRepository.findAllById(questionIds)
                .stream()
                .collect(Collectors.toMap(Question::getQuestionid, q -> q));

        StringBuilder prompt = new StringBuilder();
        prompt.append("Cliente: ").append(result.getMetadata().getClientType()).append("\n\n");
        prompt.append("Categoría: ").append(result.getMetadata().getCategory()).append("\n\n");

        for (QuestionnaireAnswer qa : result.getAnswers()) {
            Question question = questionMap.get((long) qa.getQuestionId());
            prompt.append("Pregunta: ").append(question.getQuestion()).append("\n");
            prompt.append("Respuesta: ").append(String.join(" | ", qa.getAnswer())).append("\n\n");
        }

        return prompt.toString();
    }

    private String cleanJsonResponse(String response) {
        if (response == null || response.trim().isEmpty()) return response;

        String cleaned = response.trim();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "```(?:json)?\\s*\\n?([\\s\\S]*?)```",
                java.util.regex.Pattern.DOTALL);
        java.util.regex.Matcher matcher = pattern.matcher(cleaned);

        if (matcher.find()) cleaned = matcher.group(1).trim();

        if (!cleaned.startsWith("{") || !cleaned.endsWith("}")) {
            int start = cleaned.indexOf("{");
            int end = cleaned.lastIndexOf("}");
            if (start != -1 && end != -1 && end > start) {
                cleaned = cleaned.substring(start, end + 1);
            }
        }

        return cleaned.trim();
    }

    private LocalDateTime dateTimeParser(String timestamp) {
        try {
            return ZonedDateTime.parse(timestamp).toLocalDateTime();
        } catch (Exception e) {
            return LocalDateTime.parse(timestamp);
        }
    }
}