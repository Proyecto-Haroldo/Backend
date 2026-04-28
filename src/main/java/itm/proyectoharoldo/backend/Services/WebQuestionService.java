package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.Web.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Utility.QuestionConverter;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WebQuestionService {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final MultipleOptionAnswersService multipleOptionAnswersService;
    private final KeywordsService keywordsService;

    @Transactional(readOnly = true)
    public List<QuestionWebModel> getAllQuestions() {
        List<QuestionWebModel> models = questionRepository.findAllWithOptions()
                .stream()
                .map(this::toWebModel)
                .toList();
        keywordsService.enrichQuestionsWithKeywords(models);
        return models;
    }

    @Transactional(readOnly = true)
    public List<QuestionWebModel> getQuestionsByCategory(String categoryName) {
        List<QuestionWebModel> models = questionRepository.findByCategoryNameWithOptions(categoryName)
                .stream()
                .map(this::toWebModel)
                .toList();
        keywordsService.enrichQuestionsWithKeywords(models);
        return models;
    }

    @Transactional(readOnly = true)
    public List<QuestionWebModel> getQuestionsByQuestionnaire(Long questionnaireId) {
        List<QuestionWebModel> models = questionRepository.findByQuestionnaireWithOptions(questionnaireId)
                .stream()
                .map(this::toWebModel)
                .toList();
        keywordsService.enrichQuestionsWithKeywords(models);
        return models;
    }

    @Transactional(readOnly = true)
    public QuestionWebModel getQuestionById(@NonNull Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada con id: " + id));
        QuestionWebModel model = toWebModel(question);
        keywordsService.enrichQuestionWithKeywords(model);
        return model;
    }

    @Transactional
    @SuppressWarnings("null")
    public Question createQuestion(QuestionWebModel webModel) {
        Category category = categoryRepository.findByTitle(webModel.getCategoryName())
                .orElseThrow(
                        () -> new NoSuchElementException("Categoría no encontrada: " + webModel.getCategoryName()));

        Questionnaire questionnaire = questionnaireRepository.findByCategory(category)
                .stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Cuestionario no encontrado para categoría: " + webModel.getCategoryName()));

        return questionRepository.save(QuestionConverter.convertWebModelToEntity(webModel, questionnaire));
    }

    @Transactional
    @SuppressWarnings("null")
    public Question updateQuestion(Long id, QuestionWebModel webModel) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada con id: " + id));

        if (webModel.getQuestion() != null)
            existing.setQuestion(webModel.getQuestion());
        if (webModel.getQuestionType() != null)
            existing.setQuestionType(webModel.getQuestionType());

        if (webModel.getOptions() != null) {
            existing.getOptions().clear();
            List<MultipleOptionQuestionAnswer> newOptions = webModel.getOptions().stream()
                    .map(optionWeb -> {
                        MultipleOptionQuestionAnswer option = new MultipleOptionQuestionAnswer();
                        option.setAnswertext(optionWeb.getText());
                        option.setQuestion(existing);
                        return option;
                    })
                    .toList();
            existing.getOptions().addAll(newOptions);
        }

        return questionRepository.save(existing);
    }

    @Transactional
    public void deleteQuestion(@NonNull Long id) {
        if (!questionRepository.existsById(id)) {
            throw new NoSuchElementException("Pregunta no encontrada con id: " + id);
        }
        questionRepository.deleteById(id);
    }

    private QuestionWebModel toWebModel(Question question) {
        QuestionWebModel model = new QuestionWebModel();
        model.setId(question.getQuestionid());
        model.setQuestion(question.getQuestion());
        model.setQuestionType(question.getQuestionType());
        model.setOptions(multipleOptionAnswersService.getAnswersAsWebModel(question));
        model.setKeywords(List.of());

        if (question.getQuestionnaire() != null) {
            model.setQuestionnaireId(question.getQuestionnaire().getId());
            model.setCategoryName(question.getQuestionnaire().getCategory().getTitle());
            model.setCategoryId(question.getQuestionnaire().getCategory().getCategoryid());
        }

        return model;
    }
}