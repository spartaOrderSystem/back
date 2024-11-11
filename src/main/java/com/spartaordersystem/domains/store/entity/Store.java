package com.spartaordersystem.domains.store.entity;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.store.enums.StoreStatus;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_store")
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
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
    private StoreStatus storeStatus = StoreStatus.CLOSE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updated_by;

    @Column(name = "deleted_by")
    private String deletedBy;




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

    /**
     *  삭제 시 호출
     *
     *  서비스레이어에선 userId로 username을 받아올 수 있으니 파라미터를 변경해도 될듯
     */
    public void setDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = ZonedDateTime.now();
    }
}
