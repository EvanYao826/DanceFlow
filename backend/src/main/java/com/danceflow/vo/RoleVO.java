package com.danceflow.vo;

import com.danceflow.entity.Role;

public record RoleVO(Long id, String roleCode, String roleName, Integer status) {
    public static RoleVO from(Role role) {
        return new RoleVO(role.getId(), role.getRoleCode(), role.getRoleName(), role.getStatus());
    }
}
