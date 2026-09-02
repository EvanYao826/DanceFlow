package com.danceflow.controller;

import com.danceflow.common.Result;
import com.danceflow.dto.ChangePasswordRequest;
import com.danceflow.dto.LoginRequest;
import com.danceflow.dto.RegisterRequest;
import com.danceflow.dto.UpdateProfileRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.AuthService;
import com.danceflow.vo.LoginVO;
import com.danceflow.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return Result.ok();
    }

    @PostMapping("/auth/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(authService.login(request));
    }

    @PostMapping("/auth/logout")
    public Result<Void> logout() {
        return Result.ok();
    }

    @GetMapping("/auth/me")
    public Result<UserVO> me(Authentication authentication) {
        return Result.ok(authService.me(((AuthUser) authentication.getPrincipal()).id()));
    }

    @PutMapping("/users/me")
    public Result<UserVO> updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return Result.ok(authService.updateProfile(((AuthUser) authentication.getPrincipal()).id(), request));
    }

    @PutMapping("/users/me/password")
    public Result<Void> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(((AuthUser) authentication.getPrincipal()).id(), request);
        return Result.ok();
    }

    @GetMapping("/users/{id}")
    public Result<UserVO> user(@PathVariable Long id) {
        return Result.ok(authService.me(id));
    }
}
