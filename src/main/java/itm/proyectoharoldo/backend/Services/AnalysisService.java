package itm.proyectoharoldo.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itm.proyectoharoldo.backend.Models.Analysis;
import itm.proyectoharoldo.backend.Models.DTO.AnalysisDTO;
import itm.proyectoharoldo.backend.Repositories.AnalysisRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnalysisService {

    @Autowired
    private final AnalysisRepository analysisRepository;

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
    
}
