package com.danceflow.vo;

import java.time.LocalDateTime;

public record WorkVO(Long id, Long userId, String authorName, String title, String coverUrl, String mediaUrl,
                     String mediaType, String description, String danceType, String auditStatus, String auditReason,
                     Integer likeCount, Integer commentCount, Integer collectionCount, Integer viewCount,
                     Boolean liked, Boolean collected, LocalDateTime publishedTime, LocalDateTime createdAt) {}
