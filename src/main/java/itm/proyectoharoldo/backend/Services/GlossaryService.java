package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlossaryService {

    private final GlossaryWordService glossaryWordService;
    private List<String> glossary;

    public GlossaryService(GlossaryWordService glossaryWordService) {
        this.glossaryWordService = glossaryWordService;
    }



}
