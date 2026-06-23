package com.zhiqu.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("t_user_achievement")
public class UserAchievement {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("achievement_code")
    private String achievementCode;

    @TableField("unlocked_at")
    private LocalDateTime unlockedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAchievementCode() { return achievementCode; }
    public void setAchievementCode(String achievementCode) { this.achievementCode = achievementCode; }

    public LocalDateTime getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(LocalDateTime unlockedAt) { this.unlockedAt = unlockedAt; }
}
