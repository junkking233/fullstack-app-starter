package com.zhiqu.community.service;

import com.zhiqu.community.dto.AuthUserDto;
import com.zhiqu.community.dto.ChangePasswordRequest;
import com.zhiqu.community.dto.LoginRequest;
import com.zhiqu.community.dto.LoginResponse;
import com.zhiqu.community.dto.RegisterRequest;
import com.zhiqu.community.dto.UpdateProfileRequest;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    AuthUserDto getCurrentUser(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);

    AuthUserDto getProfile(Long userId);

    AuthUserDto updateProfile(Long userId, UpdateProfileRequest request);
}
