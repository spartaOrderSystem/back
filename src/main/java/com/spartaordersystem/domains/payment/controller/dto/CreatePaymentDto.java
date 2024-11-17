package com.spartaordersystem.domains.payment.controller.dto;

import com.spartaordersystem.domains.payment.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CreatePaymentDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto {
        private UUID orderId;
        private PaymentMethod paymentMethod;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID orderId;
        private UUID paymentId;
        private long totalPrice;
        private PaymentMethod paymentMethod;
        private ZonedDateTime createdAt;
    }
}
