package com.spartaordersystem.domains.category.controller;

import com.spartaordersystem.domains.category.controller.dto.CategoryDto;
import com.spartaordersystem.domains.category.service.CategoryService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CategoryDto.CreateCategoryRequestDto requestDto
    ) {
        CategoryDto.CreateCategoryResponseDto responseDto = categoryService.createCategory(user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("카테고리가 생성되었습니다", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<BaseResponse> updateCategory(
            @PathVariable UUID categoryId,
            @AuthenticationPrincipal User user,
            @RequestBody CategoryDto.UpdateCategoryRequestDto requestDto
    ) {
        CategoryDto.UpdateCategoryResponseDto responseDto = categoryService.updateCategory(categoryId, user, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse(" 카테고리 정보가 수정되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<BaseResponse> deleteCategory(
            @PathVariable UUID categoryId,
            @AuthenticationPrincipal User user
    ) {
        categoryService.deleteCategory(categoryId, user);
        BaseResponse response = BaseResponse.toSuccessResponse("카테고리가 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getCategoryList() {
        List<CategoryDto.GetCategoryResponseDto> responseDtoList = categoryService.getCategoryList();
        BaseResponse response = BaseResponse.toSuccessResponse("카테고리 전체 조회", responseDtoList);
        return ResponseEntity.ok(response);
    }

}
