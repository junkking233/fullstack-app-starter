package com.zhiqu.community.controller;

import com.zhiqu.community.common.Result;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.service.NotificationService;
import com.zhiqu.community.util.TokenSubject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/notifications")
    public Result<?> listNotifications(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer pageSize,
                                        HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        return Result.ok(notificationService.listByUser(currentUser.getUserId(), page, pageSize));
    }

    @GetMapping("/api/notifications/unread-count")
    public Result<Long> unreadCount(HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        return Result.ok(notificationService.getUnreadCount(currentUser.getUserId()));
    }

    @PutMapping("/api/notifications/{id}/read")
    public Result<Void> markRead(@PathVariable Long id, HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        notificationService.markAsRead(id, currentUser.getUserId());
        return Result.ok(null);
    }

    @PutMapping("/api/notifications/read-all")
    public Result<Void> markAllRead(HttpServletRequest request) {
        TokenSubject currentUser = requireLogin(request);
        notificationService.markAllAsRead(currentUser.getUserId());
        return Result.ok(null);
    }

    private TokenSubject requireLogin(HttpServletRequest request) {
        Object subject = request.getAttribute("currentUser");
        if (subject instanceof TokenSubject ts) return ts;
        throw new BusinessException(401, "请先登录");
    }
}
