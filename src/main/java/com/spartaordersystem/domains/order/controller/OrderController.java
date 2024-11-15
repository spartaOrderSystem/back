package com.spartaordersystem.domains.order.controller;

import com.spartaordersystem.domains.order.controller.dto.CreateOrderDto;
import com.spartaordersystem.domains.order.service.OrderService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BaseResponse> createOrder(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody CreateOrderDto.RequestDto requestDto
    ) {
        CreateOrderDto.ResponseDto responseDto = orderService.createOrder(user, storeId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("주문이 생성되었습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
