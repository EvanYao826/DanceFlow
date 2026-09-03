package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record LessonRequest(
        @NotBlank(message = "课时标题不能为空") @Size(max = 100, message = "课时标题过长") String title,
        @Size(max = 500, message = "视频地址过长") String videoUrl,
        @NotNull(message = "课时时长不能为空") @PositiveOrZero(message = "课时时长不能为负数") Integer duration,
        @Size(max = 10000, message = "课时内容过长") String content,
        @NotNull(message = "排序值不能为空") @PositiveOrZero(message = "排序值不能为负数") Integer sortNo,
        String status
) {
}
