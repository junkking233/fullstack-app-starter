package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.VoteService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/api/votes/toggle")
    public Result<Boolean> toggleVote(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        String targetType = (String) body.get("targetType");
        Long targetId = Long.valueOf(body.get("targetId").toString());
        boolean voted = voteService.toggleVote(currentUser.getUserId(), targetType, targetId);
        return Result.ok(voted);
    }

    @GetMapping("/api/votes/check")
    public Result<Boolean> checkVote(@RequestParam String targetType,
                                     @RequestParam Long targetId,
                                     HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        boolean voted = voteService.hasVoted(currentUser.getUserId(), targetType, targetId);
        return Result.ok(voted);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
