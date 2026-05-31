package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dto.UserQuery;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.util.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public IPage<User> page(UserQuery query) {
        long pageNum = query.getPage() == null ? 1 : query.getPage();
        long pageSize = query.getPageSize() == null ? 10 : query.getPageSize();
        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.selectByCondition(page, query);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User create(User user) {
        prepareForCreate(user);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        user.setId(id);
        prepareForUpdate(user);
        userMapper.updateById(user);
        return userMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    private void prepareForCreate(User user) {
        prepareBaseFields(user);
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(PasswordUtils.md5IfNeeded(user.getPassword()));
        } else {
            user.setPassword(PasswordUtils.md5("123456"));
        }
    }

    private void prepareForUpdate(User user) {
        prepareBaseFields(user);
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(PasswordUtils.md5IfNeeded(user.getPassword()));
        } else {
            user.setPassword(null);
        }
    }

    private void prepareBaseFields(User user) {
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole("USER");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
    }
}
