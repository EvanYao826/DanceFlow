package com.danceflow.dto;
import jakarta.validation.constraints.NotBlank;
public record ForumPostStatusRequest(@NotBlank String status) {}
