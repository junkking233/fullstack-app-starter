-- ============================================================================
-- Fullstack App Starter 数据库初始化脚本
-- Database: template_db
-- Charset: utf8mb4 / utf8mb4_unicode_ci
-- Engine: InnoDB
-- Execute: docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db/db.sql
-- ============================================================================

CREATE DATABASE IF NOT EXISTS template_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE template_db;

-- ============================================================================
-- 1. 用户表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(32) NOT NULL COMMENT '密码(MD5加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar MEDIUMTEXT COMMENT '头像(Base64编码)',
    bio VARCHAR(500) COMMENT '个人简介',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/PARTNER/USER',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/banned',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================================
-- 种子数据
-- ============================================================================

-- 用户 (密码均为 MD5 加密)
-- admin/admin123 -> 0192023a7bbd73250516f069df18b500 (MD5 of admin123)
-- user/123456    -> e10adc3949ba59abbe56e057f20f883e (MD5 of 123456)

INSERT IGNORE INTO t_user (id, username, password, nickname, role, status) VALUES
(1, 'admin',   '0192023a7bbd73250516f069df18b500', '管理员',   'ADMIN',   'active'),
(2, 'user',    'e10adc3949ba59abbe56e057f20f883e', '普通用户', 'USER',    'active');
