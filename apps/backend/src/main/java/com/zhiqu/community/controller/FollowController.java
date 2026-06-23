package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.FollowService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/api/follows/toggle")
    public Result<Boolean> toggleFollow(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        Long followedId = Long.valueOf(body.get("followedId").toString());
        boolean followed = followService.toggleFollow(currentUser.getUserId(), followedId);
        return Result.ok(followed);
    }

    @GetMapping("/api/followers/{userId}")
    public Result<IPage<User>> listFollowers(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(followService.listFollowers(userId, page, pageSize));
    }

    @GetMapping("/api/following/{userId}")
    public Result<IPage<User>> listFollowing(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(followService.listFollowing(userId, page, pageSize));
    }

    @GetMapping("/api/follows/check")
    public Result<Boolean> checkFollow(@RequestParam Long followedId, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        boolean following = followService.isFollowing(currentUser.getUserId(), followedId);
        return Result.ok(following);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
