package com.spartaordersystem.domains.category.entity;

import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.global.common.BaseAudit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Table(name = "p_category")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseAudit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, length = 36, unique = true)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @OneToMany(mappedBy = "category")
    private List<Store> storeList = new ArrayList<>();

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(String username) {
        this.deletedBy = username;
        this.deletedAt = ZonedDateTime.now();
        this.isDeleted = true;
    }

    /**
     *  테스트용 임시 코드
     */
//    public void setCreatedBy(String username) {
//        this.createdBy = username;
//    }
}
