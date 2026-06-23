package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.entity.UserAchievement;

import java.util.List;
import java.util.Map;

public interface UserAchievementService {

    boolean checkAndUnlock(Long userId, String achievementCode);

    List<Map<String, Object>> listByUser(Long userId);

    IPage<User> getLeaderboard(Integer page, Integer pageSize);
}
