package com.spartaordersystem.domains.storeMenu.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class GetMenuListDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID id;
        private String title;
        private String description;
        private long price;
    }
}
