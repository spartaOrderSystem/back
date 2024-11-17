package com.spartaordersystem.domains.order.repository;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
