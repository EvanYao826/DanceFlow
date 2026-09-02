package com.danceflow.controller;

import com.danceflow.common.Result;
import com.danceflow.service.AdminService;
import com.danceflow.vo.PermissionVO;
import com.danceflow.vo.RoleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/roles")
    public Result<List<RoleVO>> roles() {
        return Result.ok(adminService.roles());
    }

    @GetMapping("/permissions")
    public Result<List<PermissionVO>> permissions() {
        return Result.ok(adminService.permissions());
    }
}
