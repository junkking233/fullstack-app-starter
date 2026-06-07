package com.example.controller;

import com.example.common.Result;
import com.example.dto.AuthUserDto;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.entity.User;
import com.example.exception.BusinessException;
import com.example.service.AuthService;
import com.example.util.TokenSubject;
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

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok("登录成功", authService.login(request));
    }

    @PostMapping("/register")
    public Result<AuthUserDto> register(@Valid @RequestBody User user) {
        return Result.ok("注册成功", authService.register(user));
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

    private TokenSubject currentUser(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject tokenSubject) {
            return tokenSubject;
        }
        throw new BusinessException(401, "登录已失效，请重新登录");
    }
}
