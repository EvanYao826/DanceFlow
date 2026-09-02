package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.ChangePasswordRequest;
import com.danceflow.dto.LoginRequest;
import com.danceflow.dto.RegisterRequest;
import com.danceflow.dto.UpdateProfileRequest;
import com.danceflow.entity.Permission;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.PermissionMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.security.AuthUser;
import com.danceflow.security.JwtService;
import com.danceflow.vo.LoginVO;
import com.danceflow.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserMapper userMapper, PermissionMapper permissionMapper,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (findByUsername(request.username()) != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setNickname(request.nickname());
        user.setRole("USER");
        user.setStatus(1);
        user.setIsDeleted(0);
        userMapper.insert(user);
    }

    public LoginVO login(LoginRequest request) {
        User user = findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "用户名或密码错误");
        }
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
        AuthUser authUser = new AuthUser(user.getId(), user.getUsername(), user.getRole());
        return new LoginVO(jwtService.createToken(authUser), jwtService.getExpirationSeconds(), UserVO.from(user), permissions(user.getRole()));
    }

    public User getRequiredUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        return user;
    }

    public UserVO me(Long id) {
        return UserVO.from(getRequiredUser(id));
    }

    @Transactional
    public UserVO updateProfile(Long id, UpdateProfileRequest request) {
        User user = getRequiredUser(id);
        user.setNickname(request.nickname());
        user.setAvatar(request.avatar());
        user.setPhone(request.phone());
        user.setEmail(request.email());
        userMapper.updateById(user);
        return UserVO.from(user);
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = getRequiredUser(id);
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "旧密码错误");
        }
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "两次输入的新密码不一致");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userMapper.updateById(user);
    }

    public List<String> permissions(String role) {
        if ("SUPER_ADMIN".equals(role)) {
            return permissionMapper.selectList(new LambdaQueryWrapper<Permission>().eq(Permission::getStatus, 1))
                    .stream().map(Permission::getPermissionCode).toList();
        }
        return permissionMapper.selectList(new LambdaQueryWrapper<Permission>().eq(Permission::getStatus, 1)
                        .in(Permission::getPermissionCode, "home:view", "ADMIN".equals(role) ? "admin:view" : "home:view"))
                .stream().map(Permission::getPermissionCode).distinct().toList();
    }

    private User findByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).last("LIMIT 1"));
    }
}
