package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.AnalysisDTO;
import itm.proyectoharoldo.backend.Models.Analysis;
import itm.proyectoharoldo.backend.Models.AnalysisStatus;
import itm.proyectoharoldo.backend.Repositories.AnalysisRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Services.AnalysisService;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;

import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analyses")
@AllArgsConstructor
public class AnalysesController {

    private final AnalysisRepository analysisRepository;
    private final AnalysisService analysisService;
    private final UserRepository userRepository;
    private final ClientAnswerService clientAnswerService;

    @GetMapping("/all")
    public ResponseEntity<List<AnalysisDTO>> getAllAnalyses() {
        List<AnalysisDTO> questionnaires = analysisRepository.findAll()
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AnalysisDTO>> getPendingAnalyses() {
        List<AnalysisDTO> questionnaires = analysisRepository.findByStatus(AnalysisStatus.pending)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }

    @GetMapping("/proofread")
    public ResponseEntity<List<AnalysisDTO>> getCheckedAnalyses() {
        List<AnalysisDTO> questionnaires = analysisRepository.findByStatus(AnalysisStatus.checked)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questionnaires);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisDTO analysisFromWeb) {
        Analysis analysis = analysisRepository.findById(id).orElseThrow(() -> new RuntimeException("Analysis not found"));
        analysis.setStatus(AnalysisStatus.valueOf(analysisFromWeb.getStatus()));
        analysis.setContenidoRevision(analysisFromWeb.getContenidoRevision());
        analysis.setColorSemaforo(analysisFromWeb.getColorSemaforo());
        analysis.setRecomendacionInicial(analysisFromWeb.getRecomendacionInicial());
        analysis.setAsesor(userRepository.findByLegalName(analysisFromWeb.getAsesorName()).get());
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @PutMapping("/setChecked/{id}")
    public ResponseEntity<AnalysisDTO> setCheckedAnalysis(@PathVariable Long id) {
        Analysis analysis = analysisRepository.findById(id).orElseThrow(() -> new RuntimeException("Questionnaire not found"));
        analysis.setStatus(AnalysisStatus.checked);
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<AnalysisDTO>> getAllUserAnalyses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            List<AnalysisDTO> analyses = clientAnswerService.getAllUserAnalyses(userEmail);
            return ResponseEntity.ok(analyses);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuarioYCategoria/{categoria}")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysesByCategory(
            @PathVariable String categoria) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            List<AnalysisDTO> analyses = clientAnswerService.getUserAnalysesByCategory(userEmail, categoria);
            return ResponseEntity.ok(analyses);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
