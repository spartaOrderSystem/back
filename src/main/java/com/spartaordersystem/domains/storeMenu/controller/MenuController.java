package com.spartaordersystem.domains.storeMenu.controller;

import com.spartaordersystem.domains.storeMenu.controller.dto.CreateMenuDto;
import com.spartaordersystem.domains.storeMenu.service.MenuService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{storeId}/menus")
    public ResponseEntity<BaseResponse> createMenu(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody CreateMenuDto.RequestDto requestDto
    ) {
        CreateMenuDto.ResponseDto responseDto = menuService.createMenu(user, storeId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("메뉴가 생성되었습니다", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
