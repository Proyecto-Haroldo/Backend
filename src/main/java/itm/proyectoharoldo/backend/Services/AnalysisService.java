package itm.proyectoharoldo.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.AnalysisDTO;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO;
import itm.proyectoharoldo.backend.Repositories.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import itm.proyectoharoldo.backend.Repositories.AnswersOfQuestionnaireRepository.QuestionAnswerProjection;

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
        List<QuestionAnswerProjection> rows = answersOfQuestionnaireRepository.findAnswersByAnalysisId(analysisId);
        return rows.stream()
            .map(p -> new QuestionAnswerDTO(p.getQuestionid(), p.getQuestiontext(), p.getAnswertext()))
            .collect(Collectors.toList());
    }
}
