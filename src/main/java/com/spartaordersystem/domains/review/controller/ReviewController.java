package com.spartaordersystem.domains.review.controller;

import com.spartaordersystem.domains.review.controller.dto.CreateReviewDto;
import com.spartaordersystem.domains.review.controller.dto.UpdateReviewDto;
import com.spartaordersystem.domains.review.service.ReviewService;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/stores/{storeId}/reviews")
    public ResponseEntity<BaseResponse> createReview(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @RequestBody CreateReviewDto.RequestDto requestDto
    ) {
        CreateReviewDto.ResponseDto responseDto = reviewService.createReview(user, storeId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("리뷰 생성에 성공하였습니다.", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<BaseResponse> updateReview(
            @AuthenticationPrincipal User user,
            @PathVariable UUID storeId,
            @PathVariable UUID reviewId,
            @RequestBody UpdateReviewDto.RequestDto requestDto
    ) {
        UpdateReviewDto.ResponseDto responseDto = reviewService.updateReview(user, storeId, reviewId, requestDto);
        BaseResponse response = BaseResponse.toSuccessResponse("리뷰가 수정되었습니다.", responseDto);
        return ResponseEntity.ok(response);
    }
}
