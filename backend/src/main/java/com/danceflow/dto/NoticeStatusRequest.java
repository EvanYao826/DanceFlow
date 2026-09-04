package com.danceflow.dto;
import jakarta.validation.constraints.NotBlank;
public record NoticeStatusRequest(@NotBlank String status) {}
