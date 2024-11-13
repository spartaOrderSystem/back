package com.spartaordersystem.domains.user.entity;

import com.spartaordersystem.global.common.BaseAudit;
import com.spartaordersystem.global.security.user.UserRoleConverter;
import com.spartaordersystem.global.security.user.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@Entity
@Table(name = "users", indexes = @Index(name = "idx_nickname", columnList = "nickname"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]+$")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String nickname;

    @Builder
    public User(String username, String password, UserRoleEnum role, String nickname) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
    }
}
