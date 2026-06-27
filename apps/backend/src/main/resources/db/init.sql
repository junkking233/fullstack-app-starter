CREATE DATABASE IF NOT EXISTS template_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE template_db;

CREATE TABLE IF NOT EXISTS t_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username    VARCHAR(64)  NOT NULL COMMENT '用户名',
    password    VARCHAR(32)  NOT NULL COMMENT 'MD5密码',
    nickname    VARCHAR(64)  COMMENT '昵称',
    avatar      MEDIUMTEXT   COMMENT '头像',
    bio         VARCHAR(500) COMMENT '个人简介',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/PARTNER/USER',
    status      VARCHAR(20)  NOT NULL DEFAULT 'active' COMMENT '状态: active/banned',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

INSERT IGNORE INTO t_user (username, password, nickname, role, status)
VALUES
    ('admin',   '0192023a7bbd73250516f069df18b500', '管理员',   'ADMIN',   'active'),
    ('partner', 'e10adc3949ba59abbe56e057f20f883e', '服务方',   'PARTNER', 'active'),
    ('user',    'e10adc3949ba59abbe56e057f20f883e', '普通用户', 'USER',    'active');
