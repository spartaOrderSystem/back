package com.spartaordersystem.domains.store.repository;

import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.store.enums.StoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StoreRepositoryCustom {
    Page<Store> getStoresBySearchOptions(Pageable pageable, String title, StoreStatus status, Category category);
}
