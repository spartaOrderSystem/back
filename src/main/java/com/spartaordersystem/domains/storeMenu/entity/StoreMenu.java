package com.spartaordersystem.domains.storeMenu.entity;

import com.spartaordersystem.domains.ai.entity.Prompt;
import com.spartaordersystem.domains.storeMenu.controller.dto.UpdateMenuDto;
import com.spartaordersystem.domains.storeMenu.enums.MenuStatus;
import com.spartaordersystem.domains.store.entity.Store;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_menu")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StoreMenu extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // uuid 값이 36자의 문자열로 저장됨
    private UUID id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(mappedBy = "storeMenu", fetch = FetchType.LAZY)
    private Prompt prompt;

    @Builder
    public StoreMenu(String title, String description, long price, MenuStatus menuStatus, Store store, Prompt prompt) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.menuStatus = menuStatus;
        this.store = store;
        this.prompt = prompt;
    }

    @Transactional
    public void updateMenu(UpdateMenuDto.RequestDto requestDto) {
        this.title = Optional.ofNullable(requestDto.getTitle()).orElse(this.title);
        this.description = Optional.ofNullable(requestDto.getDescription()).orElse(this.description);
        this.price = Optional.of(requestDto.getPrice()).orElse(this.price);
    }

    public void setDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = ZonedDateTime.now();
        this.isDeleted = true;
    }

    public void setMenuStatus(MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
