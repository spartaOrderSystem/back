package com.spartaordersystem.domains.storeMenu.controller;

import com.spartaordersystem.domains.storeMenu.controller.dto.CreateMenuDto;
import com.spartaordersystem.domains.storeMenu.controller.dto.GetMenuDto;
import com.spartaordersystem.domains.storeMenu.controller.dto.UpdateMenuDto;
import com.spartaordersystem.domains.storeMenu.service.MenuService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PatchMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<BaseResponse> updateMenu(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID menuId,
            @RequestBody UpdateMenuDto.RequestDto requestDto
    ) {
        UpdateMenuDto.ResponseDto responseDto = menuService.updateMenu(user, storeId, menuId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("메뉴 정보가 수정되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<BaseResponse> deleteMenu(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID menuId
    ) {
        menuService.deleteMenu(user, storeId, menuId);
        return ResponseEntity.ok(BaseResponse.toSuccessResponse("메뉴가 삭제되었습니다"));
    }

    @PatchMapping("/{storeId}/menus/{menuId}/hide")
    public ResponseEntity<BaseResponse> updateMenuStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID menuId
    ) {
        menuService.updateMenuStatus(user, storeId, menuId);
        BaseResponse response = BaseResponse.toSuccessResponse("메뉴 상태가 변경되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}/menus/{menuId}")
    public ResponseEntity<BaseResponse> getMenuInfo(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId
    ) {
        GetMenuDto.ResponseDto responseDto = menuService.getMenuInfo(storeId, menuId);
        BaseResponse response = BaseResponse.toSuccessResponse("메뉴 정보 조회 성공", responseDto);
        return ResponseEntity.ok(response);
    }


}
