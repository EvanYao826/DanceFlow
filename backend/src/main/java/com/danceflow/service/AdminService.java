package com.danceflow.service;

import com.danceflow.entity.Permission;
import com.danceflow.entity.Role;
import com.danceflow.mapper.PermissionMapper;
import com.danceflow.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import com.danceflow.vo.PermissionVO;
import com.danceflow.vo.RoleVO;

import java.util.List;

@Service
public class AdminService {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public AdminService(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    public List<RoleVO> roles() {
        return roleMapper.selectList(null).stream().map(RoleVO::from).toList();
    }

    public List<PermissionVO> permissions() {
        return permissionMapper.selectList(null).stream().map(PermissionVO::from).toList();
    }
}
