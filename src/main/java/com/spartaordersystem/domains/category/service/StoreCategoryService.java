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
    public Store createStoreCategory(UUID storeId, String categoryName) {
        Store store = getStore(storeId);
        Category category = checkCategoryExists(categoryName);

        store.setCategory(category);
        return storeRepository.save(store);
    }

    @Transactional
    public void updateStoreCategory(
            UUID storeId, String categoryName) {

        Store store = getStore(storeId);
        Category category = checkCategoryExists(categoryName);

        store.setCategory(category);
        storeRepository.save(store);
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
