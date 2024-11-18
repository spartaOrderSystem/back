package com.spartaordersystem.domains.review.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

public class GetReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID reviewId;
        private int star;
        private String content;
        private ZonedDateTime createdAt;
        private String userName; // 가게 리뷰에서는 유저 정보 저장
        private String storeName; // 유저 리뷰에서는 가게 정보 저장
    }

}
