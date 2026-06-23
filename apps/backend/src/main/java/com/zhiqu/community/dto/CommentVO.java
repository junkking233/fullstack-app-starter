package com.zhiqu.community.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentVO {

    private Long id;

    private String content;

    private Long userId;

    private String username;

    private String nickname;

    private Long answerId;

    private Long parentId;

    private String status;

    private LocalDateTime createdAt;

    private List<CommentVO> replies = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentVO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentVO> replies) {
        this.replies = replies;
    }
}
