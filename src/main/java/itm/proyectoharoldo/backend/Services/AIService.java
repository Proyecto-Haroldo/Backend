package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.DTO.AIRecommendationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AIService {

    @Value("${microservice.ia.url}")
    private String iaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getRecommendationAsText(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AIRecommendationRequest body = new AIRecommendationRequest(prompt);

        HttpEntity<AIRecommendationRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(iaUrl, entity, Map.class);

        return response.getBody().get("recommendation").toString();
    }
}
