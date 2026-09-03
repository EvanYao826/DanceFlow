package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dance_work")
public class DanceWork {
    @TableId private Long id;
    private Long userId;
    private String title;
    private String coverUrl;
    private String mediaUrl;
    private String mediaType;
    private String description;
    private String danceType;
    private String auditStatus;
    private String auditReason;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectionCount;
    private Integer viewCount;
    private LocalDateTime publishedTime;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
