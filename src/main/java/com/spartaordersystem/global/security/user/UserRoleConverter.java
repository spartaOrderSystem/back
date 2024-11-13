package com.spartaordersystem.global.security.user;

import jakarta.persistence.AttributeConverter;

/**
 * UserRoleEnum 를 Authority 필드값으로 DB에 저장할 때 사용하는 Converter
 */
public class UserRoleConverter implements AttributeConverter<UserRoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(UserRoleEnum userRoleEnum) {
        if (userRoleEnum == null) {
            return null;
        }
        return userRoleEnum.getAuthority();
    }

    @Override
    public UserRoleEnum convertToEntityAttribute(String s) {
        return UserRoleEnum.getInstance(s);
    }
}
