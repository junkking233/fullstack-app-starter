package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.dto.AuthUserDto;
import com.zhiqu.community.dto.ChangePasswordRequest;
import com.zhiqu.community.dto.LoginRequest;
import com.zhiqu.community.dto.LoginResponse;
import com.zhiqu.community.dto.RegisterRequest;
import com.zhiqu.community.dto.UpdateProfileRequest;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.AuthService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.ok("注册成功", authService.register(request));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok("登录成功", authService.login(request));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok("已退出登录", null);
    }

    @GetMapping("/me")
    public Result<AuthUserDto> me(HttpServletRequest request) {
        return Result.ok(authService.getCurrentUser(currentUser(request).getUserId()));
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        authService.changePassword(currentUser(httpRequest).getUserId(), request);
        return Result.ok("密码修改成功", null);
    }

    @GetMapping("/profile")
    public Result<AuthUserDto> getProfile(HttpServletRequest httpRequest) {
        return Result.ok(authService.getProfile(currentUser(httpRequest).getUserId()));
    }

    @PutMapping("/profile")
    public Result<AuthUserDto> updateProfile(@RequestBody UpdateProfileRequest request, HttpServletRequest httpRequest) {
        return Result.ok("更新成功", authService.updateProfile(currentUser(httpRequest).getUserId(), request));
    }

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "登录已失效，请重新登录");
    }
}
