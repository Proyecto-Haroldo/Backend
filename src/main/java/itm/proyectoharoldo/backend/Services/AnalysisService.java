package itm.proyectoharoldo.backend.Services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.AnalysisDTO;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.GradeRequest;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO;
import itm.proyectoharoldo.backend.Models.Enums.AnalysisStatus;
import itm.proyectoharoldo.backend.Repositories.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final AnswersOfQuestionnaireRepository answersOfQuestionnaireRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getAllAnalysesWithDetails() {
        return analysisRepository.findAllWithDetails().stream()
                .map(this::toAnalysisDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<AnalysisDTO> getAnalysisById(@NonNull Long id) {
        return analysisRepository.findById(id).map(this::toAnalysisDTO);
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswerDTO> getAnalysisAnswersByAnalysisId(Long analysisId) {
        return answersOfQuestionnaireRepository.findByAnalysisId(analysisId).stream()
                .map(p -> new QuestionAnswerDTO(p.questionId(), p.questionText(), p.answerText()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getPendingAnalyses() {
        return analysisRepository.findByStatus(AnalysisStatus.PENDING)
                .stream().map(this::toAnalysisDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getCheckedAnalyses() {
        return analysisRepository.findByStatus(AnalysisStatus.CHECKED)
                .stream().map(this::toAnalysisDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getAdvisersAnalysesByAdviserId(Long adviserId) {
        return analysisRepository.findByAsesorIdOrderByTimeWhenCheckedDesc(adviserId)
                .stream().map(this::toAnalysisDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getUserAnalysesByUserId(Long userId) {
        return analysisRepository.findByUsuarioRespondeUserIdOrderByTimeWhenSolvedDesc(userId)
                .stream().map(this::toAnalysisDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AnalysisDTO> getUserAnalysesByUserIdAndCategory(Long userId, String category) {
        return analysisRepository.findByUsuarioRespondeUserIdAndCategoryOrderByTimeWhenSolvedDesc(userId, category)
                .stream().map(this::toAnalysisDTO).toList();
    }

    @Transactional
    public AnalysisDTO gradeAnalysis(Long analysisId, GradeRequest request, String adviserEmail) {

        User adviser = userRepository.findByEmail(adviserEmail)
                .orElseThrow(() -> new NoSuchElementException("Asesor no encontrado"));

        Analysis analysis = analysisRepository.findByIdWithDetails(analysisId)
                .orElseThrow(() -> new NoSuchElementException("Análisis no encontrado con id: " + analysisId));

        analysis.setAsesor(adviser);
        analysis.setComentarioAsesor(request.getContenidoRevision() != null ? request.getContenidoRevision() : "");
        analysis.setTimeWhenChecked(LocalDateTime.now());
        analysis.setStatus(AnalysisStatus.CHECKED);

        if (request.getColorSemaforo() != null && !request.getColorSemaforo().isBlank()) {
            analysis.setColorSemaforo(request.getColorSemaforo().trim().toLowerCase());
        }

        return toAnalysisDTO(analysisRepository.save(analysis));
    }

    @SuppressWarnings("null")
    @Transactional
    public AnalysisDTO updateAnalysis(@NonNull Long analysisId, @NonNull AnalysisDTO newAnalysis) {

        Analysis analysis = analysisRepository.findByIdWithDetails(analysisId)
                .orElseThrow(() -> new NoSuchElementException("Análisis no encontrado con id: " + analysisId));

        if (newAnalysis.getStatus() != null) {
            analysis.setStatus(AnalysisStatus.valueOf(newAnalysis.getStatus()));
        }
        if (newAnalysis.getResumenIA() != null) {
            analysis.setResumenIA(newAnalysis.getResumenIA());
        }
        if (newAnalysis.getColorSemaforo() != null) {
            analysis.setColorSemaforo(newAnalysis.getColorSemaforo());
        }
        if (newAnalysis.getAnalisisIA() != null) {
            analysis.setAnalisisIA(newAnalysis.getAnalisisIA());
        }
        if (newAnalysis.getComentarioAsesor() != null) {
            analysis.setComentarioAsesor(newAnalysis.getComentarioAsesor());
        }
        if (newAnalysis.getAsesorName() != null) {
            User asesor = userRepository.findByLegalName(newAnalysis.getAsesorName())
                    .orElseThrow(
                            () -> new NoSuchElementException("Asesor no encontrado: " + newAnalysis.getAsesorName()));
            analysis.setAsesor(asesor);
        }

        return toAnalysisDTO(analysisRepository.save(analysis));
    }

    @Transactional
    public AnalysisDTO setAnalysisToCheckedByAnalysisId(@NonNull Long analysisId) {
        Analysis analysis = analysisRepository.findByIdWithDetails(analysisId)
                .orElseThrow(() -> new NoSuchElementException("No se halló análisis de ID: " + analysisId));

        analysis.setStatus(AnalysisStatus.CHECKED);
        return toAnalysisDTO(analysisRepository.save(analysis));
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteAnalysisById(@NonNull Long analysisId) {
        analysisRepository.delete(analysisRepository.findByIdWithDetails(analysisId)
                .orElseThrow(() -> new NoSuchElementException("No se halló el análisis de ID: " + analysisId)));
    }

    @Transactional(readOnly = true)
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
        dto.setCategoryName(analysis.getQuestionnaire() != null && analysis.getQuestionnaire().getCategory() != null
                ? analysis.getQuestionnaire().getCategory().getTitle()
                : null);

        dto.setQuestionnaireTitle(analysis.getQuestionnaire().getTitle());

        // Conteo: how many analyses this user has for this questionnaire
        dto.setConteo(analysis.getUsuarioResponde() != null && analysis.getQuestionnaire() != null
                ? analysisRepository.countByUsuarioRespondeAndQuestionnaire(
                        analysis.getUsuarioResponde(),
                        analysis.getQuestionnaire())
                : null);

        return dto;
    }

}