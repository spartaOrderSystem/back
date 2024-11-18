package com.spartaordersystem.domains.payment.enums;

public enum PaymentFailStatus {
    CARD_EXPIRED,
    NETWORK_ERROR,
    INVALID_PAYMENT_METHOD,   // 유효하지 않은 결제 방식
    LACK_OF_BALANCE,  // 잔액 부족
    CANCELLED_BY_USER  // 유저가 취소
}
