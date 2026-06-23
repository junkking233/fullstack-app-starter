package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.Follow;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.FollowMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.FollowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;
    private final UserMapper userMapper;

    public FollowServiceImpl(FollowMapper followMapper, UserMapper userMapper) {
        this.followMapper = followMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public boolean toggleFollow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new BusinessException(400, "不能关注自己");
        }
        User target = userMapper.selectById(followedId);
        if (target == null) {
            throw new BusinessException(404, "用户不存在");
        }

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, followerId)
               .eq(Follow::getFollowedId, followedId);
        Follow existing = followMapper.selectOne(wrapper);

        if (existing != null) {
            followMapper.deleteById(existing.getId());
            return false;
        } else {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowedId(followedId);
            follow.setCreatedAt(LocalDateTime.now());
            followMapper.insert(follow);
            return true;
        }
    }

    @Override
    public boolean isFollowing(Long followerId, Long followedId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, followerId)
               .eq(Follow::getFollowedId, followedId);
        return followMapper.selectCount(wrapper) > 0;
    }

    @Override
    public IPage<User> listFollowers(Long userId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowedId, userId)
               .orderByDesc(Follow::getCreatedAt);
        Page<Follow> followPage = followMapper.selectPage(new Page<>(pn, ps), wrapper);

        IPage<User> userPage = new Page<>(pn, ps, followPage.getTotal());
        List<User> users = new ArrayList<>();
        for (Follow f : followPage.getRecords()) {
            User user = userMapper.selectById(f.getFollowerId());
            if (user != null) {
                user.setPassword(null);
                users.add(user);
            }
        }
        userPage.setRecords(users);
        return userPage;
    }

    @Override
    public IPage<User> listFollowing(Long userId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, userId)
               .orderByDesc(Follow::getCreatedAt);
        Page<Follow> followPage = followMapper.selectPage(new Page<>(pn, ps), wrapper);

        IPage<User> userPage = new Page<>(pn, ps, followPage.getTotal());
        List<User> users = new ArrayList<>();
        for (Follow f : followPage.getRecords()) {
            User user = userMapper.selectById(f.getFollowedId());
            if (user != null) {
                user.setPassword(null);
                users.add(user);
            }
        }
        userPage.setRecords(users);
        return userPage;
    }
}
