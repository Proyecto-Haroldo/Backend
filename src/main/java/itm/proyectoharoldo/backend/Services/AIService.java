package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.DTO.AIRecommendationRequest;
import itm.proyectoharoldo.backend.Models.DTO.Analysis.AIRawResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${microservice.ia.url}")
    private String iaUrl;

    private final RestTemplate restTemplate;

    public AIService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(@NonNull ClientHttpResponse  response) throws IOException {
                logger.error("Error communicating with microservice: {} - {}",
                        response.getStatusCode(), response.getStatusText());
            }
        });
    }

    @SuppressWarnings("null")
    public AIRawResponse getAiRecommendation(String prompt) {
        logger.info("Sending request to microservice at: {}", iaUrl);
        logger.debug("Request payload: {}", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AIRecommendationRequest> entity = new HttpEntity<>(new AIRecommendationRequest(prompt), headers);

        try {
            ResponseEntity<AIRawResponse> response = restTemplate.postForEntity(iaUrl, entity, AIRawResponse.class);
            logger.info("Microservice response status: {}", response.getStatusCode());
            logger.debug("Microservice response body: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            logger.warn("Microservice returned non-success status: {}", response.getStatusCode());
            return null;

        } catch (Exception e) {
            logger.error("Exception while communicating with microservice: {}", e.getMessage(), e);
            return null;
        }
    }
}