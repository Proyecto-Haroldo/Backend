package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordsService {

    private final GlossaryWordsService glossaryWordsService;

    public KeywordsService(GlossaryWordsService glossaryWordsService) {
        this.glossaryWordsService = glossaryWordsService;
    }

    public void enrichQuestionWithKeywords(QuestionWebModel questionWebModel) {
        List<String> allKeywords = glossaryWordsService.getAllKeys();

        List<GlossaryWord> foundKeywords = new ArrayList<>();

        for (String keyword : allKeywords) {
            if (questionWebModel.getTitle() != null && questionWebModel.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                GlossaryWord keywordData = glossaryWordsService.getKeyword(keyword);
                if (keywordData != null) {
                    foundKeywords.add(keywordData);
                }
            }

            for (AnswersOptionWebModel option : questionWebModel.getOptions()) {
                if (option.getText() != null && option.getText().toLowerCase().contains(keyword.toLowerCase())) {
                    GlossaryWord keywordData = glossaryWordsService.getKeyword(keyword);
                    if (keywordData != null) {
                        foundKeywords.add(keywordData);
                    }
                }
            }
        }

        questionWebModel.setKeywords(
                foundKeywords.stream().distinct().collect(Collectors.toList())
        );
    }

}
