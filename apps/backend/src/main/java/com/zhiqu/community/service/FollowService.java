package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.entity.User;

public interface FollowService {

    boolean toggleFollow(Long followerId, Long followedId);

    boolean isFollowing(Long followerId, Long followedId);

    IPage<User> listFollowers(Long userId, Integer page, Integer pageSize);

    IPage<User> listFollowing(Long userId, Integer page, Integer pageSize);
}
