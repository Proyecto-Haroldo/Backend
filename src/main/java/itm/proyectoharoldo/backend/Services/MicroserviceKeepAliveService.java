package itm.proyectoharoldo.backend.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MicroserviceKeepAliveService {

    private static final Logger logger = LoggerFactory.getLogger(MicroserviceKeepAliveService.class);

    @Value("${microservice.ia.url}")
    private String iaUrl;

    private final RestTemplate restTemplate;

    @SuppressWarnings("null")
    @Scheduled(fixedRate = 10000) // every 14 minutes
    public void pingMicroservice() {
        String healthUrl = iaUrl.replace("/ia/recomendacion", "/ia/health");
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            logger.info("Microservice keepalive ping: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.warn("Microservice keepalive ping failed: {}", e.getMessage());
        }
    }
}
