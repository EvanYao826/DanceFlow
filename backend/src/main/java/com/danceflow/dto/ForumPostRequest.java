package com.danceflow.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record ForumPostRequest(@NotBlank @Size(max = 120) String title, @NotBlank @Size(max = 10000) String content,
                               @Size(max = 500) String coverUrl, @NotBlank @Size(max = 30) String category) {}
