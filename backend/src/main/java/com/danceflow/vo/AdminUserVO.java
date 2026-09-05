package com.danceflow.vo;

import com.danceflow.entity.User;

import java.time.LocalDateTime;

public record AdminUserVO(Long id, String username, String nickname, String avatar, String phone, String email,
                          String role, Integer status, LocalDateTime lastLoginTime, LocalDateTime createdAt) {
    public static AdminUserVO from(User user) {
        return new AdminUserVO(user.getId(), user.getUsername(), user.getNickname(), user.getAvatar(), user.getPhone(),
                user.getEmail(), user.getRole(), user.getStatus(), user.getLastLoginTime(), user.getCreatedAt());
    }
}
