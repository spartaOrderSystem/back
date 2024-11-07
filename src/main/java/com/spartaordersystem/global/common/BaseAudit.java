package com.spartaordersystem.global.common;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.spartaordersystem.global.common.GlobalConst.TIME_ZONE_ID;

// TODO : 추가 반영 필요 사항 - deletedAt과 생성, 수정, 삭제 사용자 업데이트
/**
 * 생성시간, 수정시간 자동 저장 클래스
 */
@Getter
@MappedSuperclass
public class BaseAudit {
    protected ZonedDateTime createdAt;
    protected ZonedDateTime updatedAt;
    protected ZonedDateTime deletedAt;


    @PrePersist
    protected void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
        this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    }
}
