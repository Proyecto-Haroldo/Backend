package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.Web.AnswersOptionWebModel;
import itm.proyectoharoldo.backend.Models.Web.QuestionWebModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KeywordsService {

    private final GlossaryWordsService glossaryWordsService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public KeywordsService(GlossaryWordsService glossaryWordsService) {
        this.glossaryWordsService = glossaryWordsService;
    }

    public void enrichQuestionWithKeywords(QuestionWebModel questionWebModel) {
        // Extract all text content from question and options
        Set<String> textContent = new HashSet<>();
        if (questionWebModel.getTitle() != null) {
            textContent.add(questionWebModel.getTitle().toLowerCase());
        }
        
        for (AnswersOptionWebModel option : questionWebModel.getOptions()) {
            if (option.getText() != null) {
                textContent.add(option.getText().toLowerCase());
            }
        }

        // Get all keywords once
        List<String> allKeywords = glossaryWordsService.getAllKeys();
        
        // Find matching keywords efficiently
        Set<String> matchingKeywords = new HashSet<>();
        for (String keyword : allKeywords) {
            String lowerKeyword = keyword.toLowerCase();
            for (String text : textContent) {
                if (text.contains(lowerKeyword)) {
                    matchingKeywords.add(keyword);
                    break; // No need to check other text for this keyword
                }
            }
        }

        // Batch fetch all matching keywords
        List<GlossaryWord> foundKeywords = matchingKeywords.parallelStream()
                .map(glossaryWordsService::getKeyword)
                .filter(keyword -> keyword != null)
                .collect(Collectors.toList());

        questionWebModel.setKeywords(foundKeywords);
    }

    // Batch processing method for multiple questions
    public void enrichQuestionsWithKeywords(List<QuestionWebModel> questions) {
        if (questions.isEmpty()) return;

        // Get all keywords once for all questions
        List<String> allKeywords = glossaryWordsService.getAllKeys();
        
        // Process all questions in parallel
        List<CompletableFuture<Void>> futures = questions.stream()
                .map(question -> CompletableFuture.runAsync(() -> 
                    enrichQuestionWithKeywordsOptimized(question, allKeywords), executorService))
                .collect(Collectors.toList());

        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void enrichQuestionWithKeywordsOptimized(QuestionWebModel questionWebModel, List<String> allKeywords) {
        // Extract all text content from question and options
        Set<String> textContent = new HashSet<>();
        if (questionWebModel.getTitle() != null) {
            textContent.add(questionWebModel.getTitle().toLowerCase());
        }
        
        for (AnswersOptionWebModel option : questionWebModel.getOptions()) {
            if (option.getText() != null) {
                textContent.add(option.getText().toLowerCase());
            }
        }

        // Find matching keywords efficiently
        Set<String> matchingKeywords = new HashSet<>();
        for (String keyword : allKeywords) {
            String lowerKeyword = keyword.toLowerCase();
            for (String text : textContent) {
                if (text.contains(lowerKeyword)) {
                    matchingKeywords.add(keyword);
                    break; // No need to check other text for this keyword
                }
            }
        }

        // Batch fetch all matching keywords
        List<GlossaryWord> foundKeywords = matchingKeywords.parallelStream()
                .map(glossaryWordsService::getKeyword)
                .filter(keyword -> keyword != null)
                .collect(Collectors.toList());

        questionWebModel.setKeywords(foundKeywords);
    }

}
