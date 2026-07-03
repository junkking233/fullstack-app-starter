package com.fullstack.starter.service;

import com.fullstack.starter.dto.AuthUserDto;
import com.fullstack.starter.dto.ChangePasswordRequest;
import com.fullstack.starter.dto.LoginRequest;
import com.fullstack.starter.dto.LoginResponse;
import com.fullstack.starter.dto.RegisterRequest;
import com.fullstack.starter.dto.UpdateProfileRequest;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    AuthUserDto getCurrentUser(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);

    AuthUserDto getProfile(Long userId);

    AuthUserDto updateProfile(Long userId, UpdateProfileRequest request);
}
