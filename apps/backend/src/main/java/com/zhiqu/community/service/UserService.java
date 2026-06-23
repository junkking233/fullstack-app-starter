package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.dto.UserQuery;
import com.zhiqu.community.entity.User;

public interface UserService {

    IPage<User> page(UserQuery query);

    User getById(Long id);

    User create(User user);

    User update(Long id, User user);

    void delete(Long id);

    void banUser(Long id);

    void unbanUser(Long id);
}
