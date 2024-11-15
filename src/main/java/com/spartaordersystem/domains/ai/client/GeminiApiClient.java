package com.spartaordersystem.domains.ai.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 *  글로벌로 옮기고
 *  인터페이스로 변경
 *  ai 패키지에서는
 *  impl을 만들어서 상속받게
 *  하는게 좋다
 *  리팩토링
 */
@Slf4j
@Component
public class GeminiApiClient {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    public String createDescription(String title, long price, String details) {
        String prompt = String.format("제목: %s, 가격: %d. %s. 답변은 최대한 간결하게 50자 이내로 해줘", title, price, details);

        String requestBody = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", prompt
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(GEMINI_API_URL + "?key=" + apiKey, HttpMethod.POST, request, String.class);
//        return response.getBody();
        try {
            CandidatesDto candidatesDto = objectMapper.readValue(response.getBody(), CandidatesDto.class);
            return candidatesDto.getCandidates().get(0).getContent().getParts().get(0).getText();
        } catch (Exception e) {
            log.error("파싱에러남", e);
            return "파싱 에러";
        }
    }


    public static class requestDto {

        private UUID menuid;
        private String promptContent;

    }

    public static class data {
        private UUID menuid;
        private String description;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CandidatesDto {
        private List<ContentsDto> candidates;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentsDto {
        private ContentDto content;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentDto {
        private List<PartsDto> parts;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PartsDto {
        private String text;
    }
}

