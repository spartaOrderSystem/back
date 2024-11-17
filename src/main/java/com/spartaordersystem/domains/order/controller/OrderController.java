package com.spartaordersystem.domains.order.controller;

import com.spartaordersystem.domains.order.controller.dto.CreateOrderDto;
import com.spartaordersystem.domains.order.controller.dto.GetOrderInfoByOwnerDto;
import com.spartaordersystem.domains.order.controller.dto.GetOrderInfoDto;
import com.spartaordersystem.domains.order.service.OrderService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/stores/{storeId}/orders")
    public ResponseEntity<BaseResponse> createOrder(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody CreateOrderDto.RequestDto requestDto
    ) {
        CreateOrderDto.ResponseDto responseDto = orderService.createOrder(user, storeId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("주문이 생성되었습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<BaseResponse> getOrderInfo(
            @AuthenticationPrincipal User user,
            @PathVariable UUID orderId
    ) {
        GetOrderInfoDto.ResponseDto responseDto = orderService.getOrderInfo(user, orderId);
        BaseResponse response = BaseResponse.toSuccessResponse("주문 단건 조회 성공", responseDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/stores/{storeId}/orders/{orderId}")
    public ResponseEntity<BaseResponse> getOrderInfoByOwner(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID orderId
    ) {
        GetOrderInfoByOwnerDto.ResponseDto responseDto = orderService.getOrderInfoByOwner(user, storeId, orderId);
        BaseResponse response = BaseResponse.toSuccessResponse("주문 단건 조회 성공", responseDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/stores/{storeId}/orders/{orderId}/status")
    public ResponseEntity<BaseResponse> updateStoreStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID orderId
    ) {
        orderService.updateStoreStatus(user, storeId, orderId);
        BaseResponse response = BaseResponse.toSuccessResponse("주문 상태 정보가 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/users/{userId}/orders")
//    public ResponseEntity<BaseResponse> getMyOrderList(
//            @AuthenticationPrincipal User user,
//            @PathVariable Long userId
//    ) {
//         totalPrice를 각 주문마다 리턴해줘야하는데 지금 로직으로는
//            매 주문에서 직접 계산해줘야하므로 성능이 떨어진다.
//          그래서 결제를 구현하고 결제에 저장된 총 금액을 각 주문마다 리턴해서 보여주는것이 훨씬 나을 것 같다
//    }
}
