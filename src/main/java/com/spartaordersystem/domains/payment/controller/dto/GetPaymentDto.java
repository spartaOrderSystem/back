package com.spartaordersystem.domains.payment.controller.dto;

import com.spartaordersystem.domains.payment.enums.PaymentFailStatus;
import com.spartaordersystem.domains.payment.enums.PaymentMethod;
import com.spartaordersystem.domains.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

public class GetPaymentDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID orderId;
        private UUID paymentId;
        private long totalPrice;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private PaymentFailStatus paymentFailStatus;
        private ZonedDateTime createdAt;
    }
}
