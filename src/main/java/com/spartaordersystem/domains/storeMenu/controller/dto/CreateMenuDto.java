package com.spartaordersystem.domains.storeMenu.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateMenuDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto {
        private String title;
        private String description;
        private long price;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private String title;
        private String description;
        private long price;
    }

}
