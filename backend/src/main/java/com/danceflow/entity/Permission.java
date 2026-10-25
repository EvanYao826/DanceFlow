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
    /** 授予该权限的最低角色：USER/ADMIN/SUPER_ADMIN，高级角色自动继承 */
    private String roleCode;
    private String type;
    private Long parentId;
    private String path;
    private Integer status;
}
