package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberApplyRequest(
        @NotBlank(message = "舞种不能为空") @Size(max = 50, message = "舞种过长") String danceType,
        @NotBlank(message = "技术等级不能为空") @Size(max = 30, message = "技术等级过长") String skillLevel,
        @Size(max = 1000, message = "个人简介过长") String bio
) {
}
