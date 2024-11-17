package com.spartaordersystem.domains.order.controller.dto;

import com.spartaordersystem.domains.order.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class GetOrderInfoDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDto {
        private UUID orderId;
        private OrderType orderType;
        private String storeRequest;
        private String riderRequest;
        private List<GetOrderInfoDto.OrderMenuResponse> orderMenuResponseList;
        private long totalPrice;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderMenuResponse {
        private UUID menuId;
        private String menuName;
        private int quantity;
        private long price;
    }

}
