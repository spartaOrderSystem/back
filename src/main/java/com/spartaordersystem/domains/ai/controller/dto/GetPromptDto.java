package com.spartaordersystem.domains.ai.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

public class GetPromptDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID id;
        private String promptContent;
        private String answer;
        private ZonedDateTime createdAt;
    }
}
