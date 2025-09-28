package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.DTO.AIClientAnalysesDTO;
import itm.proyectoharoldo.backend.Services.ClientAnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analisis")
public class AnalysisController {

    private final ClientAnswerService clientAnswerService;

    public AnalysisController(ClientAnswerService clientAnswerService) {
        this.clientAnswerService = clientAnswerService;
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<AIClientAnalysesDTO>> getAllUserAnalyses() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            List<AIClientAnalysesDTO> analyses = clientAnswerService.getAllUserAnalyses(userEmail);
            return ResponseEntity.ok(analyses);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/categoria/{categoria}")
    public ResponseEntity<List<AIClientAnalysesDTO>> getUserAnalysesByCategory(
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
            
            List<AIClientAnalysesDTO> analyses = clientAnswerService.getUserAnalysesByCategory(userEmail, categoria);
            return ResponseEntity.ok(analyses);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

