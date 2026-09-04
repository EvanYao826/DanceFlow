package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("forum_post")
public class ForumPost {
    @TableId private Long id;
    private Long userId; private String title; private String content; private String coverUrl; private String category; private String status;
    private Integer viewCount; private Integer likeCount; private Integer isDeleted; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
