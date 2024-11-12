package com.spartaordersystem.domains.category.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CategoryDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateCategoryRequestDto {
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateCategoryResponseDto {
        private UUID categoryId;
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateCategoryRequestDto {
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateCategoryResponseDto {
        private UUID categoryId;
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetCategoryResponseDto {
        private UUID categoryId;
        private String categoryName;
    }

}
