package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivityStatusRequest(@NotBlank(message = "活动状态不能为空") String status) {
}
