package com.spartaordersystem.domains.store.service;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.store.controller.dto.CreateStoreDto;
import com.spartaordersystem.domains.store.controller.dto.UpdateStoreDto;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CreateStoreDto.ResponseDto createStore(User user, String userRole, CreateStoreDto.RequestDto requestDto) {
        checkUser(user);
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

    public UpdateStoreDto.ResponseDto updateStore(UUID storeId, User user, String userRole, UpdateStoreDto.RequestDto requestDto) {
        checkUser(user);
        checkUserRole(userRole);
        Store store = getStore(storeId);
        checkUserIsStoreOwner(user, store);

        Category category = getCategory(requestDto);

        store = Store.builder()
                .title(requestDto.getTitle())
                .address(requestDto.getAddress())
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .phoneNumber(requestDto.getPhoneNumber())
                .category(category)
                .build();

        storeRepository.save(store);

        return UpdateStoreDto.ResponseDto.builder()
                .id(storeId)
                .title(store.getTitle())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .phoneNumber(store.getPhoneNumber())
                .categoryName(category.getName().name())
                .build();
    }

    public void deleteStore(UUID storeId, User user) {
        checkUser(user);
        checkUserRole(user.getRole().getAuthority());
        Store store = getStore(storeId);
        checkUserIsStoreOwner(user, store);

        storeRepository.delete(store);
    }

    private static void checkUserIsStoreOwner(User user, Store store) {
        if (!store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Category getCategory(UpdateStoreDto.RequestDto requestDto) {
        return categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void checkUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private static void checkUserRole(String userRole) {
        if (!(userRole.equals("ROLE_OWNER") || userRole.equals("ROLE_MANEGER") || userRole.equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Category getCategory(CreateStoreDto.RequestDto requestDto) {
        return categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
