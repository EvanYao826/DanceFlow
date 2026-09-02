package com.danceflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "昵称不能为空") @Size(min = 2, max = 30, message = "昵称长度必须为 2-30 位") String nickname,
        @Size(max = 500, message = "头像地址过长") String avatar,
        @Size(max = 20, message = "手机号过长") String phone,
        @Email(message = "邮箱格式不正确") @Size(max = 100, message = "邮箱过长") String email
) {
}
