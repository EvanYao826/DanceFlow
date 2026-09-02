package com.danceflow.dto;

import jakarta.validation.constraints.Size;

public record ActivityApplyRequest(@Size(max = 500, message = "备注过长") String remark) {
}
