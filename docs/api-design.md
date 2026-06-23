# 知问社区 API 接口文档

> **版本：** v1.0
> **目标读者：** backend-worker / frontend-worker
> **后端技术栈：** Spring Boot 3.3.4, JDK 21, MyBatis Plus 3.5.7, MySQL 8.0.26
> **前端技术栈：** Vue 3 + Vite + Element Plus
> **后端端口：** 8888 | **前端端口：** 9999

---

## 目录

1. [响应约定](#1-响应约定)
2. [用户注册登录](#2-用户注册登录)
3. [个人中心](#3-个人中心)
4. [问题管理](#4-问题管理)
5. [回答管理](#5-回答管理)
6. [评论功能](#6-评论功能)
7. [互动功能](#7-互动功能)
8. [邀请回答](#8-邀请回答)
9. [举报功能](#9-举报功能)
10. [分类标签管理](#10-分类标签管理)
11. [标签智能推荐](#11-标签智能推荐)
12. [搜索功能](#12-搜索功能)
13. [消息通知](#13-消息通知)
14. [首页推荐](#14-首页推荐)
15. [热榜](#15-热榜)
16. [精华区](#16-精华区)
17. [等级成就体系](#17-等级成就体系)
18. [知识关联推荐](#18-知识关联推荐)
19. [敏感词过滤](#19-敏感词过滤)
20. [管理后台](#20-管理后台)
21. [数据统计报表](#21-数据统计报表)
22. [错误码汇总](#22-错误码汇总)
23. [全局约束与实现注意事项](#23-全局约束与实现注意事项)

---

## 1. 响应约定

### 1.1 统一响应结构

所有接口返回统一结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1706603943000
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | int | 业务状态码，200 表示成功 |
| `message` | string | 提示信息，成功时为 `"success"` |
| `data` | any | 响应数据，无数据时为 `null` |
| `timestamp` | long | 响应时间戳（毫秒） |

### 1.2 HTTP 状态码与业务码

| HTTP | code | 场景 |
|------|------|------|
| 200 | 200 | 成功 |
| 400 | 400 | 参数校验失败 |
| 401 | 401 | 未登录 / Token 过期 |
| 403 | 403 | 无权限 |
| 404 | 404 | 资源不存在 |
| 409 | 409 | 冲突（重复操作） |
| 500 | 500 | 服务器内部错误 |
| 200 | 4001+ | 自定义业务错误（见[错误码汇总](#22-错误码汇总)） |

### 1.3 鉴权方式

- 登录后返回 JWT Token，有效期 7 天
- 客户端请求头携带：`Authorization: Bearer <token>`
- 标记 `requireLogin` 的接口需携带 Token
- 标记 `adminOnly` 的接口需 ADMIN 角色

### 1.4 分页约定

分页请求参数（Query String）：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `page` | int | 否 | 1 | 页码，最小 1 |
| `pageSize` | int | 否 | 20 | 每页条数，上限 50 |

分页响应结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "timestamp": 1706603943000
}
```

---

## 2. 用户注册登录

**角色：** Public（注册/登录）、USER（获取当前用户/退出）

### 2.1 用户注册

- **路径：** `POST /api/auth/register`
- **权限：** Public
- **说明：** 用户名 + 密码注册，注册后自动登录返回 Token

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `username` | string | 是 | 用户名，4~50 字符，全局唯一 |
| `password` | string | 是 | 密码，6~50 字符，MD5 无盐存储 |

**请求示例：**

```json
{
  "username": "newuser",
  "password": "123456"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 3,
      "username": "newuser",
      "nickname": "newuser",
      "avatar": null,
      "bio": null,
      "role": "user",
      "status": "active",
      "exp": 0,
      "level": 1
    }
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 用户名为空 | 用户名不能为空 |
| 400 | 密码少于6位 | 密码长度不能少于6位 |
| 409 | 用户名已存在 | 用户名已被注册 |
| 4001 | 用户名含敏感词 | 用户名包含不当用语 |

**实现备注：**
- 注册成功后自动触发成就「初来乍到」（achievement_code: `registered`）
- nickname 默认取 username
- 前端：注册成功后存储 Token 并跳转首页

---

### 2.2 用户登录

- **路径：** `POST /api/auth/login`
- **权限：** Public
- **说明：** 用户名 + 密码登录，返回 JWT Token

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `username` | string | 是 | 用户名 |
| `password` | string | 是 | 密码 |

**请求示例：**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**成功响应：** 同注册返回结构

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 用户名为空 | 用户名不能为空 |
| 400 | 密码为空 | 密码不能为空 |
| 401 | 用户名或密码错误 | 用户名或密码错误 |
| 4002 | 账号已被封禁 | 账号已被封禁，请联系管理员 |

**实现备注：**
- 登录成功更新 `login_days` 和 `consecutive_days`（凌晨定时任务兜底）
- 检查连续登录触发成就「社区常客」（7天）、「铁杆粉丝」（30天）
- 封禁用户（status=banned）登录时返回 4002

---

### 2.3 退出登录

- **路径：** `POST /api/auth/logout`
- **权限：** USER
- **说明：** 退出登录，服务端可以不维护黑名单（无状态 JWT）

**请求参数：** 无

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1706603943000
}
```

**实现备注：**
- 当前设计下 JWT 无状态，前端清除 Token 即可
- 后续如需 Token 黑名单可在此扩展

---

### 2.4 获取当前用户信息

- **路径：** `GET /api/auth/me`
- **权限：** USER

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "data:image/png;base64,...",
    "bio": "社区管理员",
    "role": "admin",
    "status": "active",
    "exp": 1520,
    "level": 5,
    "loginDays": 45,
    "consecutiveDays": 12,
    "createdAt": "2026-01-15T10:30:00"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 401 | Token 无效或过期 | 请重新登录 |

**实现备注：**
- 前端页面加载时调用此接口校验登录态
- 返回 401 时前端清除 Token 并跳转登录页

---

## 3. 个人中心

**角色：** USER

### 3.1 获取个人资料

- **路径：** `GET /api/user/profile`
- **权限：** USER

**成功响应：** 同 2.4 返回结构

---

### 3.2 编辑个人资料

- **路径：** `PUT /api/user/profile`
- **权限：** USER

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `nickname` | string | 否 | 昵称，最长50字符，可重复 |
| `avatar` | string | 否 | 头像 Base64，≤500KB（解码后约200×200） |
| `bio` | string | 否 | 个人简介，最长500字符 |

**请求示例：**

```json
{
  "nickname": "新昵称",
  "avatar": "data:image/png;base64,iVBORw0KGgo...",
  "bio": "热爱编程的社区成员"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "nickname": "新昵称",
    "avatar": "data:image/png;base64,iVBORw0KGgo...",
    "bio": "热爱编程的社区成员"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 头像超500KB | 头像大小不能超过500KB |
| 400 | 昵称超50字符 | 昵称长度不能超过50字符 |
| 400 | 简介超500字符 | 简介长度不能超过500字符 |
| 4001 | 昵称含敏感词 | 昵称包含不当用语 |

**实现备注：**
- 仅更新传入的非空字段
- avatar 前端裁剪后 Base64 传入
- 敏感词过滤应用于 nickname 和 bio

---

### 3.3 我的提问列表

- **路径：** `GET /api/user/questions`
- **权限：** USER
- **查询参数：** page, pageSize, status（可选：all / pending / published / rejected）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "Java 泛型擦除是什么意思？",
        "status": "published",
        "answerCount": 5,
        "voteCount": 12,
        "viewCount": 230,
        "createdAt": "2026-06-20T10:30:00"
      }
    ],
    "total": 15,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 按创建时间倒序
- 用户可同时查看 pending/rejected 状态（仅查自己的）

---

### 3.4 我的回答列表

- **路径：** `GET /api/user/answers`
- **权限：** USER
- **查询参数：** page, pageSize, status（可选：all / pending / published / rejected）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 10,
        "content": "<p>泛型擦除是指...</p>",
        "questionId": 1,
        "questionTitle": "Java 泛型擦除是什么意思？",
        "isAccepted": true,
        "voteCount": 25,
        "commentCount": 3,
        "status": "published",
        "createdAt": "2026-06-21T14:00:00"
      }
    ],
    "total": 8,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 按创建时间倒序
- `questionTitle` 通过 JOIN question 表获取
- 被采纳的回答 `isAccepted=true`

---

### 3.5 我的收藏

- **路径：** `GET /api/user/favorites`
- **权限：** USER
- **查询参数：** page, pageSize

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "favoriteId": 5,
        "questionId": 3,
        "title": "Spring Boot 自动配置原理",
        "answerCount": 8,
        "voteCount": 30,
        "favoritedAt": "2026-06-22T09:00:00"
      }
    ],
    "total": 12,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 按收藏时间 `created_at` 倒序
- 仅查询 published 且未删除的问题
- 前端点击跳转到对应问题详情页

---

### 3.6 我的关注列表

- **路径：** `GET /api/user/following`
- **权限：** USER
- **查询参数：** page, pageSize

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "followId": 8,
        "userId": 2,
        "nickname": "技术达人",
        "avatar": "data:image/png;base64,...",
        "bio": "全栈工程师",
        "followedAt": "2026-06-15T16:00:00"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 关注方向：我（follower_id）-> 对方（followed_id）
- 按关注时间倒序

---

### 3.7 我的粉丝列表

- **路径：** `GET /api/user/followers`
- **权限：** USER
- **查询参数：** page, pageSize

**成功响应：** 结构同 3.6，字段名为 `followerId` 替代 `followId`

**实现备注：**
- 粉丝方向：对方（follower_id）-> 我（followed_id）
- 按关注时间倒序

---

### 3.8 修改密码

- **路径：** `PUT /api/user/password`
- **权限：** USER

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `oldPassword` | string | 是 | 旧密码 |
| `newPassword` | string | 是 | 新密码，6~50字符 |

**请求示例：**

```json
{
  "oldPassword": "123456",
  "newPassword": "newpass123"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "修改成功",
  "data": null,
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 新密码少于6位 | 新密码长度不能少于6位 |
| 4003 | 旧密码错误 | 旧密码不正确 |

**实现备注：**
- 修改密码后**不失效**现有 Token（简化设计）
- 前后端均 MD5 后比较

---

### 3.9 我的等级与成就

- **路径：** `GET /api/user/achievements`
- **权限：** USER

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "level": 5,
    "levelName": "Lv.5",
    "currentExp": 1200,
    "nextLevelExp": 1999,
    "totalExp": 1200,
    "expProgress": 0.20,
    "achievements": [
      {
        "code": "registered",
        "name": "初来乍到",
        "description": "完成注册",
        "unlocked": true,
        "unlockedAt": "2026-01-15T10:30:00"
      },
      {
        "code": "month_login",
        "name": "铁杆粉丝",
        "description": "累计登录30天",
        "unlocked": false,
        "progress": "15/30"
      }
    ]
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- `expProgress` = (currentExp - 当前等级最低Exp) / (下一等级最低Exp - 当前等级最低Exp)
- 返回全部10种成就，已解锁 `unlocked=true`，未解锁显示进度
- 等级区间与成就触发条件见需求文档 3.16

---

## 4. 问题管理

**角色：** Public（查看）、USER（创建/编辑/删除自己的）、ADMIN（管理）

### 4.1 创建问题

- **路径：** `POST /api/questions`
- **权限：** USER
- **说明：** 发布问题 → 敏感词过滤 → 通过后 status=pending → 进入审核

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 是 | 标题，1~200字符 |
| `content` | string | 否 | 正文，可选 |
| `categoryId` | long | 是 | 分类ID |
| `tagIds` | long[] | 否 | 标签ID数组，最多5个 |
| `inviteeIds` | long[] | 否 | 被邀请用户ID数组，最多3个，不能包含自己 |

**请求示例：**

```json
{
  "title": "Java 泛型擦除到底是什么意思？",
  "content": "一直不太理解泛型擦除，求大佬解答",
  "categoryId": 1,
  "tagIds": [1, 3],
  "inviteeIds": [2]
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "问题已提交，等待审核",
  "data": {
    "id": 25,
    "status": "pending"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 标题为空 | 标题不能为空 |
| 400 | 标题超200字符 | 标题不能超过200字符 |
| 400 | 分类ID为空 | 请选择分类 |
| 400 | 标签超过5个 | 最多选择5个标签 |
| 400 | 邀请超过3人 | 最多邀请3人 |
| 400 | 邀请自己 | 不能邀请自己 |
| 400 | 分类不存在 | 分类不存在 |
| 4004 | 敏感词命中 | 内容包含不当用语，请修改 |

**实现备注：**
- 创建时在 `question_invitation` 表插入邀请记录；创建 notification 通知被邀请者
- 敏感词过滤在 Controller 层统一拦截（前后端各执行一次）
- 前端：命中敏感词即时提示不提交

---

### 4.2 问题列表

- **路径：** `GET /api/questions`
- **权限：** Public
- **说明：** 支持推荐/热门/最新三种排序

**查询参数：**

| 字段 | 类型 | 必填 | 默认 | 说明 |
|------|------|------|------|------|
| `sort` | string | 否 | recommend | recommend / latest / hot |
| `categoryId` | long | 否 | — | 按分类筛选 |
| `page` | int | 否 | 1 | 页码 |
| `pageSize` | int | 否 | 20 | 每页条数 |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "Java 泛型擦除是什么意思？",
        "contentPreview": "一直不太理解泛型擦除...",
        "categoryId": 1,
        "categoryName": "编程技术",
        "tags": [
          {"id": 1, "name": "Java"},
          {"id": 3, "name": "泛型"}
        ],
        "author": {
          "id": 2,
          "nickname": "技术达人",
          "avatar": "data:image/png;base64,...",
          "level": 5
        },
        "viewCount": 230,
        "answerCount": 5,
        "voteCount": 12,
        "favoriteCount": 8,
        "isFeatured": false,
        "isPinned": false,
        "createdAt": "2026-06-20T10:30:00"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "timestamp": 1706603943000
}
```

**排序规则：**

| sort 值 | 排序规则 |
|---------|---------|
| `recommend` | 按推荐分数倒序：`(view×0.3 + answer×1 + vote×2 + favorite×1.5) × time_decay`，featured ×1.5 |
| `latest` | 按 `created_at` 倒序 |
| `hot` | 按回答数倒序（最近7天活跃） |

**实现备注：**
- `time_decay`：≤24h=1.0，之后每天×0.85，最低0.1
- 仅返回 `status=published` 的问题
- `contentPreview` 取正文前150字符（去除 HTML 标签）
- 前端：首页三个 Tab 对应 sort=recommend/latest/hot

---

### 4.3 搜索问题

- **路径：** `GET /api/questions/search`
- **权限：** Public

**查询参数：**

| 字段 | 类型 | 必填 | 默认 | 说明 |
|------|------|------|------|------|
| `keyword` | string | 否 | — | 搜索关键词（标题+正文模糊匹配） |
| `categoryId` | long | 否 | — | 按分类筛选 |
| `tagId` | long | 否 | — | 按标签筛选 |
| `sort` | string | 否 | relevance | relevance / time / hot |
| `page` | int | 否 | 1 | 页码 |
| `pageSize` | int | 否 | 20 | 每页条数 |

**成功响应：** 结构同 4.2

**实现备注：**
- `keyword` 使用 SQL `LIKE '%keyword%'` 匹配 `title` 和 `content`
- 仅搜索 `status=published` 的问题
- sort=relevance：关键词匹配度排序（title 完全匹配优先）
- sort=time：按 `created_at` 倒序
- sort=hot：按热度分数（view×1 + answer×3 + vote×5 + favorite×3）
- 空搜索返回全部 published 问题

---

### 4.4 问题详情

- **路径：** `GET /api/questions/{id}`
- **权限：** Public（published）/ USER（作者可查看 pending/rejected）/ ADMIN（全部可见）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Java 泛型擦除是什么意思？",
    "content": "一直不太理解泛型擦除，求大佬解答",
    "categoryId": 1,
    "categoryName": "编程技术",
    "tags": [
      {"id": 1, "name": "Java"},
      {"id": 3, "name": "泛型"}
    ],
    "author": {
      "id": 2,
      "nickname": "技术达人",
      "avatar": "data:image/png;base64,...",
      "level": 5
    },
    "viewCount": 230,
    "answerCount": 5,
    "voteCount": 12,
    "favoriteCount": 8,
    "status": "published",
    "isFeatured": false,
    "isPinned": false,
    "auditReason": null,
    "invitations": [
      {
        "id": 5,
        "invitee": {
          "id": 3,
          "nickname": "Java大神",
          "avatar": "data:image/png;base64,..."
        },
        "status": "pending",
        "createdAt": "2026-06-20T10:30:00"
      }
    ],
    "isVoted": true,
    "isFavorited": false,
    "isAuthorFollowed": false,
    "relatedQuestions": [
      {
        "id": 5,
        "title": "Java 类型擦除和桥方法",
        "answerCount": 3,
        "voteCount": 8,
        "createdAt": "2026-06-18T08:00:00"
      }
    ],
    "createdAt": "2026-06-20T10:30:00",
    "updatedAt": "2026-06-20T10:30:00"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 404 | 问题不存在 | 问题不存在或已被删除 |
| 403 | 访问pending/rejected（非作者非管理员） | 无权限查看该内容 |

**实现备注：**
- 每次访问 `viewCount + 1`（同一用户/IP 30分钟内去重）
- `isVoted`/`isFavorited`/`isAuthorFollowed`：需登录才返回，未登录时均为 false
- `relatedQuestions`：知识关联推荐（见模块17）
- `invitations`：邀请记录列表
- 前端：pending/rejected 状态问题仅在作者和管理员查看时返回完整数据

---

### 4.5 编辑问题

- **路径：** `PUT /api/questions/{id}`
- **权限：** Author only
- **说明：** 仅作者可编辑；rejected 状态编辑后自动重置为 pending

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 否 | 标题 |
| `content` | string | 否 | 正文 |
| `categoryId` | long | 否 | 分类ID |
| `tagIds` | long[] | 否 | 标签ID数组 |

**请求示例：**

```json
{
  "title": "Java 泛型类型擦除详解",
  "content": "补充了更多上下文，求大佬解答",
  "categoryId": 1,
  "tagIds": [1, 3, 5]
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "status": "pending",
    "auditReason": null
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 403 | 非作者 | 无权编辑此问题 |
| 404 | 问题不存在 | 问题不存在 |
| 4004 | 敏感词命中 | 内容包含不当用语，请修改 |

**实现备注：**
- rejected 状态编辑后自动重置为 pending，清空 `audit_reason`
- published 状态编辑**不触发重新审核**（简化设计）
- 敏感词过滤重新执行

---

### 4.6 删除问题

- **路径：** `DELETE /api/questions/{id}`
- **权限：** Author / ADMIN
- **说明：** 软删除（status=deleted）

**成功响应：**

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 403 | 非作者非管理员 | 无权删除此问题 |
| 404 | 问题不存在 | 问题不存在 |

**实现备注：**
- 软删除：设置 `status=deleted`
- 删除后问题下的所有回答**不级联删除**（可选：前端展示"问题已删除"）
- 前端：弹窗二次确认

---

## 5. 回答管理

**角色：** Public（查看 published）、USER（创建/编辑/删除自己）、Author（采纳）、ADMIN（管理）

### 5.1 创建回答

- **路径：** `POST /api/questions/{questionId}/answers`
- **权限：** USER
- **说明：** 发布回答 → 敏感词过滤 + 图片校验 → status=pending → 进入审核

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `content` | string | 是 | 回答富文本正文（HTML），可内嵌 Base64 图片 |

**请求示例：**

```json
{
  "content": "<p>泛型擦除是Java编译器的机制...</p><img src=\"data:image/png;base64,iVBORw0KGgo...\" />"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "回答已提交，等待审核",
  "data": {
    "id": 50,
    "status": "pending"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 内容为空 | 回答内容不能为空 |
| 400 | 单张图片超2MB | 单张图片大小不能超过2MB |
| 400 | 图片超过10张 | 回答中图片数量不能超过10张 |
| 400 | 问题不存在 | 问题不存在 |
| 4004 | 敏感词命中 | 内容包含不当用语，请修改 |

**实现备注：**
- Base64 图片校验：正则提取 `<img src="data:image/...">` 中的 Base64 字符串
  - 解码后计算大小，单张 ≤ 2MB
  - 统计 `<img>` 标签数，≤ 10 张
- 富文本 XSS 白名单过滤：允许 `p, br, b, i, em, strong, a, img, h1-h6, ul, ol, li, code, pre, blockquote`
- 创建后通知提问者（notification type: `new_answer`）
- 敏感词过滤在 XSS 过滤之后执行

---

### 5.2 回答列表

- **路径：** `GET /api/questions/{questionId}/answers`
- **权限：** Public（published 回答）/ Author + ADMIN（全部可见）
- **查询参数：** `page`, `pageSize`, `sort`（默认 vote：按点赞数；`time`：按时间）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 10,
        "content": "<p>泛型擦除是Java编译器的机制...</p>",
        "author": {
          "id": 3,
          "nickname": "Java大神",
          "avatar": "data:image/png;base64,...",
          "level": 8
        },
        "isAccepted": true,
        "voteCount": 25,
        "commentCount": 3,
        "status": "published",
        "auditReason": null,
        "isVoted": true,
        "createdAt": "2026-06-21T14:00:00"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**排序规则：**
- 被采纳的回答始终置顶
- 其余按 sort 参数排序（默认 vote_count 倒序）

**实现备注：**
- 非作者非管理员：仅返回 `status=published` 的回答
- 作者和管理员：返回全部状态
- `isVoted` 需登录才返回

---

### 5.3 编辑回答

- **路径：** `PUT /api/answers/{id}`
- **权限：** Author only
- **说明：** rejected 状态编辑后自动重置为 pending

**请求参数：** 同 5.1，仅 `content`

**成功响应：** 同创建

**错误用例：** 同创建 + 403 非作者

**实现备注：**
- rejected → 编辑 → 自动重置 status=pending，清空 audit_reason
- 重新执行图片校验 + 敏感词过滤 + XSS 过滤

---

### 5.4 删除回答

- **路径：** `DELETE /api/answers/{id}`
- **权限：** Author / ADMIN
- **说明：** 软删除

**成功响应：** `{ "code": 200, "message": "删除成功" }`

**错误用例：** 403 非作者非管理员 / 404 不存在

**实现备注：**
- 设置 `status=deleted`
- 回答下的评论**不级联删除**（前端展示"回答已删除"）
- 前端弹窗二次确认

---

### 5.5 采纳回答

- **路径：** `POST /api/answers/{id}/accept`
- **权限：** Question Author only

**成功响应：**

```json
{
  "code": 200,
  "message": "采纳成功",
  "data": {
    "answerId": 10,
    "isAccepted": true
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 非提问者 | 只有提问者可以采纳回答 |
| 4005 | 已有采纳 | 该问题已有被采纳的回答 |
| 400 | 回答不属于该问题 | 操作失败 |
| 404 | 回答不存在 | 回答不存在 |

**实现备注：**
- 校验问题是否已有 `is_accepted=1` 的回答
- 采纳后触发经验值变更：被采纳者 +10 exp，检查成就「最佳答案」
- 通知被采纳者（notification type: `accepted`）
- 前端：采纳后按钮变为"已采纳"灰色不可点击

---

## 6. 评论功能

**角色：** Public（查看）、USER（创建/删除自己）、ADMIN（删除任意）

### 6.1 发表评论

- **路径：** `POST /api/answers/{answerId}/comments`
- **权限：** USER
- **说明：** 评论 → 敏感词过滤 → 直接发布（不进入审核）

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `content` | string | 是 | 评论正文，1~500字符 |
| `parentId` | long | 否 | 父评论ID，为空=顶级评论，非空=二级回复 |

**请求示例：**

```json
{
  "content": "讲得很清楚，感谢解答！",
  "parentId": null
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 100,
    "createdAt": "2026-06-23T15:00:00"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 内容为空 | 评论内容不能为空 |
| 400 | 超500字符 | 评论不能超过500字符 |
| 400 | 超过二级嵌套 | 不支持三级及以上回复 |
| 400 | 父评论不属于该回答 | 操作失败 |
| 404 | 回答不存在 | 回答不存在 |
| 4004 | 敏感词命中 | 内容包含不当用语，请修改 |

**实现备注：**
- `parentId` 非空时，校验该父评论的 `parent_id` 是否为 null（即父评论必须是顶级评论）
- 评论不进入审核队列，直接 status=published
- 通知回答者（notification type: `comment`），二级回复通知父评论作者
- 评论创建后 `answer.comment_count + 1`

---

### 6.2 评论列表

- **路径：** `GET /api/answers/{answerId}/comments`
- **权限：** Public
- **查询参数：** `page`, `pageSize`

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 100,
        "content": "讲得很清楚，感谢解答！",
        "author": {
          "id": 4,
          "nickname": "学习者",
          "avatar": "data:image/png;base64,..."
        },
        "parentId": null,
        "replies": [
          {
            "id": 101,
            "content": "不客气，有问题再问",
            "author": {
              "id": 3,
              "nickname": "Java大神",
              "avatar": "data:image/png;base64,..."
            },
            "parentId": 100,
            "createdAt": "2026-06-23T15:10:00"
          }
        ],
        "createdAt": "2026-06-23T15:00:00"
      }
    ],
    "total": 8,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 二级嵌套结构：顶级评论的 `replies` 字段包含其直接子评论
- `replies` 最多返回 3 条预览 + 总数，前端点击"查看更多"展开
- 仅返回 `status=published` 的评论
- 按时间正序排列

---

### 6.3 删除评论

- **路径：** `DELETE /api/comments/{id}`
- **权限：** Author / ADMIN
- **说明：** 软删除

**成功响应：** `{ "code": 200, "message": "删除成功" }`

**错误用例：** 403 非作者非管理员 / 404 不存在

**实现备注：**
- 软删除：`status=deleted`
- 子评论不级联删除（前端展示"该评论已删除"）
- 删除后 `answer.comment_count - 1`

---

## 7. 互动功能

**角色：** USER

### 7.1 点赞/取消点赞

- **路径：** `POST /api/votes/toggle`
- **权限：** USER
- **说明：** Toggle 模式，存在则删除（取消赞），不存在则新增（点赞）

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `targetType` | string | 是 | `question` / `answer` |
| `targetId` | long | 是 | 目标ID |

**请求示例：**

```json
{
  "targetType": "question",
  "targetId": 1
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "voted": true,
    "voteCount": 13
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- `voted=true` 表示点赞成功，`voted=false` 表示取消点赞
- 点赞：目标 vote_count +1，被点赞者 exp +2
- 取消点赞：目标 vote_count -1，经验不变（只增不减）
- 检查成就「百赞达人」「人气之星」
- Toggle 操作需考虑并发：使用 `INSERT IGNORE` + `DELETE` 或数据库唯一约束
- (user_id, target_type, target_id) 唯一约束确保同一用户只存一条

---

### 7.2 收藏/取消收藏

- **路径：** `POST /api/favorites/toggle`
- **权限：** USER
- **说明：** Toggle 模式，仅问题可收藏

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `questionId` | long | 是 | 问题ID |

**请求示例：**

```json
{
  "questionId": 1
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "favorited": true,
    "favoriteCount": 9
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 404 | 问题不存在 | 问题不存在 |

**实现备注：**
- 收藏：favorite_count +1，被收藏者 exp +1
- 取消收藏：favorite_count -1，经验不变
- 唯一约束：(user_id, question_id)

---

### 7.3 关注/取消关注

- **路径：** `POST /api/follows/toggle`
- **权限：** USER
- **说明：** Toggle 模式

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `followedId` | long | 是 | 被关注用户ID，不能是自己 |

**请求示例：**

```json
{
  "followedId": 2
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "followed": true
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 关注自己 | 不能关注自己 |
| 404 | 用户不存在 | 用户不存在 |

**实现备注：**
- 关注时通知被关注者（notification type: `follow`）
- 检查成就「万人迷」（100个关注者）
- 唯一约束：(follower_id, followed_id)

---

## 8. 邀请回答

**角色：** USER（提问者）

### 8.1 搜索用户（邀请用）

- **路径：** `GET /api/users/search`
- **权限：** USER
- **说明：** 发布问题时搜索被邀请用户

**查询参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `keyword` | string | 是 | 用户名或昵称模糊搜索 |
| `limit` | int | 否 | 返回数量上限，默认10，最大20 |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 2,
      "username": "tech_master",
      "nickname": "技术达人",
      "avatar": "data:image/png;base64,...",
      "level": 5,
      "answerCount": 45
    }
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 排除当前登录用户
- 按回答数倒序排列（活跃用户优先）
- 前端：选择后加入邀请列表，最多3人

---

### 8.2 问题邀请记录

- **路径：** `GET /api/questions/{questionId}/invitations`
- **权限：** Public
- **说明：** 查看某问题的邀请记录

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 5,
      "inviter": {
        "id": 1,
        "nickname": "提问者"
      },
      "invitee": {
        "id": 2,
        "nickname": "技术达人",
        "avatar": "data:image/png;base64,..."
      },
      "status": "pending",
      "createdAt": "2026-06-20T10:30:00"
    }
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 邀请记录包含在问题详情接口（4.4）中
- 独立接口用于前端按需加载或分页

---

### 8.3 被邀请者回答后更新邀请状态

- **说明：** 无独立接口，创建回答时自动更新对应 `question_invitation` 状态为 `answered`

**实现备注：**
- 回答者在受邀列表（status=pending）中 → 创建回答后更新 invitation status=answered
- 被邀请者无强制回答义务

---

## 9. 举报功能

**角色：** USER（举报）、ADMIN（处理）

### 9.1 提交举报

- **路径：** `POST /api/reports`
- **权限：** USER

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `targetType` | string | 是 | `question` / `answer` / `comment` |
| `targetId` | long | 是 | 目标ID |
| `reason` | string | 是 | `ad` / `porn` / `abuse` / `copyright` / `other` |
| `detail` | string | 否 | 补充说明，最长500字符 |

**请求示例：**

```json
{
  "targetType": "answer",
  "targetId": 10,
  "reason": "ad",
  "detail": "回答中包含广告链接"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "举报已提交，管理员会尽快处理",
  "data": {
    "id": 5
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | targetType 无效 | 举报类型无效 |
| 404 | 目标不存在 | 举报目标不存在 |
| 4006 | 重复举报 | 您已举报过该内容 |
| 400 | 不能举报自己 | 不能举报自己的内容 |

**实现备注：**
- 唯一约束：(reporter_id, target_type, target_id) 防止重复举报
- 举报不自动处理内容，由管理员人工裁决
- 通知管理员（可选）

---

### 9.2 管理员查看举报列表

- **路径：** `GET /api/admin/reports`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `status`（可选：pending / ignored / deleted_content / banned_user）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 5,
        "reporter": {
          "id": 4,
          "nickname": "学习者"
        },
        "targetType": "answer",
        "targetId": 10,
        "targetPreview": "回答内容摘要...",
        "targetAuthor": {
          "id": 3,
          "nickname": "Java大神"
        },
        "reason": "ad",
        "detail": "回答中包含广告链接",
        "status": "pending",
        "handlerId": null,
        "resultNote": null,
        "createdAt": "2026-06-23T16:00:00",
        "handledAt": null
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

---

### 9.3 管理员处理举报

- **路径：** `PUT /api/admin/reports/{id}/handle`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `action` | string | 是 | `ignore` / `delete_content` / `ban_user` |
| `resultNote` | string | 否 | 处理备注 |

**请求示例：**

```json
{
  "action": "delete_content",
  "resultNote": "确认含广告，已删除该回答"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "处理成功",
  "data": null,
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | action 无效 | 无效的处理动作 |
| 4007 | 举报已处理 | 该举报已处理 |

**实现备注：**
- `ignore`：仅更新 report status=ignored，不操作内容
- `delete_content`：软删除目标内容 + report status=deleted_content
- `ban_user`：设定目标作者 status=banned + 软删除其内容 + report status=banned_user
- 处理完成后通知举报者（notification type: `report_result`）
- 记录操作日志（operation_log）

---

## 10. 分类标签管理

**角色：** Public（查看）、ADMIN（管理）

### 10.1 获取分类树

- **路径：** `GET /api/categories`
- **权限：** Public

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "编程技术",
      "parentId": null,
      "level": 1,
      "sortOrder": 1,
      "questionCount": 25,
      "children": [
        {
          "id": 4,
          "name": "Java",
          "parentId": 1,
          "level": 2,
          "sortOrder": 1,
          "questionCount": 12,
          "children": []
        }
      ]
    },
    {
      "id": 2,
      "name": "生活娱乐",
      "parentId": null,
      "level": 1,
      "sortOrder": 2,
      "questionCount": 18,
      "children": []
    }
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 仅返回 status=active 的分类
- 返回完整树形结构（递归组装）
- 前端首页分类导航使用此接口

---

### 10.2 管理员创建分类

- **路径：** `POST /api/admin/categories`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | string | 是 | 分类名称，1~50字符 |
| `parentId` | long | 否 | 父分类ID，空=顶级分类 |
| `sortOrder` | int | 否 | 排序值，默认0 |

**请求示例：**

```json
{
  "name": "人工智能",
  "parentId": 1,
  "sortOrder": 2
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 10,
    "name": "人工智能",
    "parentId": 1,
    "level": 2
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 名称为空 | 分类名称不能为空 |
| 400 | 父分类不存在 | 父分类不存在 |
| 409 | 同级名称重复 | 该分类已存在 |

---

### 10.3 管理员编辑分类

- **路径：** `PUT /api/admin/categories/{id}`
- **权限：** ADMIN

**请求参数：** 同 10.2（只传需更新的字段）

**成功响应：** `{ "code": 200, "message": "更新成功" }`

**错误用例：** 同 10.2 + 404 不存在

---

### 10.4 管理员删除分类

- **路径：** `DELETE /api/admin/categories/{id}`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "已删除3个分类",
  "data": {
    "deletedCount": 3
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 404 | 分类不存在 | 分类不存在 |
| 4008 | 分类下有已发布问题 | 该分类下有已发布问题，无法删除 |

**实现备注：**
- 级联删除子分类
- 检查该分类及所有子孙分类下是否有 status=published 的问题，有则阻止删除
- 记录操作日志

---

### 10.5 获取标签列表

- **路径：** `GET /api/tags`
- **权限：** Public
- **查询参数：** `keyword`（可选，模糊搜索标签名）, `page`, `pageSize`

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "Java",
        "usageCount": 45,
        "createdAt": "2026-01-15T10:00:00"
      }
    ],
    "total": 12,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 按 `usage_count` 倒序排列
- 前端提问页面标签选择器使用

---

### 10.6 管理员创建标签

- **路径：** `POST /api/admin/tags`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | string | 是 | 标签名称，1~30字符，唯一 |

**成功响应：** `{ "code": 200, "message": "创建成功", "data": { "id": 15 } }`

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 400 | 名称为空 | 标签名称不能为空 |
| 409 | 标签名重复 | 该标签已存在 |

---

### 10.7 管理员编辑标签

- **路径：** `PUT /api/admin/tags/{id}`
- **权限：** ADMIN

**请求参数：** 同 10.6

---

### 10.8 管理员删除标签

- **路径：** `DELETE /api/admin/tags/{id}`
- **权限：** ADMIN

**成功响应：** `{ "code": 200, "message": "删除成功" }`

**实现备注：**
- 物理删除标签 + 删除关联表 question_tag 中的记录
- 被删除标签的问题不受影响

---

## 11. 标签智能推荐

**角色：** USER（提问时触发）

### 11.1 推荐标签

- **路径：** `POST /api/tags/recommend`
- **权限：** USER
- **说明：** 根据问题标题从标签库匹配推荐标签

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `title` | string | 是 | 问题标题 |

**请求示例：**

```json
{
  "title": "Java Spring Boot 自动配置原理详解"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"id": 1, "name": "Java", "relevance": 1.0},
    {"id": 5, "name": "Spring Boot", "relevance": 1.0},
    {"id": 3, "name": "泛型", "relevance": 0.6}
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 将标题分词后与所有标签名称模糊匹配（SQL LIKE）
- 按匹配度排序，最多返回5个
- 纯辅助功能，前端展示推荐标签供用户点选
- 用户可忽略推荐手动选择

---

## 12. 搜索功能

**与问题管理模块合并实现**，见 [4.3 搜索问题](#43-搜索问题)。

---

## 13. 消息通知

**角色：** USER

### 13.1 通知列表

- **路径：** `GET /api/notifications`
- **权限：** USER
- **查询参数：** `page`, `pageSize`, `isRead`（可选：0=未读 / 1=已读）, `type`（可选通知类型）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 200,
        "type": "new_answer",
        "typeLabel": "新回答",
        "content": "技术达人 回答了你的问题「Java 泛型擦除是什么意思？」",
        "isRead": false,
        "sourceId": 10,
        "triggerUser": {
          "id": 2,
          "nickname": "技术达人",
          "avatar": "data:image/png;base64,..."
        },
        "createdAt": "2026-06-23T14:30:00"
      }
    ],
    "total": 25,
    "page": 1,
    "pageSize": 20,
    "totalPages": 2
  },
  "timestamp": 1706603943000
}
```

**通知类型：**

| type | typeLabel | 触发场景 |
|------|-----------|---------|
| `new_answer` | 新回答 | 有人回答了你的问题 |
| `comment` | 新评论 | 有人评论了你的回答 |
| `follow` | 新关注 | 有人关注了你 |
| `accepted` | 回答被采纳 | 你的回答被采纳 |
| `audit_pass` | 审核通过 | 你的问题/回答审核通过 |
| `audit_reject` | 审核驳回 | 你的问题/回答被驳回 |
| `invite` | 邀请回答 | 有人邀请你回答问题 |
| `report_result` | 举报结果 | 你的举报已处理 |
| `achievement` | 成就解锁 | 解锁了新成就 |
| `system` | 系统通知 | 系统消息 |

**实现备注：**
- 按创建时间倒序
- `sourceId`：点击跳转到对应问题/回答/评论
- 前端：通知列表页，点击标记已读并跳转

---

### 13.2 未读通知数量

- **路径：** `GET /api/notifications/unread-count`
- **权限：** USER

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "count": 5
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 前端导航栏角标显示
- 每次页面加载/路由切换时调用

---

### 13.3 标记已读

- **路径：** `PUT /api/notifications/{id}/read`
- **权限：** USER

**成功响应：** `{ "code": 200, "message": "success" }`

**实现备注：**
- 单条标记 is_read=1
- 客户端点击通知卡片时调用

---

### 13.4 全部标记已读

- **路径：** `PUT /api/notifications/read-all`
- **权限：** USER

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "updatedCount": 5
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 批量更新当前用户所有 is_read=0 的通知
- 前端"全部已读"按钮

---

## 14. 首页推荐

**与问题列表合并实现**，通过 `GET /api/questions?sort=recommend` 获取推荐 Feed。

**实现备注：**
- 首页三个 Tab：
  - 推荐 Tab → `GET /api/questions?sort=recommend`
  - 热门 Tab → `GET /api/questions?sort=hot`
  - 最新 Tab → `GET /api/questions?sort=latest`
- 首页顶部：分类导航（`GET /api/categories`）+ 热榜入口链接
- 推荐分数公式见 [4.2](#42-问题列表)

---

## 15. 热榜

**角色：** Public

### 15.1 热榜列表

- **路径：** `GET /api/questions/hot`
- **权限：** Public
- **查询参数：** `period`（d=日榜 / w=周榜 / m=月榜，默认 d）, `limit`（默认20，最大50）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "rank": 1,
      "id": 5,
      "title": "Spring Boot 自动配置原理",
      "hotScore": 2850,
      "viewCount": 500,
      "answerCount": 12,
      "voteCount": 80,
      "favoriteCount": 30,
      "createdAt": "2026-06-22T10:00:00"
    }
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 热度公式：`view×1 + answer×3 + vote×5 + favorite×3`
- 仅统计 time_range 内创建且 published 的问题
- 定时任务每日 01:00 预计算缓存
- 前端：日/周/月 Tab 切换

---

## 16. 精华区

**角色：** Public（查看）、ADMIN（管理）

### 16.1 精华列表

- **路径：** `GET /api/questions/featured`
- **权限：** Public
- **查询参数：** `page`, `pageSize`, `keyword`（可选搜索）

**成功响应：** 结构同 4.2 问题列表，只包含 `isFeatured=true` 的问题

**实现备注：**
- 仅返回 published 且 is_featured=1 的问题
- 支持关键词搜索（标题+正文）

---

### 16.2 标记精华

- **路径：** `POST /api/admin/questions/{id}/feature`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "已设为精华",
  "data": {
    "id": 1,
    "isFeatured": true
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 4009 | 问题非published | 仅已发布的问题可设为精华 |
| 404 | 问题不存在 | 问题不存在 |

**实现备注：**
- 仅 status=published 的问题可加精
- 通知作者（notification type: `system`）

---

### 16.3 取消精华

- **路径：** `DELETE /api/admin/questions/{id}/feature`
- **权限：** ADMIN

**成功响应：** `{ "code": 200, "message": "已取消精华" }`

---

## 17. 等级成就体系

**角色：** Public（排行榜）、USER（个人成就）

### 17.1 经验值排行榜

- **路径：** `GET /api/leaderboard`
- **权限：** Public
- **查询参数：** `page`, `pageSize`（默认20）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "rank": 1,
        "userId": 3,
        "nickname": "Java大神",
        "avatar": "data:image/png;base64,...",
        "level": 8,
        "exp": 8500,
        "answerCount": 120,
        "achievementCount": 7
      }
    ],
    "total": 50,
    "page": 1,
    "pageSize": 20,
    "totalPages": 3
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 按 exp 倒序
- 仅返回 status=active 的用户
- 个人成就查询见 [3.9](#39-我的等级与成就)

---

## 18. 知识关联推荐

**集成在问题详情接口**中，见 [4.4 问题详情](#44-问题详情) 中的 `relatedQuestions` 字段。

**实现备注：**
- 关联公式：`score = (same_category ? 2 : 0) + (tag_overlap_count × 3)`
- 按关联度降序取 Top 5
- 排除当前问题自身
- 仅推荐 status=published 的问题

---

## 19. 敏感词过滤

**双重过滤机制：** 前端即时提示 + 后端兜底。用户内容提交接口（问题/回答/评论/昵称修改）均在业务层统一调用敏感词过滤。

### 19.1 管理员查看敏感词列表

- **路径：** `GET /api/admin/sensitive-words`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `status`（可选 active / disabled）, `keyword`（可选搜索）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "word": "违禁词",
        "status": "active",
        "createdBy": 1,
        "createdAt": "2026-01-15T10:00:00"
      }
    ],
    "total": 15,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

---

### 19.2 添加敏感词

- **路径：** `POST /api/admin/sensitive-words`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `word` | string | 是 | 敏感词，1~100字符，唯一 |

**请求示例：**

```json
{
  "word": "新违禁词"
}
```

**成功响应：** `{ "code": 200, "message": "添加成功", "data": { "id": 20 } }`

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 409 | 敏感词重复 | 该敏感词已存在 |
| 400 | 词为空 | 敏感词不能为空 |

**实现备注：**
- 添加后默认 status=active，立即生效
- 触发敏感词缓存刷新

---

### 19.3 编辑敏感词

- **路径：** `PUT /api/admin/sensitive-words/{id}`
- **权限：** ADMIN

**请求参数：** `word`（string，必填）

---

### 19.4 删除敏感词

- **路径：** `DELETE /api/admin/sensitive-words/{id}`
- **权限：** ADMIN

**成功响应：** `{ "code": 200, "message": "删除成功" }`

**实现备注：**
- 物理删除
- 触发缓存刷新

---

### 19.5 启用/禁用敏感词

- **路径：** `PUT /api/admin/sensitive-words/{id}/toggle`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "word": "违禁词",
    "status": "disabled"
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- Toggle：active → disabled，disabled → active
- 禁用后该词不再参与过滤
- 触发缓存刷新

---

### 敏感词过滤接口（内部调用）

- **说明：** 无独立外部接口，以下接口在业务层统一调用敏感词过滤工具类：
  - `POST /api/questions` — 创建问题
  - `PUT /api/questions/{id}` — 编辑问题
  - `POST /api/questions/{questionId}/answers` — 创建回答
  - `PUT /api/answers/{id}` — 编辑回答
  - `POST /api/answers/{answerId}/comments` — 发表评论
  - `PUT /api/user/profile` — 修改昵称/简介

**过滤逻辑：**
1. 查询所有 status=active 的敏感词（缓存）
2. 对输入文本进行**不区分大小写**的 `contains` 匹配
3. 命中任一敏感词 → 返回错误码 4004："内容包含不当用语，请修改"
4. 全部未命中 → 放行

**缓存策略：** 后端内存缓存（Caffeine/Guava Cache），词库变更即时刷新 + 每小时全量刷新

---

## 20. 管理后台

**角色：** ADMIN

### 20.1 内容审核 — 待审核列表

#### 20.1.1 待审核问题列表

- **路径：** `GET /api/admin/review/questions`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 25,
        "title": "Java 泛型擦除是什么意思？",
        "content": "一直不太理解...",
        "author": {
          "id": 2,
          "nickname": "技术达人"
        },
        "categoryName": "编程技术",
        "status": "pending",
        "createdAt": "2026-06-23T12:00:00"
      }
    ],
    "total": 3,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  },
  "timestamp": 1706603943000
}
```

---

#### 20.1.2 待审核回答列表

- **路径：** `GET /api/admin/review/answers`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`

**成功响应：** 结构类似，增加 `questionTitle` 字段

---

### 20.2 内容审核 — 审核操作

#### 20.2.1 审核通过问题

- **路径：** `POST /api/admin/review/questions/{id}/approve`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "审核通过",
  "data": {
    "id": 25,
    "status": "published"
  },
  "timestamp": 1706603943000
}
```

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 404 | 问题不存在 | 问题不存在 |
| 4010 | 非待审核状态 | 该内容不处于待审核状态 |

**实现备注：**
- 仅 status=pending 可操作
- 通知作者（notification type: `audit_pass`）
- 触发经验值变更：作者 exp +1
- 记录操作日志

---

#### 20.2.2 审核驳回问题

- **路径：** `POST /api/admin/review/questions/{id}/reject`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `reason` | string | 是 | 驳回原因，1~500字符 |

**请求示例：**

```json
{
  "reason": "标题不清晰，请补充更多细节"
}
```

**成功响应：** `{ "code": 200, "message": "已驳回" }`

**错误用例：** 同 approve + 400（驳回原因必填）

**实现备注：**
- status=rejected，audit_reason=reason
- 通知作者（notification type: `audit_reject`）

---

#### 20.2.3 审核通过回答

- **路径：** `POST /api/admin/review/answers/{id}/approve`
- **权限：** ADMIN

**实现备注：**
- 触发经验值：回答者 exp +3
- 检查成就「首次回答」
- 更新 question.answer_count +1

---

#### 20.2.4 审核驳回回答

- **路径：** `POST /api/admin/review/answers/{id}/reject`
- **权限：** ADMIN

**请求参数：** `reason`（必填）

**实现备注：**
- 同 20.2.2

---

### 20.3 用户管理

#### 20.3.1 用户列表

- **路径：** `GET /api/admin/users`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `keyword`（用户名/昵称搜索）, `status`（active / banned）, `role`（user / admin）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 2,
        "username": "tech_master",
        "nickname": "技术达人",
        "avatar": "data:image/png;base64,...",
        "role": "user",
        "status": "active",
        "exp": 2500,
        "level": 6,
        "questionCount": 15,
        "answerCount": 45,
        "createdAt": "2026-01-20T08:00:00"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "timestamp": 1706603943000
}
```

---

#### 20.3.2 用户详情

- **路径：** `GET /api/admin/users/{id}`
- **权限：** ADMIN

**成功响应：** 用户完整信息 + 提问数 + 回答数 + 被举报次数

---

#### 20.3.3 封禁用户

- **路径：** `PUT /api/admin/users/{id}/ban`
- **权限：** ADMIN

**请求参数：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `reason` | string | 否 | 封禁原因 |

**成功响应：** `{ "code": 200, "message": "已封禁" }`

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 404 | 用户不存在 | 用户不存在 |
| 4011 | 已封禁 | 该用户已被封禁 |
| 400 | 不能封禁管理员 | 不能封禁管理员 |

**实现备注：**
- status=banned
- 封禁后 Token 仍然有效直到过期（可后续增强 Token 黑名单）
- 记录操作日志

---

#### 20.3.4 解封用户

- **路径：** `PUT /api/admin/users/{id}/unban`
- **权限：** ADMIN

**成功响应：** `{ "code": 200, "message": "已解封" }`

**错误用例：**

| code | 场景 | message |
|------|------|---------|
| 4012 | 未被封禁 | 该用户未被封禁 |

---

### 20.4 管理端问题管理

#### 20.4.1 问题列表

- **路径：** `GET /api/admin/questions`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `keyword`, `status`, `categoryId`, `isFeatured`

**成功响应：** 同 4.2 + 额外字段 `authorName`、`status`、`isFeatured`、`isPinned`

---

#### 20.4.2 管理端删除问题

- **路径：** `DELETE /api/admin/questions/{id}`
- **权限：** ADMIN

**实现备注：** 同 4.6，软删除

---

#### 20.4.3 置顶/取消置顶

- **路径：** `PUT /api/admin/questions/{id}/pin`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "isPinned": true
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- Toggle 操作
- 置顶问题在列表最上方展示

---

### 20.5 管理端回答管理

#### 20.5.1 回答列表

- **路径：** `GET /api/admin/answers`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `keyword`, `status`

---

#### 20.5.2 管理端删除回答

- **路径：** `DELETE /api/admin/answers/{id}`
- **权限：** ADMIN

**实现备注：** 软删除

---

### 20.6 管理端评论管理

#### 20.6.1 评论列表

- **路径：** `GET /api/admin/comments`
- **权限：** ADMIN
- **查询参数：** `page`, `pageSize`, `keyword`

---

#### 20.6.2 管理端删除评论

- **路径：** `DELETE /api/admin/comments/{id}`
- **权限：** ADMIN

**实现备注：** 软删除

---

## 21. 数据统计报表

**角色：** ADMIN

### 21.1 仪表盘统计概览

- **路径：** `GET /api/admin/dashboard/stats`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalUsers": 520,
    "totalQuestions": 850,
    "totalAnswers": 2300,
    "totalComments": 5600,
    "todayNewUsers": 12,
    "todayNewQuestions": 8,
    "todayNewAnswers": 35,
    "pendingQuestions": 3,
    "pendingAnswers": 5,
    "pendingReports": 2
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 定时任务每日 03:00 预计算缓存
- 实时获取 pending 数量

---

### 21.2 趋势图数据

- **路径：** `GET /api/admin/dashboard/trends`
- **权限：** ADMIN
- **查询参数：** `days`（默认30，最大90）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "dates": ["2026-05-25", "2026-05-26", "..."],
    "newUsers": [5, 8, "..."],
    "newQuestions": [3, 6, "..."],
    "newAnswers": [12, 18, "..."],
    "activeUsers": [45, 52, "..."]
  },
  "timestamp": 1706603943000
}
```

**实现备注：**
- 前端用 ECharts 折线图展示
- 按天汇总，返回日期数组 + 指标数组

---

### 21.3 分类分布

- **路径：** `GET /api/admin/dashboard/category-distribution`
- **权限：** ADMIN

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"categoryName": "编程技术", "count": 350, "percentage": 41.2},
    {"categoryName": "生活娱乐", "count": 200, "percentage": 23.5},
    {"categoryName": "学习教育", "count": 180, "percentage": 21.2},
    "其他": {"count": 120, "percentage": 14.1}
  ],
  "timestamp": 1706603943000
}
```

**实现备注：**
- 仅统计 published 问题
- 按一级分类统计
- 前端用 ECharts 饼图展示

---

### 21.4 用户排行榜

- **路径：** `GET /api/admin/dashboard/user-leaderboard`
- **权限：** ADMIN
- **查询参数：** `limit`（默认10，最大50）, `sortBy`（exp / answers / questions，默认 exp）

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "rank": 1,
      "userId": 3,
      "nickname": "Java大神",
      "exp": 8500,
      "level": 8,
      "questionCount": 25,
      "answerCount": 120,
      "achievementCount": 7
    }
  ],
  "timestamp": 1706603943000
}
```

---

### 21.5 数据导出

#### 21.5.1 导出用户

- **路径：** `GET /api/admin/export/users`
- **权限：** ADMIN
- **查询参数：** `keyword`, `status`, `dateFrom`, `dateTo`

**成功响应：** 返回 `.xlsx` 文件流

**实现备注：**
- `Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- 文件名：`users_20260623.xlsx`
- 超 10000 条数据分批查询 + 流式写入

---

#### 21.5.2 导出问题

- **路径：** `GET /api/admin/export/questions`
- **权限：** ADMIN
- **查询参数：** `keyword`, `status`, `categoryId`, `dateFrom`, `dateTo`

---

#### 21.5.3 导出回答

- **路径：** `GET /api/admin/export/answers`
- **权限：** ADMIN
- **查询参数：** `keyword`, `status`, `dateFrom`, `dateTo`

---

#### 21.5.4 导出操作日志

- **路径：** `GET /api/admin/export/logs`
- **权限：** ADMIN
- **查询参数：** `action`, `userId`, `dateFrom`, `dateTo`

---

## 22. 错误码汇总

### HTTP 通用错误

| HTTP | code | message | 场景 |
|------|------|---------|------|
| 400 | 400 | （具体校验错误） | 请求参数校验失败 |
| 401 | 401 | 请重新登录 | 未登录 / Token 无效或过期 |
| 403 | 403 | 无权限执行此操作 | 权限不足（非作者/非管理员） |
| 404 | 404 | 资源不存在 | 目标资源不存在或已删除 |
| 409 | 409 | 数据冲突 | 重复操作（如已点赞、已收藏） |
| 500 | 500 | 服务器内部错误 | 服务端异常 |

### 业务错误码

| code | message | 触发场景 |
|------|---------|---------|
| 4001 | 用户名包含不当用语 | 注册时用户名命中敏感词（已弃用，受4004覆盖） |
| 4002 | 账号已被封禁，请联系管理员 | 封禁用户尝试登录 |
| 4003 | 旧密码不正确 | 修改密码时旧密码错误 |
| 4004 | 内容包含不当用语，请修改 | 问题/回答/评论/昵称命中敏感词 |
| 4005 | 该问题已有被采纳的回答 | 重复采纳 |
| 4006 | 您已举报过该内容 | 重复举报同一目标 |
| 4007 | 该举报已处理 | 重复处理举报 |
| 4008 | 该分类下有已发布问题，无法删除 | 删除分类时存在关联问题 |
| 4009 | 仅已发布的问题可设为精华 | 对非published问题执行加精 |
| 4010 | 该内容不处于待审核状态 | 对非pending内容执行审核 |
| 4011 | 该用户已被封禁 | 重复封禁 |
| 4012 | 该用户未被封禁 | 对非封禁用户解封 |

---

## 23. 全局约束与实现注意事项

### 23.1 前端实现约束

| 约束项 | 说明 |
|--------|------|
| 请求拦截器 | Axios 拦截器统一添加 `Authorization`，处理 401 自动跳转登录页 |
| 响应拦截器 | 统一提取 `data` 字段，非 200 时 toast 提示 `message` |
| 敏感词前端过滤 | 页面加载时请求敏感词列表（缓存），提交内容时前端先校验，命中则阻止提交并提示 |
| 图片上传 | 前端富文本编辑器使用 Base64 内嵌图片，前端校验单张 ≤ 2MB + 总数 ≤ 10 |
| 头像上传 | 前端裁剪为 200×200 + Base64 编码，前端校验 ≤ 500KB |
| XSS 富文本 | 前端富文本编辑器使用白名单标签，不渲染 script/iframe 等 |
| 分页组件 | 统一使用 page + pageSize，默认 20，上限 50 |
| 按钮状态 | 已点赞/收藏/关注按钮高亮；已采纳禁止重复点击 |
| 权限按钮 | 编辑/删除按钮仅作者可见；管理功能仅 ADMIN 可见 |
| 二次确认 | 删除、封禁等敏感操作弹窗确认 |

### 23.2 后端实现约束

| 约束项 | 说明 |
|--------|------|
| JWT 鉴权 | 登录后生成 Token（7天有效），中间件验证 Token + role |
| 统一响应 | 全局 `ResponseBodyAdvice` 包装返回结构 |
| 全局异常 | `@ControllerAdvice` 统一异常处理，映射错误码 |
| 参数校验 | `@Valid` + Bean Validation，统一返回 400 |
| 敏感词过滤 | 独立工具类（SensitiveWordFilter），所有 UGC 接口调用 |
| XSS 过滤 | 独立工具类，富文本内容入库前过滤 |
| 软删除 | question/answer/comment 使用 status 字段，不物理删除 |
| Toggle 模式 | votes/favorites/follows 使用 INSERT IF NOT EXISTS + 事务 |
| 分页 | MyBatis Plus `Page` 对象，pageSize 上限 50 |
| 操作日志 | 管理员操作记录到 operation_log 表 |
| 通知创建 | 业务事件触发后异步创建 notification |
| 缓存 | 敏感词缓存（Caffeine）、统计缓存（Redis/本地缓存） |

### 23.3 数据库约束

| 约束项 | 说明 |
|--------|------|
| 唯一索引 | user.username, vote(user_id, target_type, target_id), favorite(user_id, question_id), follow(follower_id, followed_id), report(reporter_id, target_type, target_id), sensitive_word.word, tag.name |
| 查询索引 | question.status, answer.status, notification.expire_at, report.status |
| 字符集 | utf8mb4 |
| 密码存储 | MD5 无盐（历史兼容） |
| 软删除 | question/answer/comment 通过 status='deleted' 标记 |

### 23.4 定时任务

| 任务 | Cron | 说明 |
|------|------|------|
| 清理过期通知 | 每日 02:00 | DELETE FROM notification WHERE expire_at < NOW() |
| 刷新热榜 | 每日 01:00 | 重算日/周/月热度分并缓存 |
| 刷新统计缓存 | 每日 03:00 | 重算仪表盘指标 |
| 等级重算 | 每日 04:00 | 根据 exp 重新计算 level |
| 敏感词缓存 | 每小时 + 触发 | 全量同步内存缓存 |
| 登录天数 | 每日 00:05 | 更新 login_days + consecutive_days |

### 23.5 接口汇总

共计 **78** 个接口端点：

| # | 模块 | 端点数 | 接口 |
|---|------|--------|------|
| 1 | 用户注册登录 | 4 | register, login, logout, me |
| 2 | 个人中心 | 9 | profile(GET/PUT), my questions/answers/favorites/following/followers, password, achievements |
| 3 | 问题管理 | 6 | create, list, search, detail, edit, delete |
| 4 | 回答管理 | 5 | create, list, edit, delete, accept |
| 5 | 评论功能 | 3 | create, list, delete |
| 6 | 互动功能 | 3 | vote toggle, favorite toggle, follow toggle |
| 7 | 邀请回答 | 2 | search users, invitations |
| 8 | 举报功能 | 3 | create, admin list, admin handle |
| 9 | 分类标签管理 | 8 | categories(GET/POST/PUT/DELETE), tags(GET/POST/PUT/DELETE) |
| 10 | 标签推荐 | 1 | recommend |
| 11 | 搜索 | 1 | search（合并于 4.3） |
| 12 | 消息通知 | 4 | list, unread-count, mark-read, read-all |
| 13 | 首页推荐 | 0 | 合并于问题列表(4.2) |
| 14 | 热榜 | 1 | hot list |
| 15 | 精华区 | 3 | featured list, feature, unfeature |
| 16 | 等级成就 | 1 | leaderboard（+个人成就见3.9） |
| 17 | 知识关联 | 0 | 合并于问题详情(4.4) |
| 18 | 敏感词 | 5 | admin list/create/edit/delete/toggle |
| 19 | 管理后台 | 18 | review(4), users(4), questions(3), answers(2), comments(2), reports(2), categories(4), tags(3), sensitive-words(5) 注：与模块8/9/18有重叠 |
| 20 | 数据报表 | 8 | stats, trends, distribution, user-leaderboard, exports(4) |

### 23.6 待后端实现确认项

| # | 事项 | 说明 |
|---|------|------|
| 1 | JWT 黑名单 | 当前为无状态 Token，封禁用户需等 Token 过期。可选实现 Redis 黑名单 |
| 2 | 通知异步 | 通知创建是否使用异步事件（@Async / 消息队列）以减少接口响应延迟 |
| 3 | 搜索引擎 | 当前使用 SQL LIKE，数据量大时可升级 Elasticsearch |
| 4 | 图片存储 | 当前设计为 Base64 内嵌，未来可扩展为 OSS 独立存储 |
| 5 | 统计缓存 | 仪表盘数据预计算存储方式（Redis / 数据库统计表） |
| 6 | 浏览去重 | view_count 同一用户/IP 30分钟去重策略确认 |
| 7 | 成就自动解锁 | 成就检查在每次经验值变更时同步还是异步执行 |
| 8 | 导出超大批次 | 超过10000条记录时的流式写入策略（Apache POI SXSSF） |

---

> **文档结束。后端工作者可直接按此文档实现 Controller + Service + Mapper，前端工作者可按此文档实现 API 调用层和页面交互逻辑。**
