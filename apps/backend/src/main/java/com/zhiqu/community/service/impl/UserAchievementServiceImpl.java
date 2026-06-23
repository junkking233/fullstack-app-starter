package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.entity.UserAchievement;
import com.zhiqu.community.mapper.UserAchievementMapper;
import com.zhiqu.community.mapper.UserMapper;
import com.zhiqu.community.service.NotificationService;
import com.zhiqu.community.service.UserAchievementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserAchievementServiceImpl implements UserAchievementService {

    private final UserAchievementMapper userAchievementMapper;
    private final UserMapper userMapper;
    private final NotificationService notificationService;

    // Achievement definitions
    private static final Map<String, String> ACHIEVEMENT_NAMES = Map.ofEntries(
        Map.entry("registered", "初来乍到"),
        Map.entry("first_question", "首次提问"),
        Map.entry("first_answer", "首次回答"),
        Map.entry("first_accepted", "最佳答案"),
        Map.entry("hundred_votes", "百赞达人"),
        Map.entry("five_hundred_votes", "人气之星"),
        Map.entry("fifty_answers", "知识贡献者"),
        Map.entry("week_login", "社区常客"),
        Map.entry("month_login", "铁杆粉丝"),
        Map.entry("hundred_followers", "万人迷")
    );

    public UserAchievementServiceImpl(UserAchievementMapper userAchievementMapper,
                                       UserMapper userMapper,
                                       NotificationService notificationService) {
        this.userAchievementMapper = userAchievementMapper;
        this.userMapper = userMapper;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public boolean checkAndUnlock(Long userId, String achievementCode) {
        LambdaQueryWrapper<UserAchievement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAchievement::getUserId, userId)
                .eq(UserAchievement::getAchievementCode, achievementCode);
        if (userAchievementMapper.selectCount(wrapper) > 0) {
            return false;
        }

        UserAchievement achievement = new UserAchievement();
        achievement.setUserId(userId);
        achievement.setAchievementCode(achievementCode);
        achievement.setUnlockedAt(LocalDateTime.now());
        userAchievementMapper.insert(achievement);

        String name = ACHIEVEMENT_NAMES.getOrDefault(achievementCode, achievementCode);
        notificationService.createNotification(
                userId, "achievement", achievement.getId(), null,
                "恭喜您解锁了新成就：" + name
        );
        return true;
    }

    @Override
    public List<Map<String, Object>> listByUser(Long userId) {
        LambdaQueryWrapper<UserAchievement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAchievement::getUserId, userId)
                .orderByDesc(UserAchievement::getUnlockedAt);
        List<UserAchievement> list = userAchievementMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (UserAchievement ua : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", ua.getAchievementCode());
            map.put("name", ACHIEVEMENT_NAMES.getOrDefault(ua.getAchievementCode(), ua.getAchievementCode()));
            map.put("unlockedAt", ua.getUnlockedAt());
            result.add(map);
        }
        return result;
    }

    @Override
    public IPage<User> getLeaderboard(Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        Page<User> userPage = new Page<>(pn, ps);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(User::getStatus, "banned")
                .orderByDesc(User::getExp);
        IPage<User> result = userMapper.selectPage(userPage, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }
}
