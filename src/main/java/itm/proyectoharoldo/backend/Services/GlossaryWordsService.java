package itm.proyectoharoldo.backend.Services;

import itm.proyectoharoldo.backend.Models.GlossaryWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class GlossaryWordsService {

    private static final Logger logger = LoggerFactory.getLogger(GlossaryWordsService.class);
    
    private final RestTemplate restTemplate;
    
    @Value("${redis.base.url:https://handy-lemming-28224.upstash.io}")
    private String redisBaseUrl;
    
    @Value("${redis.bearer.token:}")
    private String bearerToken;
    
    // In-memory cache for keywords
    private final Map<String, GlossaryWord> keywordCache = new ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private final Map<String, List<String>> allKeysCache = new ConcurrentHashMap<>();
    private final Map<String, Long> allKeysCacheTimestamp = new ConcurrentHashMap<>();
    
    // Cache TTL: 30 minutes
    private static final long CACHE_TTL_MINUTES = 30;
    
    public GlossaryWordsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .build();
    }
    
    private boolean isCacheValid(String cacheKey, Map<String, Long> timestampCache) {
        Long timestamp = timestampCache.get(cacheKey);
        if (timestamp == null) return false;
        
        long currentTime = System.currentTimeMillis();
        long cacheAge = currentTime - timestamp;
        return cacheAge < TimeUnit.MINUTES.toMillis(CACHE_TTL_MINUTES);
    }
    
    private void updateCacheTimestamp(String cacheKey, Map<String, Long> timestampCache) {
        timestampCache.put(cacheKey, System.currentTimeMillis());
    }

    public List<String> getAllKeys(){
        if (redisBaseUrl == null || redisBaseUrl.trim().isEmpty()) {
            logger.warn("Redis base URL is not configured");
            return Collections.emptyList();
        }
        
        if (bearerToken == null || bearerToken.trim().isEmpty()) {
            logger.warn("Redis bearer token is not configured");
            return Collections.emptyList();
        }

        // Check cache first
        String cacheKey = "all_keys";
        if (isCacheValid(cacheKey, allKeysCacheTimestamp)) {
            List<String> cachedKeys = allKeysCache.get(cacheKey);
            if (cachedKeys != null) {
                logger.debug("Returning cached keys: {} items", cachedKeys.size());
                return cachedKeys;
            }
        }

        String url = redisBaseUrl + "/keys/*";
        logger.debug("Fetching all keys from Redis: {}", url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, 
                (Class<Map<String, Object>>) (Class<?>) Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body != null) {
                    Object result = body.get("result");
                    
                    if (result instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<String> keys = (List<String>) result;
                        logger.info("Successfully retrieved {} keys from Redis", keys.size());
                        
                        // Cache the result
                        allKeysCache.put(cacheKey, keys);
                        updateCacheTimestamp(cacheKey, allKeysCacheTimestamp);
                        
                        return keys;
                    } else {
                        logger.warn("Unexpected response format from Redis - result is not a list: {}", result);
                        return Collections.emptyList();
                    }
                } else {
                    logger.warn("Response body is null from Redis");
                    return Collections.emptyList();
                }
            } else {
                logger.warn("Unexpected response from Redis - Status: {}, Body: {}", 
                           response.getStatusCode(), response.getBody());
                return Collections.emptyList();
            }

        } catch (HttpClientErrorException e) {
            logger.error("Client error when fetching keys from Redis: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            logger.error("Server error when fetching keys from Redis: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            logger.error("Connection error when fetching keys from Redis: {}", e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error when fetching keys from Redis", e);
            return Collections.emptyList();
        }
    }

    public GlossaryWord getKeyword(String title) {
        if (title == null || title.trim().isEmpty()) {
            logger.warn("Title parameter is null or empty");
            return null;
        }
        
        if (redisBaseUrl == null || redisBaseUrl.trim().isEmpty()) {
            logger.warn("Redis base URL is not configured");
            return null;
        }
        
        if (bearerToken == null || bearerToken.trim().isEmpty()) {
            logger.warn("Redis bearer token is not configured");
            return null;
        }

        // Check cache first
        if (isCacheValid(title, cacheTimestamps)) {
            GlossaryWord cachedKeyword = keywordCache.get(title);
            if (cachedKeyword != null) {
                logger.debug("Returning cached keyword: {}", title);
                return cachedKeyword;
            }
        }

        String url = redisBaseUrl + "/get/" + title;
        logger.debug("Fetching keyword from Redis: {}", url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, 
                (Class<Map<String, Object>>) (Class<?>) Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body != null) {
                    Object result = body.get("result");
                    
                    if (result instanceof String) {
                        String rawResult = (String) result;
                        if (rawResult.trim().isEmpty()) {
                            logger.warn("Empty result for keyword: {}", title);
                            return null;
                        }
                        logger.info("Successfully retrieved keyword: {}", title);
                        
                        // Cache the result
                        GlossaryWord keyword = new GlossaryWord(title, rawResult);
                        keywordCache.put(title, keyword);
                        updateCacheTimestamp(title, cacheTimestamps);
                        
                        return keyword;
                    } else {
                        logger.warn("Unexpected response format for keyword '{}' - result is not a string: {}", title, result);
                        return null;
                    }
                } else {
                    logger.warn("Response body is null for keyword: {}", title);
                    return null;
                }
            } else {
                logger.warn("Unexpected response for keyword '{}' - Status: {}, Body: {}", 
                           title, response.getStatusCode(), response.getBody());
                return null;
            }

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.info("Keyword not found in Redis: {}", title);
            } else {
                logger.error("Client error when fetching keyword '{}' from Redis: {} - {}", 
                           title, e.getStatusCode(), e.getResponseBodyAsString());
            }
            return null;
        } catch (HttpServerErrorException e) {
            logger.error("Server error when fetching keyword '{}' from Redis: {} - {}", 
                       title, e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (ResourceAccessException e) {
            logger.error("Connection error when fetching keyword '{}' from Redis: {}", title, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error when fetching keyword '{}' from Redis", title, e);
            return null;
        }
    }

}
