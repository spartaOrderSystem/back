package com.spartaordersystem.domains.store.controller;

import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.store.service.StoreService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<BaseResponse> createStore(
            @AuthenticationPrincipal User user,
            @RequestBody CreateStoreDto.RequestDto requestDto
    ) {
        String userRole = user.getRole().getAuthority();

        CreateStoreDto.ResponseDto responseDto = storeService.createStore(user, userRole, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 생성이 완료되었습니다", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<BaseResponse> updateStore(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody UpdateStoreDto.RequestDto requestDto
            ) {
        String userRole = user.getRole().getAuthority();

        UpdateStoreDto.ResponseDto responseDto = storeService.updateStore(storeId, user, userRole, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 정보 수정이 완료되었습니다", responseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<BaseResponse> deleteStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal User user
    ) {

        storeService.deleteStore(storeId, user);
        return ResponseEntity.ok(BaseResponse.toSuccessResponse("가게가 삭제되었습니다."));
    }
}
