package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_dict")
public class SysDict {
    @TableId private Long id;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer sortNo;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
