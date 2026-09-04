package com.danceflow.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record NoticeRequest(@NotBlank @Size(max = 120) String title, @NotBlank @Size(max = 10000) String content, Boolean topFlag) {}
