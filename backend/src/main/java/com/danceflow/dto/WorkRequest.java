package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkRequest(@NotBlank @Size(max = 100) String title, @Size(max = 500) String coverUrl,
                          @NotBlank @Size(max = 500) String mediaUrl, @NotBlank String mediaType,
                          @Size(max = 2000) String description, @NotBlank @Size(max = 50) String danceType) {}
