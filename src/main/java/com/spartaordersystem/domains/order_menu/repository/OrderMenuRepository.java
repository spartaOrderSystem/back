package com.spartaordersystem.domains.order_menu.repository;

import com.spartaordersystem.domains.order_menu.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, UUID> {
}
