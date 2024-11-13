package com.spartaordersystem.domains.store.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class CreateStoreDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto {
        private String title;
        private String address;
        private ZonedDateTime openTime;
        private ZonedDateTime closeTime;
        private String phoneNumber;
        private String categoryName;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID id;
        private String title;
        private String address;
        private ZonedDateTime openTime;
        private ZonedDateTime closeTime;
        private String phoneNumber;
        private String categoryName;

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }


}
