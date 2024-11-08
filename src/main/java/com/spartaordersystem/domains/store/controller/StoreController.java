package com.spartaordersystem.domains.store.controller;

import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.service.StoreService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Long userId = user.getId();
        String userRole = user.getRole().getAuthority();

        CreateStoreDto.ResponseDto responseDto = storeService.createStore(userId, userRole, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 생성이 완료되었습니다", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);



    }


}
