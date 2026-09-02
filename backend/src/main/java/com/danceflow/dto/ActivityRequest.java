package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ActivityRequest(
        @NotBlank(message = "活动标题不能为空") @Size(max = 100, message = "活动标题过长") String title,
        @Size(max = 500, message = "封面地址过长") String coverUrl,
        @Size(max = 10000, message = "活动描述过长") String description,
        @NotBlank(message = "活动类型不能为空") @Size(max = 30, message = "活动类型过长") String activityType,
        @NotNull(message = "开始时间不能为空") LocalDateTime startTime,
        @NotNull(message = "结束时间不能为空") LocalDateTime endTime,
        @NotBlank(message = "活动地点不能为空") @Size(max = 200, message = "活动地点过长") String location,
        @NotNull(message = "人数上限不能为空") @Positive(message = "人数上限必须为正整数") Integer capacity,
        @NotNull(message = "报名截止时间不能为空") LocalDateTime applyDeadline
) {
}
