package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 30, message = "用户名长度必须为 3-30 位")
        String username,
        @NotBlank(message = "密码不能为空")
        @Size(min = 8, max = 32, message = "密码长度必须为 8-32 位")
        String password,
        @NotBlank(message = "昵称不能为空")
        @Size(min = 2, max = 30, message = "昵称长度必须为 2-30 位")
        String nickname
) {
}
