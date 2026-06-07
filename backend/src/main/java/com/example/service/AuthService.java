package com.example.service;

import com.example.dto.AuthUserDto;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.entity.User;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    AuthUserDto register(User user);

    AuthUserDto getCurrentUser(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);
}
