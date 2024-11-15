package com.spartaordersystem.domains.category.service;

import com.spartaordersystem.domains.category.controller.dto.CategoryDto;
import com.spartaordersystem.domains.category.entity.Category;
import com.spartaordersystem.domains.category.repository.CategoryRepository;
import com.spartaordersystem.domains.user.entity.User;
import com.spartaordersystem.domains.user.repository.UserRepository;
import com.spartaordersystem.global.common.GlobalConst;
import com.spartaordersystem.global.exception.CustomException;
import com.spartaordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryDto.CreateCategoryResponseDto createCategory(User user, CategoryDto.CreateCategoryRequestDto requestDto) {
        checkUserRole(user.getRole().getAuthority());
        User authUser = getAuthUser(user);
        checkCategoryAlreadyExists(requestDto);

        Category category = Category.builder()
                .name(requestDto.getCategoryName())
                .build();

        categoryRepository.save(category);

        return CategoryDto.CreateCategoryResponseDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }

    private User getAuthUser(User user) {
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public CategoryDto.UpdateCategoryResponseDto updateCategory(UUID categoryId, User user, CategoryDto.UpdateCategoryRequestDto requestDto) {
        checkUserRole(user.getRole().getAuthority());
        Category category = checkCategoryExists(categoryId);
        checkCategoryIsDeleted(category);

        category.setName(requestDto.getCategoryName());

        categoryRepository.save(category);

        return CategoryDto.UpdateCategoryResponseDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }

    public void deleteCategory(UUID categoryId, User user) {
        checkUserRole(user.getRole().getAuthority());
        Category category = checkCategoryExists(categoryId);
        checkCategoryIsDeleted(category);

        category.setDeleted(user.getUsername());
        categoryRepository.save(category);
    }

    public List<CategoryDto.GetCategoryResponseDto> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAllByIsDeletedFalse();

        return categoryList.stream()
                .map(category -> new CategoryDto.GetCategoryResponseDto(category.getId(), category.getName()))
                .toList();
    }

    private void checkUserRole(String userRole) {
        if (!(userRole.equals(GlobalConst.ROLE_MANAGER) || userRole.equals(GlobalConst.ROLE_ADMIN))) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkCategoryAlreadyExists(CategoryDto.CreateCategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getCategoryName())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_CATEGORY);
        }
    }

    private Category checkCategoryExists(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private static void checkCategoryIsDeleted(Category category) {
        if (category.isDeleted()) {
            throw new CustomException(ErrorCode.CATEGORY_IS_DELETED);
        }
    }
}
