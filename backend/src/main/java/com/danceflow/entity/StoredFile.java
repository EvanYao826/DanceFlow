package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("stored_file")
public class StoredFile {
    @TableId
    private Long id;
    private String originalName;
    private String storageKey;
    private String fileUrl;
    private String contentType;
    private Long fileSize;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
