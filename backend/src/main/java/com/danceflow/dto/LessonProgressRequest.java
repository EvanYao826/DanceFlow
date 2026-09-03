package com.danceflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LessonProgressRequest(
        @NotNull(message = "学习进度不能为空") @PositiveOrZero(message = "学习进度不能为负数") Integer progressSeconds,
        Boolean completed
) {
}
