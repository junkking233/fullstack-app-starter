package com.zhiqu.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiqu.community.entity.Notification;
import com.zhiqu.community.exception.BusinessException;
import com.zhiqu.community.mapper.NotificationMapper;
import com.zhiqu.community.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public IPage<Notification> listByUser(Long userId, Integer page, Integer pageSize) {
        int pn = page != null ? page : 1;
        int ps = pageSize != null ? Math.min(pageSize, 50) : 20;

        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
               .orderByDesc(Notification::getCreatedAt);
        return notificationMapper.selectPage(new Page<>(pn, ps), wrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
               .eq(Notification::getIsRead, 0);
        return notificationMapper.selectCount(wrapper);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            throw new BusinessException(404, "通知不存在");
        }
        notification.setIsRead(1);
        notificationMapper.updateById(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
               .eq(Notification::getIsRead, 0)
               .set(Notification::getIsRead, 1);
        notificationMapper.update(null, wrapper);
    }

    @Override
    @Transactional
    public void createNotification(Long userId, String type, Long sourceId, Long triggerUserId, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setSourceId(sourceId);
        notification.setTriggerUserId(triggerUserId);
        notification.setIsRead(0);
        notification.setContent(content);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setExpireAt(LocalDateTime.now().plusDays(7));
        notificationMapper.insert(notification);
    }

    @Override
    @Transactional
    public void cleanExpiredNotifications() {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(Notification::getExpireAt, LocalDateTime.now());
        notificationMapper.delete(wrapper);
    }
}
