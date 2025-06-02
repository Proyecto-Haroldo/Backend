package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Services.GlossaryWordsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/words")
public class GlossaryWordsController {

    private final GlossaryWordsService glossaryWordsService;

    public GlossaryWordsController(GlossaryWordsService glossaryWordsService) {
        this.glossaryWordsService = glossaryWordsService;
    }

    @GetMapping("/{title}")
    public GlossaryWord getKeyword(@PathVariable String title) {
        return glossaryWordsService.getKeyword(title);
    }

    @GetMapping("/keys")
    public List<String> getKeys(){
        return glossaryWordsService.getAllKeys();
    }

}
