package com.spartaordersystem.domains.store.entity;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.store.enums.StoreStatus;
import com.spartaordersystem.domains.user.entity.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_store")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Store extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // uuid 값이 36자의 문자열로 저장됨
    private UUID id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime openTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime closeTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;





    @Builder
    public Store(User user, String title, String address, String phoneNumber, ZonedDateTime openTime, ZonedDateTime closeTime, StoreStatus storeStatus, Category category) {
        this.user = user;
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.storeStatus = storeStatus;
        this.category = category;
    }

    @Transactional
    public void updateStore(UpdateStoreDto.RequestDto requestDto) {
        this.title = Optional.ofNullable(requestDto.getTitle()).orElse(this.title);
        this.address = Optional.ofNullable(requestDto.getAddress()).orElse(this.address);
        this.phoneNumber = Optional.ofNullable(requestDto.getPhoneNumber()).orElse(this.phoneNumber);
        this.openTime = Optional.ofNullable(requestDto.getOpenTime()).orElse(this.openTime);
        this.closeTime = Optional.ofNullable(requestDto.getCloseTime()).orElse(this.closeTime);
    }

    /**
     * 테스트용 메서드
     */
    public void updateTitle(String title) {
        this.title = title;
    }
    /**
     *  삭제 시 호출
     */
    public void setDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = ZonedDateTime.now();
        this.isDeleted = true;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }
}
