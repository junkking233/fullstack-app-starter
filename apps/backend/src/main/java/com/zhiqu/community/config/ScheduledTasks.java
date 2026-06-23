package com.zhiqu.community.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhiqu.community.entity.Notification;
import com.zhiqu.community.entity.Question;
import com.zhiqu.community.entity.User;
import com.zhiqu.community.mapper.NotificationMapper;
import com.zhiqu.community.mapper.QuestionMapper;
import com.zhiqu.community.mapper.UserMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    private final NotificationMapper notificationMapper;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;

    public ScheduledTasks(NotificationMapper notificationMapper,
                          QuestionMapper questionMapper,
                          UserMapper userMapper) {
        this.notificationMapper = notificationMapper;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
    }

    /**
     * Clean expired notifications older than 30 days
     * Runs daily at 2:00 AM
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanExpiredNotifications() {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(30);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(Notification::getCreatedAt, expireTime);
        notificationMapper.delete(wrapper);
    }

    /**
     * Refresh hotlist cache - mark questions as featured based on hot score
     * Runs daily at 1:00 AM
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void refreshHotlistCache() {
        // Reset all featured flags first
        Question reset = new Question();
        reset.setIsFeatured(0);
        questionMapper.update(reset, null);

        // Find top questions by activity from the last week
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getStatus, "published")
               .ge(Question::getCreatedAt, weekAgo)
               .orderByDesc(Question::getViewCount)
               .orderByDesc(Question::getAnswerCount)
               .last("LIMIT 50");
        List<Question> hotQuestions = questionMapper.selectList(wrapper);

        for (Question q : hotQuestions) {
            q.setIsFeatured(1);
            questionMapper.updateById(q);
        }
    }

    /**
     * Recalculate user levels based on exp
     * Runs daily at 4:00 AM
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void recalculateUserLevels() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            int exp = user.getExp() != null ? user.getExp() : 0;
            int level = calculateLevel(exp);
            if (user.getLevel() == null || user.getLevel() != level) {
                user.setLevel(level);
                userMapper.updateById(user);
            }
        }
    }

    private int calculateLevel(int exp) {
        if (exp < 100) return 1;
        if (exp < 300) return 2;
        if (exp < 600) return 3;
        if (exp < 1000) return 4;
        if (exp < 1500) return 5;
        if (exp < 2100) return 6;
        if (exp < 2800) return 7;
        if (exp < 3600) return 8;
        if (exp < 4500) return 9;
        return 10;
    }
}
