package com.zhiqu.community.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.common.Result;
import com.zhiqu.community.entity.Invitation;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.InvitationService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/api/invitations")
    public Result<Void> inviteToAnswer(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        Long questionId = Long.valueOf(body.get("questionId").toString());
        List<Long> inviteeIds = ((List<Number>) body.get("inviteeIds")).stream()
                .map(Number::longValue)
                .toList();
        invitationService.inviteToAnswer(questionId, currentUser.getUserId(), inviteeIds);
        return Result.ok("邀请已发送", null);
    }

    @GetMapping("/api/invitations/my")
    public Result<IPage<Invitation>> listMyInvitations(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "20") Integer pageSize,
                                                        HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        return Result.ok(invitationService.listMyInvitations(currentUser.getUserId(), page, pageSize));
    }

    @GetMapping("/api/questions/{id}/invitations")
    public Result<IPage<Invitation>> listQuestionInvitations(@PathVariable Long id,
                                                              @RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.ok(invitationService.listQuestionInvitations(id, page, pageSize));
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
