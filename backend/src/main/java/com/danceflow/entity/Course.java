package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId
    private Long id;
    private String title;
    private String coverUrl;
    private String danceType;
    private String difficulty;
    private String teacherName;
    private String description;
    private Integer lessonCount;
    private String status;
    private Integer sortNo;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
