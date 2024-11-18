package com.spartaordersystem.domains.payment.service;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.order.enums.OrderStatus;
import com.spartaordersystem.domains.order.repository.OrderRepository;
import com.spartaordersystem.domains.payment.controller.dto.CreatePaymentDto;
import com.spartaordersystem.domains.payment.controller.dto.GetPaymentDto;
import com.spartaordersystem.domains.payment.controller.dto.UpdatePaymentDto;
import com.spartaordersystem.domains.payment.entity.Payment;
import com.spartaordersystem.domains.payment.enums.PaymentStatus;
import com.spartaordersystem.domains.payment.repository.PaymentRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public CreatePaymentDto.ResponseDto createPayment(User user, CreatePaymentDto.RequestDto requestDto) {
        Order order = getOrder(requestDto.getOrderId());

        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        long totalPrice = order.getTotalPrice();

        Payment payment = Payment.builder()
                .paymentMethod(requestDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .totalPrice(totalPrice)
                .order(order)
                .build();

        paymentRepository.save(payment);

        return CreatePaymentDto.ResponseDto.builder()
                .orderId(order.getId())
                .paymentId(payment.getId())
                .totalPrice(payment.getTotalPrice())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public GetPaymentDto.ResponseDto getPayment(User user, UUID paymentId) {
        checkUserRole(user.getRole().getAuthority());
        Payment payment = getPayment(paymentId);

        return GetPaymentDto.ResponseDto.builder()
                .orderId(payment.getOrder().getId())
                .paymentId(payment.getId())
                .totalPrice(payment.getTotalPrice())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .paymentFailStatus(payment.getPaymentFailStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    @Transactional
    public UpdatePaymentDto.ResponseDto updatePaymentStatus(User user, UUID paymentId, UpdatePaymentDto.RequestDto requestDto) {
        checkUserRole(user.getRole().getAuthority());  // 외부 API연동이라 가정했기 때문에 이후엔 권한 검증 생략

        Payment payment = getPayment(paymentId);

        if (!payment.getOrder().getId().equals(requestDto.getOrderId())) {
            throw new CustomException(ErrorCode.MISMATCH);
        }

        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new CustomException(ErrorCode.CAN_NOT_UPDATE_PAYMENT_STATUS);
        }

        PaymentStatus updatedStatus = randomPaymentStatus();

        if (updatedStatus == PaymentStatus.FAILED && requestDto.getPaymentFailStatus() == null) {
            throw new CustomException(ErrorCode.REQUIRED_FAILURE_REASON);
        }

        if (updatedStatus == PaymentStatus.FAILED) {
            payment.failPayment(requestDto.getPaymentFailStatus());
        } else {
            payment.successPayment();
        }

        paymentRepository.save(payment);

        return UpdatePaymentDto.ResponseDto.builder()
                .orderId(payment.getOrder().getId())
                .paymentId(payment.getId())
                .paymentStatus(payment.getPaymentStatus())
                .paymentFailStatus(payment.getPaymentFailStatus())
                .build();
    }

    public PaymentStatus randomPaymentStatus() {
        boolean successProbability = Math.random() < 0.9;

        if (successProbability) {
            return PaymentStatus.SUCCESS;
        } else {
            return PaymentStatus.FAILED;
        }
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private void checkUserRole(String userRole) {
        if (!(userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
