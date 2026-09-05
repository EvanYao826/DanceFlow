package com.danceflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DictUpdateRequest(@NotBlank String dictLabel, @NotBlank String dictValue, @NotNull Integer sortNo, @NotNull Integer status) {}
