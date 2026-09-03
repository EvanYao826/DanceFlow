package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkCommentRequest(Long parentId, @NotBlank @Size(max = 500) String content) {}
