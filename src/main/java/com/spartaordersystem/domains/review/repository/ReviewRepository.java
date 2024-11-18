package com.spartaordersystem.domains.review.repository;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {


    Optional<Review> existsByOrder(Order order);
}
