package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberAuditRequest(
        @NotBlank(message = "审核状态不能为空") String status,
        @Size(max = 500, message = "审核理由过长") String reason
) {
}
