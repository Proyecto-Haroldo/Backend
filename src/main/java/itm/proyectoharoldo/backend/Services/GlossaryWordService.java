package itm.proyectoharoldo.backend.Services;

import io.github.cdimascio.dotenv.Dotenv;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GlossaryWordService {

    public GlossaryWordService(){

    }

    public GlossaryWord getGlossaryWord(String word){

        Dotenv env = Dotenv.load();
        String redisToken = env.get("REDIS_TOKEN");
        RestTemplate restTemplate = new RestTemplate();
        String URI = "https://handy-lemming-28224.upstash.io/GET/" + word;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(redisToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                URI,
                HttpMethod.GET,
                requestEntity,
                ApiResponse.class
        );

        ApiResponse apiResponse = response.getBody();

        if (apiResponse != null && apiResponse.getResult() != null) {
            GlossaryWord glossaryWord = new GlossaryWord();
            glossaryWord.setTitle(word);
            glossaryWord.setDescription(apiResponse.getResult());
            return glossaryWord;
        }

        return null;

    }

    public static class ApiResponse {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public List<String> getAllGlossaryWords(){
        Dotenv env = Dotenv.load();
        String redisToken = env.get("REDIS_TOKEN");
        RestTemplate restTemplate = new RestTemplate();
        String URI = "https://handy-lemming-28224.upstash.io/KEYS/*";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(redisToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String[]> response = restTemplate.exchange(
                URI,
                HttpMethod.GET,
                requestEntity,
                String[].class
        );

        return Arrays.asList(response.getBody());
    }

}
