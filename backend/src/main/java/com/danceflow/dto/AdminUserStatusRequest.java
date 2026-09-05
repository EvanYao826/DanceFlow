package com.danceflow.dto;

import jakarta.validation.constraints.NotNull;

public record AdminUserStatusRequest(@NotNull Integer status) {}
