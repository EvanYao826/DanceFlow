package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.AdminResetPasswordRequest;
import com.danceflow.dto.AdminUserRoleRequest;
import com.danceflow.dto.AdminUserStatusRequest;
import com.danceflow.service.AdminUserService;
import com.danceflow.vo.AdminUserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController @RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService service;
    public AdminUserController(AdminUserService service) { this.service = service; }
    @GetMapping public Result<PageResult<AdminUserVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize, @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer status) { return Result.ok(service.page(page, pageSize, keyword, status)); }
    @PutMapping("/{id}/status") public Result<AdminUserVO> status(@PathVariable Long id, @Valid @RequestBody AdminUserStatusRequest request) { return Result.ok(service.status(id, request)); }
    @PreAuthorize("hasRole('SUPER_ADMIN')") @PutMapping("/{id}/roles") public Result<AdminUserVO> role(@PathVariable Long id, @Valid @RequestBody AdminUserRoleRequest request) { return Result.ok(service.role(id, request)); }
    @PutMapping("/{id}/reset-password") public Result<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody AdminResetPasswordRequest request) { service.resetPassword(id, request); return Result.ok(); }
}
