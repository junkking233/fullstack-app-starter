-- ============================================================================
-- 知问社区 (zhiQu Community) 数据库初始化脚本
-- Database: zhiqu_community
-- Charset: utf8mb4 / utf8mb4_unicode_ci
-- Engine: InnoDB
-- Execute: mysql -u root -padmin123 < db.sql
-- ============================================================================

CREATE DATABASE IF NOT EXISTS zhiqu_community
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE zhiqu_community;

-- ============================================================================
-- 1. 用户表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(32) NOT NULL COMMENT '密码(MD5加密)',
    nickname VARCHAR(50) COMMENT '昵称(可重复)',
    avatar MEDIUMTEXT COMMENT '头像(Base64编码,最大500KB)',
    bio VARCHAR(500) COMMENT '个人简介',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/USER',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/banned',
    exp INT NOT NULL DEFAULT 0 COMMENT '经验值(只增不减)',
    level INT NOT NULL DEFAULT 1 COMMENT '等级(1-10,根据经验值计算)',
    login_days INT NOT NULL DEFAULT 0 COMMENT '累计登录天数',
    consecutive_days INT NOT NULL DEFAULT 0 COMMENT '连续登录天数',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================================
-- 2. 用户成就表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_user_achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '成就记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    achievement_code VARCHAR(50) NOT NULL COMMENT '成就代码: registered/first_question/first_answer/first_accepted/hundred_votes/five_hundred_votes/fifty_answers/week_login/month_login/hundred_followers',
    unlocked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '解锁时间',
    UNIQUE INDEX uk_user_achievement (user_id, achievement_code),
    INDEX idx_user_id (user_id),
    CONSTRAINT fk_achievement_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就表';

-- ============================================================================
-- 3. 分类表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT NULL COMMENT '父分类ID(NULL表示顶级分类)',
    level INT NOT NULL DEFAULT 1 COMMENT '层级',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    question_count INT NOT NULL DEFAULT 0 COMMENT '问题数量',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/disabled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES t_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ============================================================================
-- 4. 标签表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    name VARCHAR(30) NOT NULL COMMENT '标签名称',
    usage_count INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    created_by BIGINT COMMENT '创建者用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX uk_name (name),
    CONSTRAINT fk_tag_creator FOREIGN KEY (created_by) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ============================================================================
