package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.AdminResetPasswordRequest;
import com.danceflow.dto.AdminUserRoleRequest;
import com.danceflow.dto.AdminUserStatusRequest;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.AdminUserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AdminUserService {
    private final UserMapper mapper; private final PasswordEncoder encoder;
    public AdminUserService(UserMapper mapper, PasswordEncoder encoder) { this.mapper = mapper; this.encoder = encoder; }
    public PageResult<AdminUserVO> page(long page, long pageSize, String keyword, Integer status) {
        Page<User> data = mapper.selectPage(new Page<>(Math.max(1, page), Math.min(Math.max(1, pageSize), 100)),
                new LambdaQueryWrapper<User>().eq(User::getIsDeleted, 0)
                        .and(keyword != null && !keyword.isBlank(), q -> q.like(User::getUsername, keyword).or().like(User::getNickname, keyword))
                        .eq(status != null, User::getStatus).orderByDesc(User::getCreatedAt));
        return new PageResult<>(data.getRecords().stream().map(AdminUserVO::from).toList(), data.getTotal(), data.getCurrent(), data.getSize());
    }
    @Transactional public AdminUserVO status(Long id, AdminUserStatusRequest request) { User user = required(id); if (!Set.of(0, 1).contains(request.status())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户状态不正确"); user.setStatus(request.status()); mapper.updateById(user); return AdminUserVO.from(user); }
    @Transactional public AdminUserVO role(Long id, AdminUserRoleRequest request) { User user = required(id); if (!Set.of("USER", "ADMIN", "SUPER_ADMIN").contains(request.role())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户角色不正确"); user.setRole(request.role()); mapper.updateById(user); return AdminUserVO.from(user); }
    @Transactional public void resetPassword(Long id, AdminResetPasswordRequest request) { User user = required(id); user.setPassword(encoder.encode(request.newPassword())); mapper.updateById(user); }
    private User required(Long id) { User user = mapper.selectById(id); if (user == null || Integer.valueOf(1).equals(user.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在"); return user; }
}
