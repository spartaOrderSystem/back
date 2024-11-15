package com.spartaordersystem.domains.order_menu.entity;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_order_menu")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderMenu {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // uuid 값이 36자의 문자열로 저장됨
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_menu_id", nullable = false)
    private StoreMenu menu;

    @Builder
    public OrderMenu(int quantity, long price, Order order, StoreMenu menu) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.menu = menu;
    }

}
