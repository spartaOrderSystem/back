package com.spartaordersystem.domains.store.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
public class GetStoreDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private String title;
        private String address;
        private ZonedDateTime openTime;
        private ZonedDateTime closeTime;
        private String phoneNumber;
        private String categoryName;
    }
}
