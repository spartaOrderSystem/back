package com.spartaordersystem.domains.category.repository;

import com.spartaordersystem.domains.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByName(String categoryName);

    List<Category> findAllByIsDeletedFalse();

    Optional<Category> findByName(String categoryName);
}
