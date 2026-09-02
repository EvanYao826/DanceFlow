package com.danceflow.vo;

import com.danceflow.entity.User;

public record UserVO(Long id, String username, String nickname, String avatar, String phone, String email, String role) {
    public static UserVO from(User user) {
        return new UserVO(user.getId(), user.getUsername(), user.getNickname(), user.getAvatar(), user.getPhone(), user.getEmail(), user.getRole());
    }
}
