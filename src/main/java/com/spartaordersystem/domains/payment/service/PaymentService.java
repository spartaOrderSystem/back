package com.spartaordersystem.domains.payment.service;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.order.repository.OrderRepository;
import com.spartaordersystem.domains.payment.controller.dto.CreatePaymentDto;
import com.spartaordersystem.domains.payment.controller.dto.GetPaymentDto;
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
