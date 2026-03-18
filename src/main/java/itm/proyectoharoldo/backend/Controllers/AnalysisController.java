package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Analysis.*;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO;
import itm.proyectoharoldo.backend.Models.Enums.AnalysisStatus;
import itm.proyectoharoldo.backend.Models.*;
import itm.proyectoharoldo.backend.Repositories.*;
import itm.proyectoharoldo.backend.Services.*;

import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("nullness")
@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class AnalysisController {

    private final AnalysisRepository analysisRepository;
    private final AnalysisService analysisService;
    private final UserRepository userRepository;

    @GetMapping("/all")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getAllAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findAll()
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<AnalysisDTO> getAnalysisById(@PathVariable Long id) {
        return analysisRepository.findById(id)
                .map(analysisService::toAnalysisDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}/answers")
    @Transactional
    public ResponseEntity<List<QuestionAnswerDTO>> getAnalysisAnswers(@PathVariable Long id) {
        if (!analysisRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<QuestionAnswerDTO> answers = analysisService.getAnalysisAnswers(id);
        return ResponseEntity.ok(answers);
    }

    @PutMapping("/{id}/grade")
    @Transactional
    public ResponseEntity<AnalysisDTO> gradeAnalysis(@PathVariable Long id, @RequestBody GradeRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : null;
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var adviser = userRepository.findByEmail(email)
                .orElseGet(() -> null);
        if (adviser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
        analysis.setAsesor(adviser);
        analysis.setComentarioAsesor(request.getContenidoRevision() != null ? request.getContenidoRevision() : "");
        if (request.getColorSemaforo() != null && !request.getColorSemaforo().isBlank()) {
            analysis.setColorSemaforo(request.getColorSemaforo().trim().toLowerCase());
        }
        analysis.setTimeWhenChecked(LocalDateTime.now());
        analysis.setStatus(AnalysisStatus.CHECKED);
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @GetMapping("/pending")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getPendingAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findByStatus(AnalysisStatus.PENDING)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/proofread")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getCheckedAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findByStatus(AnalysisStatus.CHECKED)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisDTO analysisFromWeb) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
        analysis.setStatus(AnalysisStatus.valueOf(analysisFromWeb.getStatus()));
        analysis.setResumenIA(analysisFromWeb.getResumenIA());
        analysis.setColorSemaforo(analysisFromWeb.getColorSemaforo());
        analysis.setAnalisisIA(analysisFromWeb.getAnalisisIA());
        analysis.setResumenIA(analysisFromWeb.getResumenIA());
        analysis.setComentarioAsesor(analysisFromWeb.getComentarioAsesor());
        analysis.setAsesor(userRepository.findByLegalName(analysisFromWeb.getAsesorName()).get());
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @PutMapping("/setChecked/{id}")
    @Transactional
    public ResponseEntity<AnalysisDTO> setCheckedAnalysis(@PathVariable Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
        analysis.setStatus(AnalysisStatus.CHECKED);
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @GetMapping("/adviser/{asesor}")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getAdviserAnalysis(@PathVariable Long asesor) {
        try {
            // Buscar asesor por ID
            var adviser = userRepository.findById(asesor)
                    .orElseThrow(() -> new RuntimeException("Adviser not found"));

            // Obtener análisis del asesor
            List<AnalysisDTO> analysis = analysisRepository.findByAsesorOrderByTimeWhenCheckedDesc(adviser)
                    .stream()
                    .map(analysisService::toAnalysisDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(analysis);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysis(@PathVariable Long userId) {
        try {
            List<AnalysisDTO> analysis = analysisRepository
                    .findByUsuarioRespondeUserIdOrderByTimeWhenSolvedDesc(userId)
                    .stream()
                    .map(analysisService::toAnalysisDTO)
                    .toList();

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/category/{categoria}")
    @Transactional
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysisByCategory(
            @PathVariable Long userId, @PathVariable String categoria) {
        try {
            // Validar categoría
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<AnalysisDTO> analysis = analysisRepository
                    .findByUsuarioRespondeUserIdAndCategoryOrderByTimeWhenSolvedDesc(userId, categoria)
                    .stream()
                    .map(analysisService::toAnalysisDTO)
                    .toList();

            return ResponseEntity.ok(analysis);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
