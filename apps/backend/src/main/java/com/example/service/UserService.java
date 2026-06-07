package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.dto.UserQuery;
import com.example.entity.User;

public interface UserService {

    IPage<User> page(UserQuery query);

    User getById(Long id);

    User create(User user);

    User update(Long id, User user);

    void delete(Long id);
}
