package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.Analysis.*;
import itm.proyectoharoldo.backend.Models.DTO.Questionnaire.QuestionAnswerDTO;
import itm.proyectoharoldo.backend.Services.*;

import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@AllArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/all")
    public ResponseEntity<List<AnalysisDTO>> getAllAnalyses() {
        return ResponseEntity.ok(analysisService.getAllAnalysesWithDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDTO> getAnalysisById(@PathVariable @NonNull Long id) {
        return analysisService.getAnalysisById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/answers")
    public ResponseEntity<List<QuestionAnswerDTO>> getAnalysisAnswers(@PathVariable Long id) {
        return ResponseEntity.ok(analysisService.getAnalysisAnswersByAnalysisId(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AnalysisDTO>> getPendingAnalyses() {
        return ResponseEntity.ok(analysisService.getPendingAnalyses());
    }

    @GetMapping("/proofread")
    public ResponseEntity<List<AnalysisDTO>> getCheckedAnalyses() {
        return ResponseEntity.ok(analysisService.getCheckedAnalyses());
    }

    @GetMapping("/adviser/{adviserId}")
    public ResponseEntity<List<AnalysisDTO>> getAdviserAnalysis(@PathVariable Long adviserId) {
        return ResponseEntity.ok(analysisService.getAdvisersAnalysesByAdviserId(adviserId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalyses(@PathVariable Long userId) {
        return ResponseEntity.ok(analysisService.getUserAnalysesByUserId(userId));
    }

    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysisByCategory(
            @PathVariable Long userId, @PathVariable String category) {
        return ResponseEntity.ok(analysisService.getUserAnalysesByUserIdAndCategory(userId, category));
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<AnalysisDTO> gradeAnalysis(@PathVariable Long analysisId, @RequestBody GradeRequest gradingRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(analysisService.gradeAnalysis(analysisId, gradingRequest, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable @NonNull Long analysisId, @RequestBody @NonNull AnalysisDTO analysisFromWeb) {
        return ResponseEntity.ok(analysisService.updateAnalysis(analysisId, analysisFromWeb));
    }

    @PutMapping("/setChecked/{id}")
    public ResponseEntity<AnalysisDTO> setCheckedAnalysisById(@PathVariable @NonNull Long analysisId) {
        return ResponseEntity.ok(analysisService.setAnalysisToCheckedByAnalysisId(analysisId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysisById(@PathVariable @NonNull Long analysisId){
        analysisService.deleteAnalysisById(analysisId);
        return ResponseEntity.noContent().build();
    }


 }
