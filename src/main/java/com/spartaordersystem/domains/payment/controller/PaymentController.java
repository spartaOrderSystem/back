package com.spartaordersystem.domains.payment.controller;

import com.spartaordersystem.domains.payment.controller.dto.CreatePaymentDto;
import com.spartaordersystem.domains.payment.service.PaymentService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
