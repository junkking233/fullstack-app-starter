package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiqu.community.dto.AuthUserDto;
import com.zhiqu.community.dto.ChangePasswordRequest;
import com.zhiqu.community.dto.LoginRequest;
import com.zhiqu.community.dto.LoginResponse;
import com.zhiqu.community.dto.RegisterRequest;
import com.zhiqu.community.dto.UpdateProfileRequest;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.AuthService;
import com.zhiqu.community.util.PasswordUtils;
import com.zhiqu.community.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final TokenUtils tokenUtils;

    public AuthServiceImpl(UserMapper userMapper, TokenUtils tokenUtils) {
        this.userMapper = userMapper;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        User existing = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (existing != null) {
            throw new BusinessException(409, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.md5(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole("USER");
        user.setStatus("active");
        userMapper.insert(user);

        long expiresAt = tokenUtils.expiresAtFromNow();
        String token = tokenUtils.generate(user, expiresAt);
        return new LoginResponse(token, AuthUserDto.from(user), expiresAt);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (user == null || !PasswordUtils.md5(request.getPassword()).equals(user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (!"active".equals(user.getStatus())) {
            throw new BusinessException(403, "账号已被禁用");
        }

        long expiresAt = tokenUtils.expiresAtFromNow();
        String token = tokenUtils.generate(user, expiresAt);
        return new LoginResponse(token, AuthUserDto.from(user), expiresAt);
    }

    @Override
    public AuthUserDto getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || !"active".equals(user.getStatus())) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }
        return AuthUserDto.from(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "登录已失效，请重新登录");
        }
        if (!PasswordUtils.md5(request.getOldPassword()).equals(user.getPassword())) {
            throw new BusinessException(400, "原密码不正确");
        }

        User update = new User();
        update.setId(userId);
        update.setPassword(PasswordUtils.md5(request.getNewPassword()));
        userMapper.updateById(update);
    }

    @Override
    public AuthUserDto getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return AuthUserDto.from(user);
    }

    @Override
    public AuthUserDto updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        User update = new User();
        update.setId(userId);
        if (StringUtils.hasText(request.getNickname())) {
            update.setNickname(request.getNickname());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            update.setAvatar(request.getAvatar());
        }
        if (StringUtils.hasText(request.getBio())) {
            update.setBio(request.getBio());
        }
        userMapper.updateById(update);

        User updated = userMapper.selectById(userId);
        return AuthUserDto.from(updated);
    }
}
