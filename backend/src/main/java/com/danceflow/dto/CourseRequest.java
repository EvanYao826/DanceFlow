package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotBlank(message = "课程标题不能为空") @Size(max = 100, message = "课程标题过长") String title,
        @Size(max = 500, message = "封面地址过长") String coverUrl,
        @NotBlank(message = "舞种不能为空") @Size(max = 50, message = "舞种过长") String danceType,
        @NotBlank(message = "难度不能为空") @Size(max = 30, message = "难度过长") String difficulty,
        @NotBlank(message = "教师姓名不能为空") @Size(max = 50, message = "教师姓名过长") String teacherName,
        @Size(max = 10000, message = "课程描述过长") String description,
        @NotNull(message = "排序值不能为空") @PositiveOrZero(message = "排序值不能为负数") Integer sortNo
) {
}
