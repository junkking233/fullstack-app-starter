package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.dto.UserQuery;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.UserService;
import com.zhiqu.community.util.PasswordUtils;
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
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public User create(User user) {
        prepareForCreate(user);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setId(id);
        prepareForUpdate(user);
        int updated = userMapper.updateById(user);
        if (updated == 0) {
            throw new BusinessException(404, "用户不存在");
        }
        return userMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        int deleted = userMapper.deleteById(id);
        if (deleted == 0) {
            throw new BusinessException(404, "用户不存在");
        }
    }

    @Override
    public void banUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        User update = new User();
        update.setId(id);
        update.setStatus("banned");
        userMapper.updateById(update);
    }

    @Override
    public void unbanUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException(404, "用户不存在");
        }
        User update = new User();
        update.setId(id);
        update.setStatus("active");
        userMapper.updateById(update);
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
        if (!StringUtils.hasText(user.getStatus())) {
            user.setStatus("active");
        }
    }
}
