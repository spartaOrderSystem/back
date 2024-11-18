package com.spartaordersystem.domains.review.repository;

import com.spartaordersystem.domains.order.entity.Order;
import com.spartaordersystem.domains.review.entity.Review;
import com.spartaordersystem.domains.store.entity.Store;
import com.spartaordersystem.domains.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {


    Optional<Review> existsByOrder(Order order);

    Page<Review> findByUser(User user, Pageable pageable);

    Page<Review> findByStore(Store store, Pageable pageable);
}
