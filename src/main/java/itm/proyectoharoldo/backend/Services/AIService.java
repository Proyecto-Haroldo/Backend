package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.DTO.AIRecommendationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${microservice.ia.url}")
    private String iaUrl;

    private final RestTemplate restTemplate;

    public AIService(){
        this.restTemplate = new RestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                logger.error("Error communicating with microservice: {} - {}", 
                    response.getStatusCode(), response.getStatusText());
            }
        });
    }

    public ResponseEntity<Map> getAiRecommendation(String prompt) {
        logger.info("Sending request to microservice at: {}", iaUrl);
        logger.debug("Request payload: {}", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AIRecommendationRequest body = new AIRecommendationRequest(prompt);
        HttpEntity<AIRecommendationRequest> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(iaUrl, entity, Map.class);
            logger.info("Microservice response status: {}", response.getStatusCode());
            logger.debug("Microservice response body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                logger.warn("Microservice returned non-success status: {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            logger.error("Exception while communicating with microservice: {}", e.getMessage(), e);
            Map<String, String> errorResponse = Map.of("response", "Error communicating with AI service: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
