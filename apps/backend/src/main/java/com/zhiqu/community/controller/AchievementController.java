package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.UserAchievementService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class AchievementController {

    private final UserAchievementService achievementService;

    public AchievementController(UserAchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping("/api/achievements/my")
    public Result<?> myAchievements(HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        return Result.ok(achievementService.listByUser(currentUser.getUserId()));
    }

    @GetMapping("/api/achievements/leaderboard")
    public Result<?> leaderboard(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(achievementService.getLeaderboard(page, pageSize));
    }

    @GetMapping("/api/users/{userId}/achievements")
    public Result<?> userAchievements(@PathVariable Long userId) {
        return Result.ok(achievementService.listByUser(userId));
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
