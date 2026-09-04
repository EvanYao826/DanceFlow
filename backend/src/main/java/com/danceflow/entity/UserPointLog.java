package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("user_point_log")
public class UserPointLog {
    @TableId private Long id;
    private Long userId; private String pointType; private Integer pointValue; private String sourceType; private Long sourceId; private String remark;
    private Integer isDeleted; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
