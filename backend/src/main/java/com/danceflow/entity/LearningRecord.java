package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_record")
public class LearningRecord {
    @TableId
    private Long id;
    private Long userId;
    private Long courseId;
    private Long lessonId;
    private Integer progressSeconds;
    private Integer completed;
    private LocalDateTime lastLearnTime;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
