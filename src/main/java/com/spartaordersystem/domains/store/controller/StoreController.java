package com.spartaordersystem.domains.store.controller;

import com.spartaordersystem.domains.category.service.StoreCategoryService;
import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.controller.dto.GetStoreDto;
import com.spartaordersystem.domains.store.controller.dto.SearchStoreDto;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.store.service.StoreService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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
        CreateStoreDto.ResponseDto responseDto = storeService.createStore(user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 생성이 완료되었습니다", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<BaseResponse> updateStore(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody UpdateStoreDto.RequestDto requestDto
            ) {
        UpdateStoreDto.ResponseDto responseDto = storeService.updateStore(storeId, user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 정보 수정이 완료되었습니다", responseDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{storeId}/status")
    public ResponseEntity<BaseResponse> updateStoreStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId
    ) {
        storeService.updateStoreStatus(user, storeId);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 상태정보가 수정되었습니다.");
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

    @GetMapping("/{storeId}")
    public ResponseEntity<BaseResponse> getStoreInfo(
            @PathVariable UUID storeId
    ) {
        log.info("StoreController > getStoreInfo");
        GetStoreDto.ResponseDto responseDto = storeService.getStoreInfo(storeId);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 정보를 조회에 성공하였습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> searchStore(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "3") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String storeStatus) {
        log.info("StoreController > searchStore title : {}, categoryId : {}, storeStatus : {}", title, categoryId, storeStatus);
        List<SearchStoreDto.ResponseDto> responseDtos = storeService.searchStore(page, size, title, categoryId, storeStatus);
        BaseResponse response = BaseResponse.toSuccessResponse("가게 검색에 성공하였습니다.", responseDtos);
        return ResponseEntity.ok(response);
    }

}
