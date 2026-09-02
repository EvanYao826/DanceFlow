package com.danceflow.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_permission")
public class Permission {
    @TableId
    private Long id;
    private String permissionCode;
    private String permissionName;
    private String type;
    private Long parentId;
    private String path;
    private Integer status;
}
