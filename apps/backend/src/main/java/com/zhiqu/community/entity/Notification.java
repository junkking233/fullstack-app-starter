package com.zhiqu.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("t_notification")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String type;

    @TableField("source_id")
    private Long sourceId;

    @TableField("trigger_user_id")
    private Long triggerUserId;

    @TableField("is_read")
    private Integer isRead;

    private String content;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("expire_at")
    private LocalDateTime expireAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }

    public Long getTriggerUserId() { return triggerUserId; }
    public void setTriggerUserId(Long triggerUserId) { this.triggerUserId = triggerUserId; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpireAt() { return expireAt; }
    public void setExpireAt(LocalDateTime expireAt) { this.expireAt = expireAt; }
}
