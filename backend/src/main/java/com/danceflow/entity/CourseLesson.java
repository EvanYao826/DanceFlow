package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course_lesson")
public class CourseLesson {
    @TableId
    private Long id;
    private Long courseId;
    private String title;
    private String videoUrl;
    private Integer duration;
    private String content;
    private Integer sortNo;
    private String status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
