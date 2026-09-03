package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseStatusRequest(@NotBlank(message = "课程状态不能为空") String status) {
}
