package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity_apply")
public class ActivityApply {
    @TableId
    private Long id;
    private Long activityId;
    private Long userId;
    private String applyStatus;
    private String remark;
    private LocalDateTime applyTime;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
