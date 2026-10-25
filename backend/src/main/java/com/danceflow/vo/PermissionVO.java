package com.danceflow.vo;

import com.danceflow.entity.Permission;

public record PermissionVO(Long id, String permissionCode, String permissionName, String roleCode, String type, Long parentId, String path, Integer status) {
    public static PermissionVO from(Permission permission) {
        return new PermissionVO(permission.getId(), permission.getPermissionCode(), permission.getPermissionName(), permission.getRoleCode(), permission.getType(), permission.getParentId(), permission.getPath(), permission.getStatus());
    }
}
