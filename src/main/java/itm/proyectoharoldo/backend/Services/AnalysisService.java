package itm.proyectoharoldo.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itm.proyectoharoldo.backend.Models.AnswersOfQuestionnaire;
import itm.proyectoharoldo.backend.Models.Analysis;
import itm.proyectoharoldo.backend.Models.DTO.AnalysisDTO;
import itm.proyectoharoldo.backend.Models.DTO.QuestionAnswerDTO;
import itm.proyectoharoldo.backend.Repositories.AnalysisRepository;
import itm.proyectoharoldo.backend.Repositories.AnswersOfQuestionnaireRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalysisService {

    @Autowired
    private final AnalysisRepository analysisRepository;

    @Autowired
    private final AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository;

    public AnalysisDTO toAnalysisDTO(Analysis analysis){
        return new AnalysisDTO(
            analysis.getAnalysisId(),
            analysis.getAsesor() != null ? analysis.getAsesor().getLegalName() : null,
            analysis.getUsuarioResponde().getLegalName(),
            analysis.getStatus().name(),
            analysis.getRecomendacionInicial(),
            analysis.getColorSemaforo(),
            analysis.getContenidoRevision(),
            analysis.getTimeWhenSolved(),
            analysis.getTimeWhenChecked(),
            analysisRepository.findNextAnalysisCount(analysis.getUsuarioResponde().getUserId(), analysis.getQuestionnaire().getId()),
            analysis.getQuestionnaire().getCategory().getCategory()
        );
    }

    public List<QuestionAnswerDTO> getAnalysisAnswers(Long analysisId) {
        List<AnswersOfQuestionnaire> answers = answersOfQuestionnaireRepository
            .findByAnalysis_AnalysisIdOrderByQuestion_Questionid(analysisId);
        return answers.stream()
            .map(a -> new QuestionAnswerDTO(
                a.getQuestion().getQuestionid(),
                a.getQuestion().getQuestion(),
                a.getAnswerText()
            ))
            .collect(Collectors.toList());
    }
}
