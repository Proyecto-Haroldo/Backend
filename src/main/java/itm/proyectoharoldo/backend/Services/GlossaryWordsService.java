package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import itm.proyectoharoldo.backend.Models.DTO.RedisResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

@Service
public class GlossaryWordsService {

    private static final Logger logger = LoggerFactory.getLogger(GlossaryWordsService.class);
    private static final long CACHE_TTL_MINUTES = 30;
    private static final String ALL_KEYS_CACHE_KEY = "all_keys";

    private final RestTemplate restTemplate;

    @Value("${redis.base.url:}")
    private String redisBaseUrl;

    @Value("${redis.bearer.token:}")
    private String bearerToken;

    private final Map<String, GlossaryWord> keywordCache = new ConcurrentHashMap<>();
    private final Map<String, List<String>> allKeysCache = new ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    public GlossaryWordsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build();
    }

    @SuppressWarnings("null")
    public List<String> getAllKeys() {
        if (!isConfigured()) return Collections.emptyList();

        if (isCacheValid(ALL_KEYS_CACHE_KEY)) {
            List<String> cached = allKeysCache.get(ALL_KEYS_CACHE_KEY);
            if (cached != null) {
                logger.debug("Returning cached keys: {} items", cached.size());
                return cached;
            }
        }

        try {
            ResponseEntity<RedisResponse> response = restTemplate.exchange(
                    redisBaseUrl + "/keys/*", HttpMethod.GET, buildAuthEntity(), RedisResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object result = response.getBody().getResult();
                if (result instanceof List<?> keys) {
                    @SuppressWarnings("unchecked")
                    List<String> keyList = (List<String>) keys;
                    logger.info("Successfully retrieved {} keys from Redis", keyList.size());
                    allKeysCache.put(ALL_KEYS_CACHE_KEY, keyList);
                    cacheTimestamps.put(ALL_KEYS_CACHE_KEY, System.currentTimeMillis());
                    return keyList;
                }
                logger.warn("Unexpected response format from Redis - result is not a list: {}", result);
            }
        } catch (HttpClientErrorException e) {
            logger.error("Client error fetching keys from Redis: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching keys from Redis: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            logger.error("Connection error fetching keys from Redis: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching keys from Redis", e);
        }

        return Collections.emptyList();
    }

    @SuppressWarnings("null")
    public GlossaryWord getKeyword(String title) {
        if (title == null || title.trim().isEmpty()) {
            logger.warn("Title parameter is null or empty");
            return null;
        }

        if (!isConfigured()) return null;

        if (isCacheValid(title)) {
            GlossaryWord cached = keywordCache.get(title);
            if (cached != null) {
                logger.debug("Returning cached keyword: {}", title);
                return cached;
            }
        }

        try {
            ResponseEntity<RedisResponse> response = restTemplate.exchange(
                    redisBaseUrl + "/get/" + title, HttpMethod.GET, buildAuthEntity(), RedisResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object result = response.getBody().getResult();
                if (result instanceof String rawResult && !rawResult.trim().isEmpty()) {
                    logger.info("Successfully retrieved keyword: {}", title);
                    GlossaryWord keyword = new GlossaryWord(title, rawResult);
                    keywordCache.put(title, keyword);
                    cacheTimestamps.put(title, System.currentTimeMillis());
                    return keyword;
                }
                logger.warn("Unexpected response format for keyword '{}': {}", title, result);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.info("Keyword not found in Redis: {}", title);
            } else {
                logger.error("Client error fetching keyword '{}': {} - {}", title, e.getStatusCode(), e.getResponseBodyAsString());
            }
        } catch (HttpServerErrorException e) {
            logger.error("Server error fetching keyword '{}': {} - {}", title, e.getStatusCode(), e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            logger.error("Connection error fetching keyword '{}': {}", title, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching keyword '{}'", title, e);
        }

        return null;
    }

    private boolean isConfigured() {
        if (redisBaseUrl == null || redisBaseUrl.trim().isEmpty()) {
            logger.warn("Redis base URL is not configured");
            return false;
        }
        if (bearerToken == null || bearerToken.trim().isEmpty()) {
            logger.warn("Redis bearer token is not configured");
            return false;
        }
        return true;
    }

    private boolean isCacheValid(String key) {
        Long timestamp = cacheTimestamps.get(key);
        if (timestamp == null) return false;
        return System.currentTimeMillis() - timestamp < TimeUnit.MINUTES.toMillis(CACHE_TTL_MINUTES);
    }

    private HttpEntity<Void> buildAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
}