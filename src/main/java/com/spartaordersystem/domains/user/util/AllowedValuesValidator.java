package com.spartaordersystem.domains.user.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, String> {
    private String[] allowedValues;

    @Override
    public void initialize(AllowedValues constraintAnnotation) {
        this.allowedValues = constraintAnnotation.values();
    }

    // 검증 로직
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.isBlank()) {
            return false;
        }
        return Arrays.asList(allowedValues).contains(value); // 허용값 중 하나인지 검증
    }
}
