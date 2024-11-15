package com.spartaordersystem.domains.UserAddress.entity;

import com.spartaordersystem.domains.UserAddress.controller.dto.UpdateUserAddressDto;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_user_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAddress extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // uuid 값이 36자의 문자열로 저장됨
    private UUID id;

    @Column(nullable = false)
    private String address;

    @Column
    private String detailAddress;

    @Column
    private String storeRequest;

    @Column
    private String riderRequest;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @OneToOne(mappedBy = "userAddress")
    private User user;

    @Builder
    public UserAddress(String address, String detailAddress, String storeRequest, String riderRequest, User user) {
        this.address = address;
        this.detailAddress = detailAddress;
        this.storeRequest = storeRequest;
        this.riderRequest = riderRequest;
        this.user = user;
    }

    public void setDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = ZonedDateTime.now();
        this.isDeleted = true;
    }

//    @Transactional
//    public void updateUserAddress(UpdateUserAddressDto.RequestDto requestDto) {
//        this.address = Optional.ofNullable(requestDto.getAddress()).orElse(this.address);
//        this.detailAddress = Optional.ofNullable(requestDto.getDetailAddress()).orElse(this.detailAddress);
//        this.storeRequest = Optional.ofNullable(requestDto.getStoreRequest()).orElse(this.storeRequest);
//        this.riderRequest = Optional.ofNullable(requestDto.getRiderRequest()).orElse(this.riderRequest);
//    }
}
