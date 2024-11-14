package com.spartaordersystem.domains.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *  글로벌로 옮기고
 *  인터페이스로 변경
 *  ai 패키지에서는
 *  impl을 만들어서 상속받게
 *  하는게 좋다
 *  리팩토링
 */
@Component
public class GeminiApiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    public String createDescription(String title, long price, String details) {
        String prompt = String.format("제목: %s, 가격: %d. %s. 답변은 최대한 간결하게 50자 이내로 해줘", title, price, details);

        String requestBody = String.format(
                "{\"prompt\": \"%s\", \"maxTokens\":50, \"temperature\": 0.8}", prompt
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", "Bearer" + apiKey);

        HttpEntity<String> request = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(GEMINI_API_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }
}
