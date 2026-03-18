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

    public AnalysisDTO toAnalysisDTO(Analysis analysis) {
        AnalysisDTO dto = new AnalysisDTO();

        dto.setAnalysisId(analysis.getAnalysisId());
        dto.setStatus(analysis.getStatus() != null ? analysis.getStatus().name() : null);
        dto.setAnalisisIA(analysis.getAnalisisIA());
        dto.setColorSemaforo(analysis.getColorSemaforo());
        dto.setResumenIA(analysis.getResumenIA());
        dto.setComentarioAsesor(analysis.getComentarioAsesor());
        dto.setTimeWhenSolved(analysis.getTimeWhenSolved());
        dto.setTimeWhenChecked(analysis.getTimeWhenChecked());

        // From joined User entities
        dto.setAsesorName(analysis.getAsesor() != null ? analysis.getAsesor().getLegalName() : null);
        dto.setClientName(analysis.getUsuarioResponde() != null ? analysis.getUsuarioResponde().getLegalName() : null);

        // From joined Questionnaire → Category
        dto.setCategoria(analysis.getQuestionnaire() != null && analysis.getQuestionnaire().getCategory() != null
                ? analysis.getQuestionnaire().getCategory().getCategory()
                : null);

        // Conteo: how many analyses this user has for this questionnaire
        dto.setConteo(analysis.getUsuarioResponde() != null && analysis.getQuestionnaire() != null
                ? analysisRepository.countByUsuarioRespondeAndQuestionnaire(
                        analysis.getUsuarioResponde(),
                        analysis.getQuestionnaire())
                : null);

        return dto;
    }

    public List<QuestionAnswerDTO> getAnalysisAnswers(Long analysisId) {
        List<QuestionAnswerProjection> rows = answersOfQuestionnaireRepository.findAnswersByAnalysisId(analysisId);
        return rows.stream()
                .map(p -> new QuestionAnswerDTO(p.getQuestionid(), p.getQuestiontext(), p.getAnswertext()))
                .collect(Collectors.toList());
    }
}
