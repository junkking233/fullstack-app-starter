package com.example.service;

import com.example.dto.AuthUserDto;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    AuthUserDto getCurrentUser(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);
}
