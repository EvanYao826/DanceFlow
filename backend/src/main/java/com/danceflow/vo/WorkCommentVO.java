package com.danceflow.vo;

import java.time.LocalDateTime;

public record WorkCommentVO(Long id, Long workId, Long userId, String authorName, Long parentId, String content,
                            LocalDateTime createdAt) {}
