package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkAuditRequest(@NotBlank String status, @Size(max = 500) String reason) {}
