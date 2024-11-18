package com.spartaordersystem.domains.payment.controller;

import com.spartaordersystem.domains.payment.controller.dto.CreatePaymentDto;
import com.spartaordersystem.domains.payment.controller.dto.GetPaymentDto;
import com.spartaordersystem.domains.payment.controller.dto.UpdatePaymentDto;
import com.spartaordersystem.domains.payment.service.PaymentService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<BaseResponse> createPayment(
            @AuthenticationPrincipal User user,
            @RequestBody CreatePaymentDto.RequestDto requestDto
    ) {
        CreatePaymentDto.ResponseDto responseDto = paymentService.createPayment(user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("결제가 생성되었습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<BaseResponse> getPayment(
            @AuthenticationPrincipal User user,
            @PathVariable UUID paymentId
    ) {
        GetPaymentDto.ResponseDto responseDto = paymentService.getPayment(user, paymentId);
        BaseResponse response = BaseResponse.toSuccessResponse("결제를 조회하였습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<BaseResponse> updatePaymentStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID paymentId,
            @RequestBody UpdatePaymentDto.RequestDto requestDto
    ) {
        UpdatePaymentDto.ResponseDto responseDto = paymentService.updatePaymentStatus(user, paymentId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("결제 상태가 변경되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }
}
