package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId private Long id;
    private Long operatorId;
    private String operatorName;
    private String action;
    private String targetType;
    private String targetId;
    private String requestPath;
    private String result;
    private String detail;
    private LocalDateTime createdAt;
}
