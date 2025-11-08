package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.AnalysisDTO;
import itm.proyectoharoldo.backend.Models.Analysis;
import itm.proyectoharoldo.backend.Models.AnalysisStatus;
import itm.proyectoharoldo.backend.Repositories.AnalysisRepository;
import itm.proyectoharoldo.backend.Repositories.UserRepository;
import itm.proyectoharoldo.backend.Services.AnalysisService;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analysis")
@AllArgsConstructor
public class AnalysisController {

    private final AnalysisRepository analysisRepository;
    private final AnalysisService analysisService;
    private final UserRepository userRepository;
    private final ClientAnswerService clientAnswerService;

    @GetMapping("/all")
    public ResponseEntity<List<AnalysisDTO>> getAllAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findAll()
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AnalysisDTO>> getPendingAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findByStatus(AnalysisStatus.pending)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/proofread")
    public ResponseEntity<List<AnalysisDTO>> getCheckedAnalysis() {
        List<AnalysisDTO> analysis = analysisRepository.findByStatus(AnalysisStatus.checked)
                .stream()
                .map(analysisService::toAnalysisDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(analysis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisDTO analysisFromWeb) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
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
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));
        analysis.setStatus(AnalysisStatus.checked);
        analysisRepository.save(analysis);
        return ResponseEntity.ok(analysisService.toAnalysisDTO(analysis));
    }

    @GetMapping("/adviser/{asesor}")
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
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysis(@PathVariable Long userId) {
        try {
            // Buscar usuario por ID
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Obtener el email del usuario
            String userEmail = user.getEmail();

            // Obtener análisis usando el email
            List<AnalysisDTO> analysis = clientAnswerService.getUserAnalysis(userEmail);

            return ResponseEntity.ok(analysis);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/category/{categoria}")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalysisByCategory(
            @PathVariable Long userId, @PathVariable String categoria) {
        try {
            // Buscar usuario por ID
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validar categoría
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Obtener el email del usuario
            String userEmail = user.getEmail();

            // Obtener análisis filtrados por categoría
            List<AnalysisDTO> analysis = clientAnswerService.getUserAnalysisByCategory(userEmail, categoria);

            return ResponseEntity.ok(analysis);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
