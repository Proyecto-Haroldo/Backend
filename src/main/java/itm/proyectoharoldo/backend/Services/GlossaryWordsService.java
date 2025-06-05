package itm.proyectoharoldo.backend.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itm.proyectoharoldo.backend.Models.GlossaryWord;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GlossaryWordsService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String redisBaseUrl = "https://handy-lemming-28224.upstash.io";
    private final String bearerToken = "AW5AAAIjcDE4ZTk3MWJiNTg3MjI0NjE0ODIyMmNjNTY5OGI0ZTE2ZHAxMA";

    public List<String> getAllKeys(){
        String url = redisBaseUrl + "/keys/*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            List<String> keys = (List<String>) body.get("result");
            return keys;
        }

        return List.of();
    }

    public GlossaryWord getKeyword(String title) {
        String url = redisBaseUrl + "/get/" + title;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> body = response.getBody();

            String rawResult = (String) body.get("result");
            return new GlossaryWord(title, rawResult);

        }

        return null;
    }

}
