package com.danceflow.vo;
import com.danceflow.entity.ForumPost;
import java.time.LocalDateTime;
public record ForumPostVO(Long id, Long userId, String authorName, String title, String content, String coverUrl, String category,
                          String status, Integer viewCount, Integer likeCount, LocalDateTime createdAt) {
    public static ForumPostVO from(ForumPost post, String authorName) { return new ForumPostVO(post.getId(), post.getUserId(), authorName, post.getTitle(), post.getContent(), post.getCoverUrl(), post.getCategory(), post.getStatus(), post.getViewCount(), post.getLikeCount(), post.getCreatedAt()); }
}
