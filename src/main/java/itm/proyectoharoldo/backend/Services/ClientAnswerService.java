package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public String saveQuestionnaireResult(QuestionnaireResult result) {
        Client client = clientRepository.findById(1L).orElseThrow();
        LocalDateTime timestamp = LocalDateTime.parse(result.getMetadata().getTimestamp());
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
        String recomendacion = aiService.getRecommendationAsText(prompt);

        AiClientAnalysis analysis = new AiClientAnalysis();
        analysis.setQuestionnaire(questionnaire);
        analysis.setCategory(category);
        analysis.setRecommendation(recomendacion);
        analysis.setTimestamp(LocalDateTime.now());
        aiClientAnalysisRepository.save(analysis);

        return recomendacion;
    }
}
