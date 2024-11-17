package com.spartaordersystem.domains.payment.entity;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.payment.enums.PaymentFailStatus;
import com.spartaordersystem.domains.payment.enums.PaymentMethod;
import com.spartaordersystem.domains.payment.enums.PaymentStatus;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_payment")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // uuid 값이 36자의 문자열로 저장됨
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentFailStatus paymentFailStatus;

    @Column(nullable = false)
    private long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Builder
    public Payment(PaymentMethod paymentMethod, PaymentStatus paymentStatus, PaymentFailStatus paymentFailStatus, long totalPrice, Order order) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentFailStatus = paymentFailStatus;
        this.totalPrice = totalPrice;
        this.order = order;
    }

    public void failPayment(PaymentFailStatus paymentFailStatus) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.paymentFailStatus = paymentFailStatus;
    }

    public void successPayment() {
        this.paymentStatus = PaymentStatus.SUCCESS;
    }



}
