package com.spartaordersystem.domains.order.controller.dto;

import com.spartaordersystem.domains.order.enums.OrderStatus;
import com.spartaordersystem.domains.order.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class CreateOrderDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto {
        private OrderStatus orderStatus;
        private OrderType orderType;
        private List<OrderMenuRequest> orderMenuRequestList;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderMenuRequest {
        private UUID menuId;
        private int quantity;
        private int price;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID orderId;
        private UUID storeId;
        private OrderType orderType;
        private String storeRequest;
        private String riderRequest;
        private List<OrderMenuResponse> orderMenuResponseList;
        private long totalPrice;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderMenuResponse {
        private UUID menuId;
        private int quantity;
        private long price;
    }
}
