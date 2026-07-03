package com.fullstack.starter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fullstack.starter.dto.UserQuery;
import com.fullstack.starter.entity.User;

public interface UserService {

    IPage<User> page(UserQuery query);

    User getById(Long id);

    User create(User user);

    User update(Long id, User user);

    void delete(Long id);

    void banUser(Long id);

    void unbanUser(Long id);
}
