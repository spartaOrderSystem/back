package com.spartaordersystem.domains.user.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 값 중 하나를 갖는지 검증하는 커스텀 검증 어노테이션
 */
@Constraint(validatedBy = AllowedValuesValidator.class) // java Bean Validation 검증 어노테이션으로 등록
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValues {
    String message() default "허용되지 않은 값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] values(); // 허용값을 담을 배열
}
