package com.spartaordersystem.domains.review.service;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.order.repository.OrderRepository;
import com.spartaordersystem.domains.order_menu.repository.OrderMenuRepository;
import com.spartaordersystem.domains.review.controller.dto.CreateReviewDto;
import com.spartaordersystem.domains.review.entity.Review;
import com.spartaordersystem.domains.review.repository.ReviewRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public CreateReviewDto.ResponseDto createReview(User user, UUID storeId, CreateReviewDto.RequestDto requestDto) {
        Order order = getOrder(requestDto.getOrderId());
        Store store = orderMenuRepository.findByOrder(order).stream()
                .findFirst()
                .map(orderMenu -> orderMenu.getMenu().getStore())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_MATCH_STORE));

        if (!store.getId().equals(storeId)) {
            throw new CustomException(ErrorCode.MISMATCH);
        }

        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        checkReviewAlreadyExists(order);

        Review review = Review.builder()
                .star(requestDto.getStar())
                .content(requestDto.getContent())
                .user(user)
                .store(store)
                .order(order)
                .build();

        reviewRepository.save(review);

        store.updateAvgStar(review.getStar(), true);

        return CreateReviewDto.ResponseDto.builder()
                .reviewId(review.getId())
                .star(review.getStar())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private void checkReviewAlreadyExists(Order order) {
        reviewRepository.existsByOrder(order)
                .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_EXISTS_REVIEW));
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }
}