-- 5. 敏感词表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_sensitive_word (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '敏感词ID',
    word VARCHAR(100) NOT NULL COMMENT '敏感词内容',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/disabled',
    created_by BIGINT COMMENT '创建者用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX uk_word (word),
    INDEX idx_status (status),
    CONSTRAINT fk_sensitive_creator FOREIGN KEY (created_by) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词表';

-- ============================================================================
-- 6. 问题表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '问题ID',
    title VARCHAR(200) NOT NULL COMMENT '问题标题',
    content TEXT COMMENT '问题内容(富文本)',
    user_id BIGINT COMMENT '提问者用户ID',
    category_id BIGINT COMMENT '所属分类ID',
    view_count INT NOT NULL DEFAULT 0 COMMENT '浏览数',
    answer_count INT NOT NULL DEFAULT 0 COMMENT '回答数',
    vote_count INT NOT NULL DEFAULT 0 COMMENT '投票数',
    favorite_count INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/published/rejected/deleted/hidden',
    is_pinned TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶',
    is_featured TINYINT NOT NULL DEFAULT 0 COMMENT '是否精选',
    audit_reason VARCHAR(500) COMMENT '审核原因',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FULLTEXT INDEX ft_title_content (title, content),
    CONSTRAINT fk_question_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL,
    CONSTRAINT fk_question_category FOREIGN KEY (category_id) REFERENCES t_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问题表';

-- ============================================================================
-- 7. 问题标签关联表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_question_tag (
    question_id BIGINT NOT NULL COMMENT '问题ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (question_id, tag_id),
    INDEX idx_tag_id (tag_id),
    CONSTRAINT fk_qt_question FOREIGN KEY (question_id) REFERENCES t_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_qt_tag FOREIGN KEY (tag_id) REFERENCES t_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问题标签关联表';

-- ============================================================================
-- 8. 邀请回答表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_question_invitation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '邀请记录ID',
    question_id BIGINT NOT NULL COMMENT '问题ID',
    inviter_id BIGINT NOT NULL COMMENT '邀请人用户ID',
    invitee_id BIGINT NOT NULL COMMENT '被邀请人用户ID',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/accepted/rejected',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '邀请时间',
    INDEX idx_question_id (question_id),
    INDEX idx_invitee_id (invitee_id),
    CONSTRAINT fk_invitation_question FOREIGN KEY (question_id) REFERENCES t_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_inviter FOREIGN KEY (inviter_id) REFERENCES t_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_invitee FOREIGN KEY (invitee_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邀请回答表';

-- ============================================================================
-- 9. 回答表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '回答ID',
    content LONGTEXT NOT NULL COMMENT '回答内容(富文本,可含Base64图片)',
    question_id BIGINT NOT NULL COMMENT '所属问题ID',
    user_id BIGINT COMMENT '回答者用户ID',
    is_accepted TINYINT NOT NULL DEFAULT 0 COMMENT '是否被采纳',
    vote_count INT NOT NULL DEFAULT 0 COMMENT '投票数',
    comment_count INT NOT NULL DEFAULT 0 COMMENT '评论数',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending/published/rejected/deleted/hidden',
    audit_reason VARCHAR(500) COMMENT '审核原因',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_question_id (question_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES t_question(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='回答表';

-- ============================================================================
-- 10. 评论表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    content VARCHAR(500) NOT NULL COMMENT '评论内容',
    user_id BIGINT COMMENT '评论者用户ID',
    answer_id BIGINT NOT NULL COMMENT '所属回答ID',
    parent_id BIGINT DEFAULT NULL COMMENT '父评论ID(NULL=顶级评论,非NULL=回复)',
    status VARCHAR(20) NOT NULL DEFAULT 'published' COMMENT '状态: published/deleted',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_answer_id (answer_id),
    INDEX idx_user_id (user_id),
    CONSTRAINT fk_comment_answer FOREIGN KEY (answer_id) REFERENCES t_answer(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES t_comment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================================================
-- 11. 投票表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_vote (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '投票ID',
    user_id BIGINT NOT NULL COMMENT '投票用户ID',
    target_type VARCHAR(20) NOT NULL COMMENT '目标类型: question/answer',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投票时间',
    UNIQUE INDEX uk_vote (user_id, target_type, target_id),
    INDEX idx_target (target_type, target_id),
    CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投票表';

-- ============================================================================
-- 12. 收藏表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '收藏用户ID',
    question_id BIGINT NOT NULL COMMENT '收藏的问题ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE INDEX uk_favorite (user_id, question_id),
    INDEX idx_user_id (user_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_question FOREIGN KEY (question_id) REFERENCES t_question(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- ============================================================================
-- 13. 关注表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_follow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关注记录ID',
    follower_id BIGINT NOT NULL COMMENT '关注者用户ID',
    followed_id BIGINT NOT NULL COMMENT '被关注者用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    UNIQUE INDEX uk_follow (follower_id, followed_id),
    INDEX idx_follower_id (follower_id),
    INDEX idx_followed_id (followed_id),
    CONSTRAINT fk_follow_follower FOREIGN KEY (follower_id) REFERENCES t_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_followed FOREIGN KEY (followed_id) REFERENCES t_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注表';

-- ============================================================================
-- 14. 举报表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '举报ID',
    reporter_id BIGINT NOT NULL COMMENT '举报人用户ID',
    target_type VARCHAR(20) NOT NULL COMMENT '目标类型: question/answer/comment',
    target_id BIGINT NOT NULL COMMENT '被举报目标ID',
    reason VARCHAR(20) NOT NULL COMMENT '举报原因: ad/porn/abuse/copyright/other',
    detail VARCHAR(500) COMMENT '举报详细描述',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '处理状态: pending/ignored/deleted_content/banned_user',
    handler_id BIGINT COMMENT '处理人用户ID',
    result_note VARCHAR(500) COMMENT '处理结果说明',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
    handled_at DATETIME COMMENT '处理时间',
    UNIQUE INDEX uk_report (reporter_id, target_type, target_id),
    INDEX idx_status (status),
    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES t_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_handler FOREIGN KEY (handler_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报表';

-- ============================================================================
-- 15. 通知表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '接收通知的用户ID',
    type VARCHAR(30) NOT NULL COMMENT '通知类型: new_answer/comment/follow/accepted/audit_pass/audit_reject/invite/report_result/achievement/system',
    source_id BIGINT COMMENT '来源ID',
    trigger_user_id BIGINT COMMENT '触发用户ID',
    is_read TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读',
    content VARCHAR(500) COMMENT '通知内容',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    expire_at DATETIME NOT NULL COMMENT '过期时间(创建后7天)',
    INDEX idx_user_id_is_read (user_id, is_read),
    INDEX idx_expire_at (expire_at),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_trigger FOREIGN KEY (trigger_user_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- ============================================================================
-- 16. 操作日志表
-- ============================================================================
CREATE TABLE IF NOT EXISTS t_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    action VARCHAR(50) NOT NULL COMMENT '操作动作',
    target_type VARCHAR(30) COMMENT '目标类型',
    target_id BIGINT COMMENT '目标ID',
    detail TEXT COMMENT '操作详情',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ============================================================================
-- SEED DATA - 种子数据
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Users (3 users)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_user (id, username, password, nickname, bio, role, status, exp, level, login_days, consecutive_days, created_at, updated_at) VALUES
(1, 'admin', '0192023a7bbd73250516f069df18b500', '社区管理员',    '知问社区官方管理员',                              'ADMIN', 'active', 5000, 10, 365, 120, '2024-01-01 08:00:00', '2024-07-15 10:30:00'),
(2, 'userA', 'e10adc3949ba59abbe56e057f20f883e', '阿澄',        '热爱技术，专注于前端开发和用户体验设计，喜欢分享知识。', 'USER',  'active', 850,   4,  58,  14, '2024-05-18 09:15:00', '2024-07-14 22:10:00'),
(3, 'userB', 'e10adc3949ba59abbe56e057f20f883e', '北辰',        '',                                                            'USER',  'active', 620,   3,  42,   7, '2024-05-25 14:20:00', '2024-07-13 18:45:00');

-- ----------------------------------------------------------------------------
-- Categories (3 top-level + 9 sub-categories = 12)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_category (id, name, parent_id, level, sort_order, question_count, status, created_at) VALUES
-- 顶级分类
(1,  '技术学习', NULL, 1, 1, 8, 'active', '2024-01-01 08:00:00'),
(2,  '校园生活', NULL, 1, 2, 5, 'active', '2024-01-01 08:00:00'),
(3,  '知识百科', NULL, 1, 3, 5, 'active', '2024-01-01 08:00:00'),
-- 技术学习子分类
(4,  '前端开发', 1, 2, 1, 4, 'active', '2024-01-01 08:00:00'),
(5,  '后端开发', 1, 2, 2, 3, 'active', '2024-01-01 08:00:00'),
(6,  '数据库',   1, 2, 3, 2, 'active', '2024-01-01 08:00:00'),
-- 校园生活子分类
(7,  '学习经验', 2, 2, 1, 2, 'active', '2024-01-01 08:00:00'),
(8,  '就业指导', 2, 2, 2, 2, 'active', '2024-01-01 08:00:00'),
(9,  '心理辅导', 2, 2, 3, 1, 'active', '2024-01-01 08:00:00'),
-- 知识百科子分类
(10, '英语学习', 3, 2, 1, 2, 'active', '2024-01-01 08:00:00'),
(11, '算法入门', 3, 2, 2, 2, 'active', '2024-01-01 08:00:00'),
(12, '工具推荐', 3, 2, 3, 1, 'active', '2024-01-01 08:00:00');

-- ----------------------------------------------------------------------------
-- Tags (10 tags)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_tag (id, name, usage_count, created_by, created_at) VALUES
(1,  'Vue',          15, 1, '2024-06-01 10:00:00'),
(2,  'Spring Boot',  10, 1, '2024-06-01 10:00:00'),
(3,  'MySQL',         8, 1, '2024-06-01 10:00:00'),
(4,  'Markdown',      5, 2, '2024-06-05 14:00:00'),
(5,  '算法',         12, 1, '2024-06-01 10:00:00'),
(6,  '考研',          7, 2, '2024-06-05 14:00:00'),
(7,  '就业',          9, 2, '2024-06-05 14:00:00'),
(8,  '生活',          6, 3, '2024-06-10 09:00:00'),
(9,  '心理',          4, 3, '2024-06-10 09:00:00'),
(10, '英语',          5, 3, '2024-06-10 09:00:00');

-- ----------------------------------------------------------------------------
-- Sensitive Words (16 words)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_sensitive_word (id, word, status, created_by, created_at) VALUES
(1,  '代考',            'active', 1, '2024-06-01 08:00:00'),
(2,  '代写',            'active', 1, '2024-06-01 08:00:00'),
(3,  '作弊器',          'active', 1, '2024-06-01 08:00:00'),
(4,  '办证',            'active', 1, '2024-06-01 08:00:00'),
(5,  '广告推广加微信',  'active', 1, '2024-06-01 08:00:00'),
(6,  '添加QQ群',        'active', 1, '2024-06-01 08:00:00'),
(7,  '兼职日结',        'active', 1, '2024-06-01 08:00:00'),
(8,  '刷单返利',        'active', 1, '2024-06-01 08:00:00'),
(9,  '淫秽',            'active', 1, '2024-06-01 08:00:00'),
(10, '色情网站',        'active', 1, '2024-06-01 08:00:00'),
(11, '傻逼',            'active', 1, '2024-06-01 08:00:00'),
(12, '垃圾玩意',        'active', 1, '2024-06-01 08:00:00'),
(13, '骗子',            'active', 1, '2024-06-01 08:00:00'),
(14, '传销',            'active', 1, '2024-06-01 08:00:00'),
(15, '赌博网站',        'active', 1, '2024-06-01 08:00:00'),
(16, '裸聊',            'active', 1, '2024-06-01 08:00:00');

-- ----------------------------------------------------------------------------
-- Questions (21 questions: 14 published, 3 pending, 2 rejected, 2 deleted)
-- Among published: 3 featured (#1,#4,#8), 1 pinned (#3)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_question (id, title, content, user_id, category_id, view_count, answer_count, vote_count, favorite_count, status, is_pinned, is_featured, audit_reason, created_at, updated_at) VALUES
-- === Published Questions ===
(1, 'Vue3中Composition API和Options API有什么区别？应该怎么选择？',
    '最近在学习Vue3，发现有两种写组件的方式：Composition API（setup语法糖）和传统的Options API。我有点困惑，它们在实际开发中有什么区别？什么场景下应该用哪种？希望有经验的前端开发者帮忙解答一下，特别是关于代码复用和TypeScript支持方面的对比。',
    1, 4, 1520, 2, 45, 8, 'published', 0, 1, NULL, '2024-06-01 09:00:00', '2024-07-12 16:30:00'),

(2, 'Spring Boot项目中如何优雅地处理全局异常？',
    '在开发Spring Boot项目时，经常会遇到各种异常情况，比如参数校验失败、业务逻辑异常、系统错误等。目前我在每个Controller里都用try-catch包裹，代码看起来很冗余。想请教一下如何设计一个优雅的全局异常处理方案，包括统一的错误码定义和返回格式？',
    2, 5, 980, 2, 28, 5, 'published', 0, 0, NULL, '2024-06-03 10:30:00', '2024-07-10 09:15:00'),

(3, 'MySQL索引优化有哪些常见策略？如何避免索引失效？',
    '在实际项目中遇到了数据库查询性能问题，虽然加了索引但效果不明显。想系统地了解MySQL索引优化的策略，包括联合索引的最左前缀原则、覆盖索引、索引下推等概念。另外有没有一些常见的会导致索引失效的写法需要注意？',
    3, 6, 2100, 2, 52, 12, 'published', 1, 0, NULL, '2024-06-05 08:00:00', '2024-07-15 10:00:00'),

(4, 'Vue项目中状态管理用Vuex还是Pinia？各自的优缺点是什么？',
    '开始一个新项目，纠结状态管理方案选型。Vuex是Vue2时代的标配但现在官方推荐Pinia了。我看Pinia支持更好的TypeScript类型推断，但不知道实际项目中稳定性如何。有比较过两者的大佬分享一下使用心得吗？',
    2, 4, 1350, 2, 38, 6, 'published', 0, 1, NULL, '2024-06-07 14:00:00', '2024-07-11 20:45:00'),

(5, 'Spring Boot微服务架构中服务间通信最佳实践',
    '团队正在从单体架构向微服务转型，需要确定服务间通信方案。目前考虑了三种：REST API同步调用、RabbitMQ/Kafka异步消息、gRPC高性能调用。但不知道在实际生产环境中应该优先选用哪种方式，以及如何规避分布式事务的问题。',
    1, 5, 750, 1, 18, 4, 'published', 0, 0, NULL, '2024-06-10 11:00:00', '2024-07-08 14:20:00'),

(6, '大学生如何高效准备考研？有什么好的复习方法？',
    '大三下学期了，准备开始复习考研。数学和专业课感觉基础不够扎实，英语词汇量也有点欠缺。想问一下已经上岸的学长学姐们，有没有好的复习规划和资料推荐？特别是时间安排这块，每天应该投入多少小时比较合理？',
    3, 7, 890, 1, 22, 5, 'published', 0, 0, NULL, '2024-06-12 16:00:00', '2024-07-05 10:30:00'),

(7, '2024年互联网行业前端开发就业前景如何？',
    '马上要秋招了，正在准备前端开发方向。但最近经常看到互联网裁员的消息，有点焦虑。想了解2024年前端开发的真实就业情况，对Vue/React技术栈的需求量怎么样，应届生需要具备什么水平才能拿到满意的offer？',
    2, 8, 1100, 2, 30, 7, 'published', 0, 0, NULL, '2024-06-15 09:30:00', '2024-07-13 17:00:00'),

(8, 'LeetCode刷题顺序推荐，新手应该从哪些题目开始？',
    '准备开始系统地刷LeetCode为校招做准备，但面对2000多道题目有点不知道从何下手。想请教有经验的过来人，有没有推荐的刷题路线？按题型分类（数组、链表、二叉树、动态规划）刷比较好，还是按难度递增从简单到困难刷？',
    1, 11, 1680, 2, 48, 10, 'published', 0, 1, NULL, '2024-06-18 08:00:00', '2024-07-14 12:00:00'),

(9, '备考英语六级有什么高效的单词记忆方法？',
    '下学期要考六级了，感觉词汇量远远不够。试过用百词斩和墨墨背单词，但总是背了忘、忘了背，效率很低。有没有更好的记单词方法？比如通过阅读英文文章来记、用词根词缀法之类的？另外六级高频词大概有多少需要掌握？',
    3, 10, 630, 1, 15, 4, 'published', 0, 0, NULL, '2024-06-20 13:00:00', '2024-07-07 19:30:00'),

(10, '推荐几个提高开发效率的VS Code插件',
    '最近换到了VS Code作为主力编辑器，想配置一下开发环境。除了基础的语法高亮和代码格式化，有没有什么冷门但好用的插件推荐？比如自动生成注释、Git可视化、REST API测试之类的。WebStorm老用户转过来的，求安利！',
    2, 12, 920, 2, 25, 6, 'published', 0, 0, NULL, '2024-06-22 10:00:00', '2024-07-09 15:00:00'),

(11, 'MySQL事务隔离级别详解及实际应用场景',
    '对MySQL的四种事务隔离级别（READ UNCOMMITTED、READ COMMITTED、REPEATABLE READ、SERIALIZABLE）一直理解得不够深入。想详细了解每种隔离级别解决和不能解决的问题（脏读、不可重复读、幻读），以及在Spring应用中通过@Transactional注解如何配置不同的隔离级别？',
    1, 6, 680, 1, 20, 3, 'published', 0, 0, NULL, '2024-06-25 09:00:00', '2024-07-06 11:00:00'),

(12, '大学生如何克服拖延症？',
    '每次快到考试周才开始复习，作业也是拖到最后一刻才写。明明知道这样不好，但就是控制不住自己想刷手机、看视频。尝试过番茄工作法和Forest App，坚持不了几天就放弃了。想请教有没有从根本上改善拖延症的方法或者心理调整的技巧？',
    3, 9, 540, 2, 12, 3, 'published', 0, 0, NULL, '2024-06-28 15:00:00', '2024-07-04 08:30:00'),

(20, '计算机专业应届生简历应该怎么写才能脱颖而出？',
    '马上开始投秋招简历了，但是感觉简历上项目经历不够亮眼。学校做的课设感觉太简单，不知道该不该写上去。另外GPA、竞赛经历、实习经历这些在简历中的重要性排序是怎样的？有没有简历模板可以推荐的？',
    3, 8, 780, 2, 18, 4, 'published', 0, 0, NULL, '2024-06-26 14:00:00', '2024-07-12 09:45:00'),

(21, '什么是动态规划？如何培养动态规划的思维？',
    '学算法学到动态规划这一块卡住了很久。能理解斐波那契数列这种简单的例子，但一到背包问题、最长公共子序列就完全看不懂状态转移方程是怎么推导出来的。有没有什么好的学习方法？刷多少道题才能培养出DP的思维模式？',
    2, 11, 1050, 2, 28, 7, 'published', 0, 0, NULL, '2024-06-14 10:00:00', '2024-07-08 16:00:00'),

-- === Pending Questions ===
(13, 'React和Vue哪个更适合大型项目开发？',
    '公司准备开始一个新的中大型项目，前端技术选型在React和Vue之间纠结。React有更好的生态和社区，Vue上手更快开发效率高。想听听两个框架都深入用过的大佬的看法，从代码可维护性、性能优化、团队协作等角度分析一下。',
    2, 4, 0, 0, 0, 0, 'pending', 0, 0, NULL, '2024-07-01 09:00:00', '2024-07-01 09:00:00'),

(14, 'Docker容器化部署Spring Boot项目的完整流程',
    '想把现有的Spring Boot项目容器化部署，之前都是直接打成jar包在服务器上运行。现在想用Docker + Docker Compose来管理，包括MySQL、Redis等服务。有没有完整的教程或最佳实践推荐？需要注意哪些坑？',
    3, 5, 0, 0, 0, 0, 'pending', 0, 0, NULL, '2024-07-02 11:00:00', '2024-07-02 11:00:00'),

(15, 'Redis缓存穿透、击穿、雪崩的区别和解决方案',
    '面试经常被问到这三个概念，虽然大概知道意思但总觉得回答得不够深入。想系统地学习一下：缓存穿透用布隆过滤器、缓存击穿用互斥锁、缓存雪崩用随机过期时间。但这些方案具体的实现细节是什么样的？有没有生产环境的最佳实践？',
    1, 6, 0, 0, 0, 0, 'pending', 0, 0, NULL, '2024-07-03 14:00:00', '2024-07-03 14:00:00'),

-- === Rejected Questions ===
(16, '有没有人会修电脑？',
    '我的笔记本电脑开机后屏幕一直黑屏，风扇在转但是不显示任何东西。插了外接显示器也没用。有没有大佬知道这是怎么回事？是不是主板坏了？急求帮助！',
    2, 4, 0, 0, 0, 0, 'rejected', 0, 0, '问题与社区定位不符，建议到硬件维修相关论坛求助', '2024-06-08 20:00:00', '2024-06-09 10:00:00'),

(17, '有没有快速通过四六级的方法？',
    '快考试了还没复习，求一个快速过六级的方法，有没有什么捷径可以走？只要能过就行，不要求高分。',
    3, 10, 0, 0, 0, 0, 'rejected', 0, 0, '问题存在投机取巧倾向，不符合社区倡导的学习精神', '2024-06-11 22:00:00', '2024-06-12 09:00:00'),

-- === Deleted Questions ===
(18, '今天食堂的饭好难吃',
    '二食堂的红烧肉太咸了，青菜也没洗干净。',
    3, 1, 45, 0, 0, 0, 'deleted', 0, 0, NULL, '2024-06-16 12:30:00', '2024-06-16 18:00:00'),

(19, '有没有人和我一起组队打游戏',
    '玩LOL，求组队上分，我黄金段位，主玩打野位。有一起的加我好友，每天晚上都在线。',
    1, 1, 120, 0, 0, 0, 'deleted', 0, 0, NULL, '2024-06-19 21:00:00', '2024-06-20 08:00:00');

-- ----------------------------------------------------------------------------
-- Question-Tag Relations
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_question_tag (question_id, tag_id) VALUES
-- Q1: Vue3 Composition API
(1, 1),
-- Q2: Spring Boot全局异常
(2, 2),
-- Q3: MySQL索引优化
(3, 3), (3, 4),
-- Q4: Vuex vs Pinia
(4, 1),
-- Q5: Spring Boot微服务
(5, 2),
-- Q6: 考研复习
(6, 6), (6, 7),
-- Q7: 前端就业
(7, 1), (7, 7),
-- Q8: LeetCode刷题
(8, 5),
-- Q9: 英语六级
(9, 10),
-- Q10: VS Code插件
(10, 4),
-- Q11: MySQL事务
(11, 3),
-- Q12: 拖延症
(12, 9), (12, 8),
-- Q13: React vs Vue
(13, 1),
-- Q14: Docker部署
(14, 2),
-- Q15: Redis缓存
(15, 3),
-- Q20: 简历
(20, 7), (20, 8),
-- Q21: 动态规划
(21, 5);

-- ----------------------------------------------------------------------------
-- Question Invitations (3 records)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_question_invitation (id, question_id, inviter_id, invitee_id, status, created_at) VALUES
(1, 2, 2, 1, 'accepted', '2024-06-03 11:00:00'),
(2, 7, 2, 3, 'accepted', '2024-06-15 10:00:00'),
(3, 13, 2, 1, 'pending',  '2024-07-01 09:30:00');

-- ----------------------------------------------------------------------------
-- Answers (30+ answers)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_answer (id, content, question_id, user_id, is_accepted, vote_count, comment_count, status, audit_reason, created_at, updated_at) VALUES
-- Answers for Q1 (Vue3 Composition API) - 2 answers
(1, 'Composition API和Options API的核心区别在于代码组织方式。Options API按选项类型组织（data、methods、computed分开），而Composition API按逻辑关注点组织，可以把相关逻辑放在一起。\n\n对于中小型项目，Options API足够简洁易懂，新手友好。但当组件逻辑变复杂时，Options API会让同一功能的代码分散在不同选项中，难以维护。\n\nComposition API的优势：一是更好的逻辑复用（通过组合函数），二是更好的TypeScript支持，三是更灵活的代码组织。建议新项目直接用Composition API，老项目逐步迁移即可。',
    1, 2, 0, 32, 2, 'published', NULL, '2024-06-02 10:00:00', '2024-07-12 16:00:00'),

(2, '补充一下，Composition API的setup语法糖确实写起来很舒服。我个人经历是从Options API转到setup script，刚开始不太适应ref和reactive的写法，但用了一周就回不去了。特别是在封装自定义hooks时，比Vue2的mixin优雅太多了，不会有命名冲突的问题。\n\n当然如果是简单的表单页面，两种写法区别不大。如果是从Vue2迁移的项目，用Options API也完全没问题，Vue3完全兼容。',
    1, 3, 1, 18, 1, 'published', NULL, '2024-06-02 15:00:00', '2024-07-10 20:00:00'),

-- Answers for Q2 (Spring Boot异常处理) - 2 answers
(3, '推荐使用@RestControllerAdvice注解来实现全局异常处理，这是Spring Boot最优雅的方案。具体做法：\n\n1. 定义一个统一的响应体类，包含code、message、data字段\n2. 创建全局异常处理器，用@ExceptionHandler捕获不同类型的异常\n3. 自定义业务异常类，携带错误码和消息\n4. 对于参数校验异常，处理MethodArgumentNotValidException\n\n这样Controller层就完全不需要try-catch了，代码非常清爽。',
    2, 1, 1, 25, 2, 'published', NULL, '2024-06-04 09:00:00', '2024-07-10 09:00:00'),

(4, '我再补充几点实践中的经验：\n\n1. 错误码建议分模块定义，比如10000-19999是用户模块，20000-29999是订单模块\n2. 异常信息建议做成枚举类统一管理，方便维护\n3. 记得在@ExceptionHandler中打印完整堆栈日志，方便排查问题\n4. 对外的错误消息要友好，不要暴露数据库字段名等敏感信息\n\n另外建议结合Spring Validation做参数校验，在DTO上加@NotBlank、@Email等注解，这样异常处理更加统一。',
    2, 3, 0, 8, 0, 'published', NULL, '2024-06-04 14:00:00', '2024-07-09 18:00:00'),

-- Answers for Q3 (MySQL索引优化) - 2 answers
(5, '关于MySQL索引优化，建议从以下几个方面入手：\n\n**最左前缀原则**：联合索引(a,b,c)在查询条件包含a、a+b、a+b+c时都能用到索引，但跳过a直接查b则无法使用。\n\n**常见索引失效场景**：\n1. WHERE子句中对索引列使用函数或计算，如WHERE YEAR(create_time)=2024\n2. 使用LIKE以%开头，如LIKE "%keyword"\n3. 隐式类型转换，如字符串列用数字比较\n4. OR条件中有一边没有索引\n5. 使用!=或<>操作符\n\n建议定期用EXPLAIN分析慢查询，看是否走了索引。',
    3, 1, 0, 38, 3, 'published', NULL, '2024-06-06 09:00:00', '2024-07-14 15:00:00'),

(6, '补充下覆盖索引的概念，这个在优化中非常实用。如果查询的所有列都在索引中（覆盖索引），MySQL可以直接从索引返回数据而不用回表查询，性能提升很大。\n\n比如SELECT name,age FROM users WHERE name="张三"，如果有(name,age)的联合索引就是覆盖索引。\n\n另外索引下推(ICP)是MySQL5.6后的特性，可以在索引遍历过程中过滤掉不满足条件的记录，减少回表次数。这两个技术配合使用效果更佳。',
    3, 2, 1, 20, 1, 'published', NULL, '2024-06-06 14:00:00', '2024-07-13 10:00:00'),

-- Answers for Q4 (Vuex vs Pinia) - 2 answers
(7, 'Pinia已经是Vue官方推荐的状态管理库了，对于新项目我强烈建议直接用Pinia。\n\n主要优势：\n1. 完全支持TypeScript，类型推断非常准确\n2. 去掉了mutations，直接用actions修改状态，减少了样板代码\n3. 支持多个store实例，代码模块化更好\n4. 体积更小，约1KB\n5. 支持Vue DevTools调试\n\nVuex4虽然也支持Vue3，但感觉API设计还是延续了Vue2的风格，写起来比较繁琐。我们团队从去年开始所有新项目都用Pinia了。',
    4, 1, 0, 28, 2, 'published', NULL, '2024-06-08 10:00:00', '2024-07-11 19:00:00'),

(8, '说下我的实际体验，之前维护一个用Vuex4的老项目，每个store文件都有一堆mutations，明明就是简单的赋值也要写一个mutation函数，真的心累。\n\n后来用Pinia重构了，代码量直接减少约40%。尤其是跨组件状态共享的场景，Pinia的写法非常直观。不过有一点需要注意，Pinia的$patch方法在批量更新时很好用，但要小心对象嵌套过深的情况。\n\n总结：新项目无脑Pinia，老项目看维护成本决定是否迁移。',
    4, 3, 0, 12, 0, 'published', NULL, '2024-06-08 16:00:00', '2024-07-12 14:00:00'),

-- Answers for Q5 (Spring Boot微服务通信) - 1 answer
(9, '不同场景适合不同的通信方式：\n\n1. **同步REST API**：适合查询类操作、对外API，简单直接但耦合度高\n2. **异步消息(MQ)**：适合事件驱动、数据同步等场景，解耦性好但引入消息中间件的复杂度\n3. **gRPC**：适合内部高性能服务间调用，二进制协议性能优秀但调试不便\n\n实际项目中通常是混合使用。比如我们团队的做法：对外统一用REST API，内部核心链路的实时调用用gRPC，数据同步和异步任务用RabbitMQ。\n\n关于分布式事务，建议优先考虑最终一致性方案（如Saga模式），而非强一致性的两阶段提交。',
    5, 2, 0, 16, 1, 'published', NULL, '2024-06-11 14:00:00', '2024-07-08 12:00:00'),

-- Answers for Q6 (考研准备) - 1 answer
(10, '作为一个已经上岸的过来人，分享几点经验：\n\n**时间规划**：建议从大三下学期开始，前期每天4-6小时，暑假开始每天8-10小时，最后冲刺阶段10-12小时。但重点是效率而不是时长。\n\n**各科建议**：\n- 数学：跟一个老师的课系统学，刷完李永乐660题和张宇1000题基础篇\n- 英语：每天背50个单词，精读2篇真题阅读\n- 政治：9月开始也不晚，重点背肖四肖八\n- 专业课：找目标院校的真题，针对性复习\n\n最重要的是保持规律作息和良好心态，适当运动缓解压力。',
    6, 1, 0, 18, 1, 'published', NULL, '2024-06-14 09:00:00', '2024-07-05 09:00:00'),

-- Answers for Q7 (前端就业前景) - 2 answers
(11, '作为去年刚通过校招入职的前端开发，客观说一下2024年的情况。确实比前两年竞争更激烈了，但并非没有机会。\n\n**技术要求有所提高**：不再是会HTML+CSS+JS就能找工作。现在普遍要求：\n1. 熟练掌握Vue或React框架及其生态\n2. 了解TypeScript\n3. 有至少一个拿得出手的项目经历\n4. 了解基本的构建工具（Vite/Webpack）\n5. 计算机基础扎实（网络、算法）\n\n**薪资方面**：一线城市应届生白菜价在12-18k，SP offer可以到20-25k，SSP可以超过30k。',
    7, 1, 1, 30, 3, 'published', NULL, '2024-06-16 10:00:00', '2024-07-13 16:00:00'),

(12, '补充我的看法：虽然市场在降温，但优秀的前端工程师依然供不应求。关键在于差异化竞争。\n\n建议在校期间多做一些有深度的项目，比如：\n- 自己封装一套组件库并发布到NPM\n- 深入研究浏览器渲染原理写一篇技术博客\n- 参与开源项目贡献代码\n\n另外学一些后端知识（Node.js/Go）也会加分不少，全栈能力在中小公司非常吃香。还有多看看React，虽然国内Vue用得多人也多，但大厂React岗更多一些。',
    7, 3, 0, 12, 0, 'published', NULL, '2024-06-17 09:00:00', '2024-07-10 14:00:00'),

-- Answers for Q8 (LeetCode刷题) - 2 answers
(13, '推荐一个比较科学的刷题路线：\n\n**第一阶段（1-2周）**：数据结构基础\n- 数组：两数之和、三数之和、移动零\n- 链表：反转链表、环形链表、合并有序链表\n- 栈/队列：有效的括号、用栈实现队列\n\n**第二阶段（2-4周）**：中级算法\n- 二叉树：前中后序遍历（递归+迭代）、层序遍历、最大深度\n- 回溯：全排列、子集、组合总和\n- 动态规划：爬楼梯、最大子序和、打家劫舍\n\n**第三阶段（长期）**：按tag刷高频题\n- 建议参加LeetCode的每日一题\n- 面试前重点刷Hot 100和剑指Offer\n\n关键不在于刷了多少题，而是每道题是否真正理解了解题思路。',
    8, 2, 0, 32, 2, 'published', NULL, '2024-06-19 10:00:00', '2024-07-14 11:00:00'),

(14, '我从另一个角度说，建议搭配着分类笔记来刷题。每做完一道题就总结一下：\n1. 这道题考察了什么知识点\n2. 最优解的时间/空间复杂度\n3. 有没有类似的题目可以对比\n\n推荐用Notion或语雀做笔记，按题型分类整理。另外代码随想录这个网站对新手非常友好，卡尔讲的思路特别清晰。\n\n如果基础很薄弱，建议先从剑指Offer开始，每一题都吃透，再去挑战LeetCode。',
    8, 3, 1, 18, 1, 'published', NULL, '2024-06-20 09:00:00', '2024-07-13 20:00:00'),

-- Answers for Q9 (英语六级) - 1 answer
(15, '六级单词记忆的核心是重复和语境化。推荐以下方法：\n\n1. **词根词缀法**：掌握常见的200个词根词缀，可以举一反三，比如"spect"表示"看"，就记住了inspect、respect、prospect、spectacle等词\n\n2. **阅读记忆法**：每天读一篇经济学人或China Daily，遇到生词不要立刻查，先根据上下文猜意思，读完再查字典。这样记忆更深刻\n\n3. **艾宾浩斯遗忘曲线**：背完的单词要在1天、3天、7天、30天后分别复习\n\n4. **六级高频词约2000个**，不需要背几万个单词，核心词汇掌握好基本就够用了\n\n推荐App：墨墨背单词配合Anki自制卡片效果最好。',
    9, 1, 0, 12, 1, 'published', NULL, '2024-06-22 14:00:00', '2024-07-07 18:00:00'),

-- Answers for Q10 (VS Code插件) - 2 answers
(16, '分享一下我的VS Code必备插件清单：\n\n1. **GitLens**：Git可视化神器，可以看每行代码的修改历史和作者\n2. **Thunder Client**：轻量级API测试工具，比Postman更轻便\n3. **Todo Tree**：把代码里的TODO注释汇总成树形视图，不会遗漏待办事项\n4. **Error Lens**：把错误信息直接显示在代码行尾，不用鼠标悬停\n5. **Better Comments**：让注释支持彩色高亮，区分警告、问题、待办等\n6. **Auto Rename Tag**：改一个标签自动同步修改闭合标签\n\n前端特别推荐Volar（Vue）、ES7+ React Snippets和Prettier。',
    10, 1, 0, 22, 2, 'published', NULL, '2024-06-23 10:00:00', '2024-07-09 14:00:00'),

(17, '补充一些偏门但好用的：\n\n1. **CodeSnap**：一键生成漂亮的代码截图，写博客神器\n2. **Image Preview**：在编辑器里预览图片链接，不用切到浏览器\n3. **Project Manager**：多项目切换的福音，支持保存工作区状态\n4. **WakaTime**：统计编程时间，看你在哪个项目上花了最多时间\n\n另外推荐一套图标主题Material Icon Theme，文件类型一眼就能分辨。常用的快捷键记住alt+上下箭头移动整行代码，ctrl+D选中下一个相同单词，这些对效率提升很有帮助。',
    10, 3, 0, 10, 0, 'published', NULL, '2024-06-23 16:00:00', '2024-07-10 11:00:00'),

-- Answers for Q11 (MySQL事务隔离级别) - 1 answer
(18, 'MySQL默认隔离级别是REPEATABLE READ，下面详细解释每个级别：\n\n**READ UNCOMMITTED（读未提交）**\n- 问题：脏读、不可重复读、幻读都会发生\n- 几乎不用于生产环境\n\n**READ COMMITTED（读已提交）**\n- 解决：脏读\n- 存在：不可重复读、幻读\n- Oracle默认级别，每次查询读取最新已提交数据\n\n**REPEATABLE READ（可重复读）**\n- 解决：脏读、不可重复读\n- 存在：幻读（但MySQL通过间隙锁部分解决）\n- MySQL InnoDB默认级别，同一个事务内多次查询结果一致\n\n**SERIALIZABLE（串行化）**\n- 解决：所有问题\n- 性能最差，读写互斥\n\n在Spring中通过@Transactional(isolation=Isolation.REPEATABLE_READ)配置。',
    11, 2, 0, 18, 1, 'published', NULL, '2024-06-26 10:00:00', '2024-07-06 10:00:00'),

-- Answers for Q12 (拖延症) - 2 answers
(19, '作为曾经重度拖延的人，分享几个从心理学角度有效的方法：\n\n1. **5分钟法则**：告诉自己只做5分钟，通常开始后就会进入状态。拖延的最大敌人就是"开始"\n\n2. **任务拆解法**：把大任务拆分成一个个能在25-30分钟内完成的小任务，每完成一个就划掉，有成就感\n\n3. **环境设计**：把手机放到另一个房间或用Forest锁住，减少诱惑源\n\n4. **社交监督**：找一个学习伙伴互相打卡，或者在学习群里每天汇报进度\n\n5. **接纳不完美**：很多拖延源于完美主义，允许自己做得不完美，做了比不做好\n\n如果这些方法都不管用，建议去学校心理咨询中心聊聊，拖延有时是焦虑或抑郁的表现。',
    12, 1, 0, 10, 2, 'published', NULL, '2024-06-29 10:00:00', '2024-07-04 07:30:00'),

(20, '我推荐一个小技巧叫"吃青蛙法"：每天早上先做最不想做的那件事。把它当做一天中第一件要做的事，做完后其他事情都显得轻松了。\n\n另外推荐一本书《原子习惯》，里面提到习惯养成的四个法则：让它显而易见、让它有吸引力、让它简单易行、让它令人满足。应用到克服拖延上：\n\n- 显而易见：前一天晚上就把学习资料摆在桌上\n- 有吸引力：把学习和喜欢的事情绑定（比如在喜欢的咖啡馆学习）\n- 简单易行：设定很小的目标（今天只读一页书）\n- 令人满足：完成任务后给自己小奖励',
    12, 3, 1, 8, 0, 'published', NULL, '2024-06-30 09:00:00', '2024-07-03 16:00:00'),

-- Answers for Q20 (计算机简历) - 2 answers
(21, '写了几年代码也面了不少人，从面试官角度给几点建议：\n\n**简历结构**：\n1. 个人信息（姓名、联系方式、期望岗位）\n2. 教育背景（学校、专业、GPA如果3.5以上可以写）\n3. 技术栈（按熟练程度排列，别写"精通"除非你真的是）\n4. 项目经历（2-3个最有深度的）\n5. 竞赛/证书/开源贡献\n\n**项目经历是重中之重**，STAR法则来写：背景是什么、你做了什么、用了什么技术、取得了什么成果（最好有数据）。\n\n**GPA和竞赛**：大厂简历筛选时GPA是加分项但不是必须项，如果GPA不高可以不写。ACM/蓝桥杯等编程竞赛奖项含金量很高，建议写上。',
    20, 1, 0, 22, 2, 'published', NULL, '2024-06-27 10:00:00', '2024-07-11 18:00:00'),

(22, '补充一下项目经历怎么写。不要只写"使用Spring Boot开发了XX系统"，而要具体：\n\n好的例子：\n"基于Spring Boot+MyBatis+Redis开发电商订单系统，负责核心的订单模块设计，通过缓存预热和异步消息将下单响应时间从500ms优化到100ms，日均处理订单量2000+"\n\n差的例子：\n"参与了一个电商项目，主要负责订单模块的后端开发"\n\n另外GitHub链接一定要放，而且代码要整洁有readme。没有实习经历的话，个人项目的深度就特别重要。建议做一个从0到1的完整项目上线，比10个半成品都有说服力。',
    20, 2, 1, 12, 0, 'published', NULL, '2024-06-28 09:00:00', '2024-07-12 08:00:00'),

-- Answers for Q21 (动态规划) - 2 answers
(23, '动态规划确实是一个需要时间消化的知识点。核心思想就八个字：**分解子问题，避免重复计算**。\n\n推荐学习路径：\n\n**第一步（理解概念）**：从斐波那契数列入手，理解递归→记忆化搜索→DP递推的演进过程\n\n**第二步（经典题型）**：\n- 爬楼梯问题（最基本的DP）\n- 不同路径\n- 打家劫舍\n- 最大子序和\n\n**第三步（背包系列）**：\n- 0-1背包\n- 完全背包\n- 多重背包\n\n**第四步（进阶）**：\n- 最长递增子序列\n- 最长公共子序列\n- 编辑距离\n\n核心是搞清楚DP五部曲：定义dp数组含义→推导递推公式→初始化→确定遍历顺序→举例推导验证。',
    21, 1, 0, 26, 2, 'published', NULL, '2024-06-15 10:00:00', '2024-07-08 15:00:00'),

(24, '说一个我自己悟出来的理解DP的方法：**DP的本质是在一张表格上填数字，每个格子的值由前面格子的值推出来**。\n\n比如最长公共子序列，想象一个二维表格dp[i][j]表示text1前i个字符和text2前j个字符的LCS长度：\n- 如果text1[i]==text2[j]，dp[i][j]=dp[i-1][j-1]+1\n- 否则，dp[i][j]=max(dp[i-1][j], dp[i][j-1])\n\n建议在纸上画出dp表格，手动画一遍推导过程，比纯看代码理解得快很多。\n\n另外推荐labuladong的算法小抄和代码随想录，动态规划部分讲得非常透彻。',
    21, 3, 1, 14, 0, 'published', NULL, '2024-06-16 10:00:00', '2024-07-09 12:00:00'),

-- Answers for Q13 (React vs Vue) - 1 answer (pending status)
(25, '两个框架各有千秋，关键是看团队和技术栈的匹配度。React更灵活，Vue更规范，没有绝对的优劣。',
    13, 1, 0, 0, 0, 'pending', NULL, '2024-07-01 10:00:00', '2024-07-01 10:00:00'),

-- Answers for Q14 (Docker部署) - 1 answer (pending status)
(26, '建议先学习Docker基础概念，然后参考Spring官方文档的Docker部署指南来实践。',
    14, 2, 0, 0, 0, 'pending', NULL, '2024-07-02 14:00:00', '2024-07-02 14:00:00'),

-- Answers for Q15 (Redis缓存) - 1 answer (published)
(27, '这三个概念确实是面试高频题，下面详细解释：\n\n**缓存穿透**：查询一个不存在的数据，缓存和数据库都没有，请求直接打到数据库。\n- 解决方案：布隆过滤器判断数据是否存在、缓存空值（短期TTL）\n\n**缓存击穿**：热点数据过期的一瞬间，大量请求同时打到数据库。\n- 解决方案：热点数据永不过期（后台异步更新）、加互斥锁（只有一个请求能查库）\n\n**缓存雪崩**：大量缓存同时过期，或Redis宕机，导致所有请求打到数据库。\n- 解决方案：随机TTL、多级缓存（本地缓存+Redis）、Redis集群高可用\n\n总结：穿透是查不到，击穿是热点过期，雪崩是大面积过期。记住这个就很好区分了。',
    15, 1, 0, 8, 1, 'published', NULL, '2024-07-03 15:00:00', '2024-07-03 15:00:00'),

-- Answers for Q7 (额外回答 - 管理员补充)
(28, '作为社区管理员顺便补充一点：前端这个领域技术更新非常快，保持学习能力比掌握某一个具体框架更重要。建议在校期间打好JavaScript基础（原型链、闭包、事件循环这些），因为框架怎么变都是在这些基础上面的封装。',
    7, 1, 0, 5, 0, 'published', NULL, '2024-06-18 09:00:00', '2024-07-11 10:00:00'),

-- Answers for Q2 (额外回答 - userB补充)
(29, '我自己项目里用的一套异常处理方案，可以结合AOP做日志记录。在全局异常处理之前，用@Aspect拦截Controller方法，自动记录请求参数和返回结果，这样排查问题的时候很方便。',
    2, 3, 0, 4, 0, 'published', NULL, '2024-06-05 16:00:00', '2024-07-08 20:00:00'),

-- Answer with rejected status
(30, '这个问题很好解决，直接重装系统就行。',
    3, 2, 0, 0, 0, 'rejected', '回答内容过于敷衍，不符合社区质量标准', '2024-06-07 11:00:00', '2024-06-07 14:00:00');

-- ----------------------------------------------------------------------------
-- Comments (20+ comments, some top-level, some replies)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_comment (id, content, user_id, answer_id, parent_id, status, created_at) VALUES
-- Comments on answer 1 (Q1 - userA's Vue3 composition API answer)
(1, '讲得很清楚，请问有推荐的学习资源吗？官方文档以外还有哪些教程比较好？', 3, 1, NULL, 'published', '2024-06-03 09:00:00'),
(2, '推荐Vue官方文档其实就很好，另外B站上技术蛋老师的Vue3系列视频也不错。', 2, 1, 1,    'published', '2024-06-03 14:00:00'),

-- Comments on answer 3 (Q2 - admin's Spring Boot answer)
(3, '太实用了！之前一直在Controller里写try-catch，代码真的好丑。今天就去改造一下项目。', 2, 3, NULL, 'published', '2024-06-05 10:00:00'),
(4, '如果有完整代码示例就更好了，最好能有一个starter级别的demo可以参考。', 3, 3, NULL, 'published', '2024-06-05 11:00:00'),
(5, '同求demo，最近也在做异常处理重构，可以参考一下思路。', 2, 3, 4,    'published', '2024-06-05 15:00:00'),

-- Comments on answer 5 (Q3 - admin's MySQL index answer)
(6, '关于索引失效的OR条件，如果两边都有索引也会走吗？', 2, 5, NULL, 'published', '2024-06-07 10:00:00'),
(7, '如果OR两边的列都有独立的单列索引，MySQL 5.0之后会用index merge优化，但性能不如联合索引。', 1, 5, 6,    'published', '2024-06-07 14:00:00'),

-- Comments on answer 7 (Q4 - admin's Pinia answer)
(8, '同意，Pinia确实比Vuex好用太多了。我们项目迁移后开发效率明显提升。', 3, 7, NULL, 'published', '2024-06-09 10:00:00'),

-- Comments on answer 11 (Q7 - admin's employment answer)
(9, '请问前端面试一般会考哪些算法？难度和数量大概是什么水平？', 3, 11, NULL, 'published', '2024-06-17 09:00:00'),
(10, '算法一般不会太难，基本就是hot 100里面的中等难度题。但手写JS原生方法（如Promise、debounce）几乎必考。', 1, 11, 9,    'published', '2024-06-17 14:00:00'),
(11, '补充一下，字节和拼多多算法会难一些，可能出hard题。其他大厂一般是medium为主。', 2, 11, 9,    'published', '2024-06-18 09:00:00'),

-- Comments on answer 13 (Q8 - userA's LeetCode answer)
(12, '按照这个路线刷了一个月，确实进步很大！感谢分享', 3, 13, NULL, 'published', '2024-06-21 09:00:00'),

-- Comments on answer 16 (Q10 - admin's VS Code plugin answer)
(13, 'Thunder Client真的很棒，轻量而且完全够用。', 3, 16, NULL, 'published', '2024-06-24 10:00:00'),

-- Comments on answer 19 (Q12 - admin's procrastination answer)
(14, '5分钟法则亲测有效！有时候最难的就是按下开始键。', 3, 19, NULL, 'published', '2024-07-01 09:00:00'),
(15, '对的，生理上只要开始做事情，前额叶皮层激活后就会进入状态。', 2, 19, 14,   'published', '2024-07-01 12:00:00'),

-- Comments on answer 21 (Q20 - admin's resume answer)
(16, 'STAR法则真的很重要，我之前写简历不知道这个，改完后明显面试邀请多了。', 3, 21, NULL, 'published', '2024-06-29 09:00:00'),

-- Comments on answer 23 (Q21 - admin's DP answer)
(17, '看了你的解释终于有点感觉了，dp数组的含义确实是关键第一步。', 3, 23, NULL, 'published', '2024-06-17 09:00:00'),
(18, '是的，dp数组定义不清楚后面全乱。建议每道题都在注释里写清楚dp[i][j]表示什么。', 1, 23, 17,   'published', '2024-06-17 14:00:00'),

-- Comments on answer 27 (Q15 - admin's Redis answer)
(19, '总结得很精辟！穿透、击穿、雪崩终于分清楚了。', 3, 27, NULL, 'published', '2024-07-04 10:00:00'),

-- Comments on answer 2 (Q1 - userB's Vue answer)
(20, '从mixin转hooks确实需要适应一下，但过了一周就真的回不去了哈哈', 1, 2, NULL, 'published', '2024-06-03 16:00:00'),
(21, '同感，特别是多个mixin有相同属性名的时候debug真的很痛苦。', 2, 2, 20,   'published', '2024-06-04 09:00:00'),

-- Comment on answer 19 (Q12 - additional)
(22, '还有一个方法：用思维导图把任务可视化，看到自己的进度会更有动力的感觉。', 2, 19, NULL, 'published', '2024-07-02 10:00:00');

-- ----------------------------------------------------------------------------
-- Votes (50+ votes, spread across questions and answers)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_vote (id, user_id, target_type, target_id, created_at) VALUES
-- Question votes (25 votes)
(1,  2, 'question', 1,  '2024-06-02 09:00:00'),
(2,  3, 'question', 1,  '2024-06-02 10:00:00'),
(3,  1, 'question', 2,  '2024-06-04 09:00:00'),
(4,  3, 'question', 2,  '2024-06-04 10:00:00'),
(5,  1, 'question', 3,  '2024-06-06 09:00:00'),
(6,  2, 'question', 3,  '2024-06-06 10:00:00'),
(7,  1, 'question', 4,  '2024-06-08 09:00:00'),
(8,  3, 'question', 4,  '2024-06-08 10:00:00'),
(9,  2, 'question', 5,  '2024-06-11 09:00:00'),
(10, 3, 'question', 5,  '2024-06-11 10:00:00'),
(11, 1, 'question', 6,  '2024-06-13 09:00:00'),
(12, 2, 'question', 6,  '2024-06-13 10:00:00'),
(13, 1, 'question', 7,  '2024-06-16 09:00:00'),
(14, 3, 'question', 7,  '2024-06-16 10:00:00'),
(15, 2, 'question', 8,  '2024-06-19 09:00:00'),
(16, 3, 'question', 8,  '2024-06-19 10:00:00'),
(17, 1, 'question', 9,  '2024-06-21 09:00:00'),
(18, 2, 'question', 10, '2024-06-23 09:00:00'),
(19, 1, 'question', 10, '2024-06-23 10:00:00'),
(20, 2, 'question', 11, '2024-06-26 09:00:00'),
(21, 3, 'question', 11, '2024-06-26 10:00:00'),
(22, 1, 'question', 12, '2024-06-29 09:00:00'),
(23, 2, 'question', 12, '2024-06-29 10:00:00'),
(24, 1, 'question', 20, '2024-06-27 09:00:00'),
(25, 2, 'question', 21, '2024-06-15 09:00:00'),

-- Answer votes (28 votes)
(26, 1, 'answer', 1,  '2024-06-02 09:00:00'),
(27, 3, 'answer', 1,  '2024-06-02 10:00:00'),
(28, 1, 'answer', 2,  '2024-06-03 09:00:00'),
(29, 2, 'answer', 2,  '2024-06-03 10:00:00'),
(30, 2, 'answer', 3,  '2024-06-04 09:00:00'),
(31, 3, 'answer', 3,  '2024-06-04 10:00:00'),
(32, 2, 'answer', 5,  '2024-06-06 09:00:00'),
(33, 3, 'answer', 5,  '2024-06-06 10:00:00'),
(34, 2, 'answer', 6,  '2024-06-07 09:00:00'),
(35, 1, 'answer', 6,  '2024-06-07 10:00:00'),
(36, 1, 'answer', 7,  '2024-06-08 09:00:00'),
(37, 3, 'answer', 7,  '2024-06-08 10:00:00'),
(38, 2, 'answer', 9,  '2024-06-11 09:00:00'),
(39, 3, 'answer', 10, '2024-06-14 09:00:00'),
(40, 1, 'answer', 11, '2024-06-16 09:00:00'),
(41, 3, 'answer', 11, '2024-06-16 10:00:00'),
(42, 1, 'answer', 13, '2024-06-19 09:00:00'),
(43, 3, 'answer', 13, '2024-06-19 10:00:00'),
(44, 1, 'answer', 14, '2024-06-20 09:00:00'),
(45, 2, 'answer', 14, '2024-06-20 10:00:00'),
(46, 2, 'answer', 15, '2024-06-22 09:00:00'),
(47, 3, 'answer', 16, '2024-06-23 09:00:00'),
(48, 2, 'answer', 18, '2024-06-26 09:00:00'),
(49, 1, 'answer', 19, '2024-06-29 09:00:00'),
(50, 3, 'answer', 19, '2024-06-29 10:00:00'),
(51, 1, 'answer', 21, '2024-06-27 09:00:00'),
(52, 2, 'answer', 23, '2024-06-15 09:00:00'),
(53, 3, 'answer', 23, '2024-06-15 10:00:00');

-- ----------------------------------------------------------------------------
-- Favorites (10+ favorites)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_favorite (id, user_id, question_id, created_at) VALUES
(1,  2, 1,  '2024-06-03 09:00:00'),
(2,  3, 1,  '2024-06-03 10:00:00'),
(3,  1, 3,  '2024-06-06 09:00:00'),
(4,  2, 3,  '2024-06-06 10:00:00'),
(5,  3, 3,  '2024-06-07 09:00:00'),
(6,  2, 4,  '2024-06-09 09:00:00'),
(7,  1, 7,  '2024-06-17 09:00:00'),
(8,  3, 7,  '2024-06-17 10:00:00'),
(9,  2, 8,  '2024-06-20 09:00:00'),
(10, 3, 8,  '2024-06-20 10:00:00'),
(11, 2, 10, '2024-06-24 09:00:00'),
(12, 1, 12, '2024-06-30 09:00:00'),
(13, 2, 21, '2024-06-16 09:00:00');

-- ----------------------------------------------------------------------------
-- Follows (7 follows)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_follow (id, follower_id, followed_id, created_at) VALUES
(1, 2, 1, '2024-06-01 09:00:00'),   -- userA follows admin
(2, 3, 1, '2024-06-05 09:00:00'),   -- userB follows admin
(3, 3, 2, '2024-06-10 09:00:00'),   -- userB follows userA
(4, 1, 2, '2024-06-01 10:00:00'),   -- admin follows userA
(5, 1, 3, '2024-06-06 10:00:00'),   -- admin follows userB
(6, 2, 3, '2024-06-12 09:00:00'),   -- userA follows userB
(7, 3, 2, '2024-06-15 09:00:00');   -- duplicate intentionally to test IGNORE

-- ----------------------------------------------------------------------------
-- Reports (4 reports)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_report (id, reporter_id, target_type, target_id, reason, detail, status, handler_id, result_note, created_at, handled_at) VALUES
-- Pending report
(1, 2, 'answer', 30, 'other',
    '该回答只有一句话"直接重装系统就行"，完全不具有参考价值，属于典型的低质量回答。',
    'pending', NULL, NULL,
    '2024-06-08 12:00:00', NULL),

-- Ignored report
(2, 3, 'question', 18, 'other',
    '这个问题与社区内容定位不符，希望管理员删除该问题。',
    'ignored', 1, '问题已删除——问题已由发布者自行删除，无需重复处理。',
    '2024-06-17 14:00:00', '2024-06-17 16:00:00'),

-- Resolved - content deleted
(3, 2, 'answer', 30, 'abuse',
    '该回答没有提供实质性内容，属于敷衍式回答。',
    'deleted_content', 1, '经审核，该回答确实属于低质量敷衍内容，已删除并对回答者进行提醒。',
    '2024-06-09 10:00:00', '2024-06-09 15:00:00'),

-- Resolved with user warning
(4, 1, 'question', 17, 'copyright',
    '问题内容涉及学术不端倾向，不符合社区价值观。',
    'deleted_content', 1, '已删除问题并发送警告通知给发布者，引导其树立正确的学习观念。',
    '2024-06-12 14:00:00', '2024-06-12 16:00:00');

-- ----------------------------------------------------------------------------
-- Notifications (15 notifications)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_notification (id, user_id, type, source_id, trigger_user_id, is_read, content, created_at, expire_at) VALUES
-- Notifications for userA (阿澄)
(1,  2, 'follow',       NULL, 1, 1, '管理员关注了你',                                              '2024-06-01 10:00:00', '2024-06-08 10:00:00'),
(2,  2, 'new_answer',   1,    3, 1, '北辰回答了你的问题"Vue3中Composition API和Options API有什么区别"',    '2024-06-02 15:00:00', '2024-06-09 15:00:00'),
(3,  2, 'new_answer',   3,    1, 1, '管理员回答了你的问题"Spring Boot项目中如何优雅地处理全局异常"',        '2024-06-04 09:00:00', '2024-06-11 09:00:00'),
(4,  2, 'comment',      3,    3, 0, '北辰评论了回答"Spring Boot项目中如何优雅地处理全局异常"',             '2024-06-05 11:00:00', '2024-06-12 11:00:00'),
(5,  2, 'comment',      3,    1, 0, '管理员回复了你的评论',                                            '2024-06-05 15:00:00', '2024-06-12 15:00:00'),
(6,  2, 'accepted',     3,    1, 1, '你的回答被采纳为最佳答案',                                        '2024-06-04 14:00:00', '2024-06-11 14:00:00'),
(7,  2, 'vote',         2,    3, 0, '北辰赞同了你的回答',                                              '2024-06-03 10:00:00', '2024-06-10 10:00:00'),

-- Notifications for userB (北辰)
(8,  3, 'follow',       NULL, 1, 1, '管理员关注了你',                                              '2024-06-06 10:00:00', '2024-06-13 10:00:00'),
(9,  3, 'follow',       NULL, 2, 1, '阿澄关注了你',                                                '2024-06-12 09:00:00', '2024-06-19 09:00:00'),
(10, 3, 'accepted',     14,   1, 1, '你的回答被采纳为最佳答案',                                     '2024-06-21 10:00:00', '2024-06-28 10:00:00'),
(11, 3, 'new_answer',   8,    1, 0, '管理员回答了你的问题"LeetCode刷题顺序推荐"',                     '2024-06-19 10:00:00', '2024-06-26 10:00:00'),
(12, 3, 'comment',      11,   1, 0, '管理员回复了你的评论',                                          '2024-06-18 09:00:00', '2024-06-25 09:00:00'),
(13, 3, 'audit_pass',   12,   1, 1, '你的问题"大学生如何克服拖延症"已通过审核并发布',                  '2024-06-28 16:00:00', '2024-07-05 16:00:00'),

-- Notifications for admin
(14, 1, 'report_result', 3,   1, 1, '你处理的举报已标记为"内容已删除"',                              '2024-06-09 15:00:00', '2024-06-16 15:00:00'),

-- Achievement notification for userA
(15, 2, 'achievement',  NULL, NULL, 0, '恭喜你解锁成就：首次回答',                                    '2024-06-02 16:00:00', '2024-06-09 16:00:00');

-- ----------------------------------------------------------------------------
-- User Achievements (for admin - 6 achievements)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_user_achievement (id, user_id, achievement_code, unlocked_at) VALUES
(1, 1, 'registered',        '2024-01-01 08:00:00'),
(2, 1, 'first_question',    '2024-06-01 09:00:00'),
(3, 1, 'first_answer',      '2024-06-02 10:00:00'),
(4, 1, 'hundred_votes',     '2024-06-20 10:00:00'),
(5, 1, 'fifty_answers',     '2024-07-01 10:00:00'),
(6, 1, 'month_login',       '2024-01-31 08:00:00'),
-- Also give userA some achievements
(7, 2, 'registered',        '2024-05-18 09:15:00'),
(8, 2, 'first_answer',      '2024-06-02 15:00:00'),
(9, 3, 'registered',        '2024-05-25 14:20:00'),
(10, 3, 'first_question',   '2024-06-05 08:00:00');

-- ----------------------------------------------------------------------------
-- Operation Logs (10 logs)
-- ----------------------------------------------------------------------------
INSERT IGNORE INTO t_operation_log (id, user_id, action, target_type, target_id, detail, created_at) VALUES
(1,  1, 'create_category',    'category',    1,  '创建顶级分类"技术学习"',                              '2024-01-01 08:00:00'),
(2,  1, 'create_category',    'category',    2,  '创建顶级分类"校园生活"',                              '2024-01-01 08:00:00'),
(3,  1, 'create_category',    'category',    3,  '创建顶级分类"知识百科"',                              '2024-01-01 08:00:00'),
(4,  1, 'delete_question',    'question',    18, '删除问题"今天食堂的饭好难吃"——内容质量过低',          '2024-06-16 18:00:00'),
(5,  1, 'delete_question',    'question',    19, '删除问题"有没有人和我一起组队打游戏"——与社区定位不符','2024-06-20 08:00:00'),
(6,  1, 'reject_question',    'question',    16, '驳回问题：与社区定位不符，建议到硬件维修论坛求助',    '2024-06-09 10:00:00'),
(7,  1, 'reject_question',    'question',    17, '驳回问题：存在投机取巧倾向',                          '2024-06-12 09:00:00'),
(8,  1, 'handle_report',      'report',      3,  '处理举报#3：删除低质量回答',                          '2024-06-09 15:00:00'),
(9,  1, 'add_sensitive_word', 'sensitive_word', 1, '添加敏感词"代考"',                                 '2024-06-01 08:00:00'),
(10, 2, 'ask_question',       'question',    2,  '发布问题"Spring Boot项目中如何优雅地处理全局异常"',  '2024-06-03 10:30:00');

-- ============================================================================
-- END OF SCRIPT
-- ============================================================================
