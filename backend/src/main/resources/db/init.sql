CREATE DATABASE IF NOT EXISTS template_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE template_db;

CREATE TABLE IF NOT EXISTS t_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL COMMENT '用户名',
    password    VARCHAR(32)  NOT NULL COMMENT 'MD5密码',
    email       VARCHAR(128) NOT NULL COMMENT '邮箱',
    phone       VARCHAR(32)           COMMENT '手机号',
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN-管理员 USER-普通用户',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_username (username),
    UNIQUE INDEX uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

INSERT IGNORE INTO t_user (username, password, email, phone, role, status)
VALUES
    ('admin', '0192023a7bbd73250516f069df18b500', 'admin@example.com', '13800000000', 'ADMIN', 1),
    ('user', 'e10adc3949ba59abbe56e057f20f883e', 'user@example.com', '13900000000', 'USER', 1);
