package com.zhiqu.community.util;

public class TokenSubject {

    private final Long userId;

    private final String username;

    private final String role;

    private final Long expiresAt;

    public TokenSubject(Long userId, String username, String role, Long expiresAt) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.expiresAt = expiresAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }
}
