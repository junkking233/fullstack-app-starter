package com.zhiqu.community.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhiqu.community.entity.Notification;

public interface NotificationService {

    IPage<Notification> listByUser(Long userId, Integer page, Integer pageSize);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    void createNotification(Long userId, String type, Long sourceId, Long triggerUserId, String content);

    void cleanExpiredNotifications();
}
