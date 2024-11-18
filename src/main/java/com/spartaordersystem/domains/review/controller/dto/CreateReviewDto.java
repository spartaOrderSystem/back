package com.spartaordersystem.domains.review.controller.dto;

import com.spartaordersystem.domains.payment.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CreateReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto {
        private UUID orderId;
        private int star;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID reviewId;
        private int star;
        private String content;
        private ZonedDateTime createdAt;
    }
}
