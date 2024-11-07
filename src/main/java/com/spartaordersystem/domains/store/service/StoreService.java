package com.spartaordersystem.domains.store.service;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CreateStoreDto.ResponseDto createStore(Long userId, String userRole, CreateStoreDto.RequestDto requestDto) {
        User user = checkUser(userId);
        checkUserRole(userRole);
        Category category = getCategory(requestDto);

        String categoryName = category.getName().name();

        Store store = Store.builder()
                .title(requestDto.getTitle())
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .build();

        storeRepository.save(store);

        return CreateStoreDto.ResponseDto.builder()
                .id(store.getId())
                .title(store.getTitle())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .phoneNumber(store.getPhoneNumber())
                .categoryName(categoryName)
                .build();
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private static void checkUserRole(String userRole) {
        if (!(userRole.equals("OWNER") || userRole.equals("MANEGER") || userRole.equals("ADMIN"))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Category getCategory(CreateStoreDto.RequestDto requestDto) {
        return categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
