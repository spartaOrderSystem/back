package com.spartaordersystem.domains.ai.entity;

import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_prompt")
@NoArgsConstructor
@AllArgsConstructor
public class Prompt extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true) // UUID 값이 36자의 문자열로 저장됨
    private UUID id;

    @Column(nullable = false)
    private String promptContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_menu_id", nullable = false)
    private StoreMenu storeMenu;

    @Builder
    public Prompt(String promptContent, String answer, StoreMenu storeMenu) {
        this.promptContent = promptContent;
        this.answer = answer;
        this.storeMenu = storeMenu;
    }
}
