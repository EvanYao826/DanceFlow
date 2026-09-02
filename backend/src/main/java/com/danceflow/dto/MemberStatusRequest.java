package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberStatusRequest(@NotBlank(message = "成员状态不能为空") String status) {
}
