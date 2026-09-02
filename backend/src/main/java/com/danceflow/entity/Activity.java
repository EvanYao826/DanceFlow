package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {
    @TableId
    private Long id;
    private String title;
    private String coverUrl;
    private String description;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private Integer capacity;
    private LocalDateTime applyDeadline;
    private String status;
    private Long publisherId;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
