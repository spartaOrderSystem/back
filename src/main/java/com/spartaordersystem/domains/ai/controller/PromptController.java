package com.spartaordersystem.domains.ai.controller;

import com.spartaordersystem.domains.ai.controller.dto.GetPromptDto;
import com.spartaordersystem.domains.ai.controller.dto.PromptDto;
import com.spartaordersystem.domains.ai.service.PromptService;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class PromptController {

    private final PromptService promptService;

    @PostMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<BaseResponse> updateDescription(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID menuId,
            @RequestBody PromptDto.RequestDto requestDto
    ) {
        PromptDto.ResponseDto responseDto = promptService.updateDescription(user, storeId, menuId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("메뉴 설명에 대한 답변 생성에 성공하였습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<BaseResponse> getPromptHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GetPromptDto.ResponseDto> responseDtoList = promptService.getPromptHistory(page, size);
        BaseResponse response = BaseResponse.toSuccessResponse("프롬프트 기록 조회", responseDtoList);
        return ResponseEntity.ok(response);
    }
}
