package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("notice")
public class Notice {
    @TableId private Long id;
    private String title; private String content; private Long publisherId; private String publishStatus; private LocalDateTime publishTime;
    private Integer topFlag; private Integer isDeleted; private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
