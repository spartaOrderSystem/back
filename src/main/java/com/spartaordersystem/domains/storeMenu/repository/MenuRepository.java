package com.spartaordersystem.domains.storeMenu.repository;

import com.spartaordersystem.domains.storeMenu.entity.StoreMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<StoreMenu, UUID> {
}
