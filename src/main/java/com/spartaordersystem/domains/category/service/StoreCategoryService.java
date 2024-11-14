package com.spartaordersystem.domains.category.service;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.repository.StoreRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreCategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createStoreCategory(UUID storeId, User user, String categoryName) {

        checkUserRole(user.getRole().getAuthority());
        Store store = getStore(storeId);
        Category category = checkCategoryExists(categoryName);

        store.setCategory(category);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStoreCategory(
            UUID storeId, User user, String categoryName) {

        Store store = getStore(storeId);
        checkUserRole(user.getRole().getAuthority(), user, store);
        Category category = checkCategoryExists(categoryName);

        store.setCategory(category);
        storeRepository.save(store);
    }

    private void checkUserRole(String userRole) {
        if (!(userRole.equals(GlobalConst.ROLE_OWNER) || userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
    private void checkUserIsStoreOwner(User user, Store store) {
        if (!store.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkUserRole(String userRole, User user, Store store) {
        if (userRole.equals(GlobalConst.ROLE_OWNER)) {
            checkUserIsStoreOwner(user, store);
        }
        else if (!(userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private Category checkCategoryExists(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
