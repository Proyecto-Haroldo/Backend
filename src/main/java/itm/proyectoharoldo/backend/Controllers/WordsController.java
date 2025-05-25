package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Models.Client;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.MultipleOptionQuestionAnswer;
import itm.proyectoharoldo.backend.Services.GlossaryWordService;
import itm.proyectoharoldo.backend.Services.MultipleOptionAnswersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/palabras")
public class WordsController {

    private final GlossaryWordService glossaryWordService;
    private final MultipleOptionAnswersService multipleOptionAnswersService;

    public WordsController(GlossaryWordService glossaryWordService, MultipleOptionAnswersService multipleOptionAnswersService){
        this.glossaryWordService = glossaryWordService;
        this.multipleOptionAnswersService = multipleOptionAnswersService;
    }

    @GetMapping("/{word}")
    public ResponseEntity<GlossaryWord> GetWord(@PathVariable String word){
        GlossaryWord glossaryWord = glossaryWordService.getGlossaryWord(word);
        return ResponseEntity.ok(glossaryWord);
    }

    @GetMapping
    public ResponseEntity<List<String>> GetAllKeywords(@PathVariable Long id){
        List<String> keyWords = glossaryWordService.getAllGlossaryWords();
        return ResponseEntity.ok(keyWords);
    }

}
