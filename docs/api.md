# 世界杯赛事信息系统接口文档

## 通用约定

Base URL：`/api`

统一返回：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2026-06-06T12:00:00"
}
```

通用错误码：
- 400：参数错误
- 401：未登录或登录失效
- 403：权限不足
- 404：数据不存在
- 409：数据冲突
- 500：服务器错误

认证方式：登录后请求头携带 `Authorization: Bearer {token}`。

权限说明：
- Public：游客可访问。
- User：注册用户可访问。
- Admin：管理员可访问。

## 认证接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 登录 | POST | `/auth/login` | Public | 用户登录 |
| 注册 | POST | `/auth/register` | Public | 注册普通用户 |
| 当前用户 | GET | `/auth/me` | User | 获取当前登录用户 |
| 修改密码 | PUT | `/auth/password` | User | 修改当前用户密码 |

登录参数：`username`、`password`。  
注册参数：`username`、`password`、`email`、`phone`。  
登录返回：`token`、`user`、`expiresAt`。

## 用户管理

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 用户分页 | GET | `/users` | Admin | 支持 username、email、status、page、pageSize |
| 用户详情 | GET | `/users/{id}` | Admin | 查询用户 |
| 新增用户 | POST | `/users` | Admin | 新增后台用户 |
| 修改用户 | PUT | `/users/{id}` | Admin | 修改用户资料、状态、角色 |
| 删除用户 | DELETE | `/users/{id}` | Admin | 删除用户 |

## 球队接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 球队分页 | GET | `/teams` | Public | 支持 keyword、groupName、confederation、page、pageSize |
| 球队详情 | GET | `/teams/{id}` | Public | 查询球队详情 |
| 新增球队 | POST | `/teams` | Admin | 新增球队 |
| 修改球队 | PUT | `/teams/{id}` | Admin | 修改球队 |
| 删除球队 | DELETE | `/teams/{id}` | Admin | 删除球队 |

球队字段：`id`、`nameCn`、`nameEn`、`country`、`confederation`、`groupName`、`flagUrl`、`description`、`source`、`sourceUpdatedAt`。

## 城市与场馆接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 城市列表 | GET | `/cities` | Public | 支持 country、keyword |
| 城市详情 | GET | `/cities/{id}` | Public | 包含承办比赛数量 |
| 场馆列表 | GET | `/stadiums` | Public | 支持 cityId、keyword |
| 场馆详情 | GET | `/stadiums/{id}` | Public | 包含承办比赛 |

城市和场馆无后台维护接口。

## 比赛接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 比赛分页 | GET | `/matches` | Public | 支持 keyword、teamId、cityId、groupName、stage、status、date、page、pageSize |
| 比赛详情 | GET | `/matches/{id}` | Public | 查询比赛详情 |
| 新增比赛 | POST | `/matches` | Admin | 新增赛程 |
| 修改比赛 | PUT | `/matches/{id}` | Admin | 修改赛程和比分 |
| 删除比赛 | DELETE | `/matches/{id}` | Admin | 删除比赛 |

比赛字段：`id`、`matchNo`、`stage`、`groupName`、`homeTeamId`、`awayTeamId`、`matchTime`、`cityId`、`stadiumId`、`homeScore`、`awayScore`、`status`、`winnerTeamId`。

比赛状态：`NOT_STARTED`、`LIVE`、`FINISHED`、`CANCELLED`。

## 积分榜接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 积分榜列表 | GET | `/standings` | Public | 支持 groupName |
| 重新计算积分榜 | POST | `/standings/recalculate` | Admin | 根据已结束小组赛更新积分榜 |
| 修改积分榜 | PUT | `/standings/{id}` | Admin | 人工修正排名和晋级状态 |

晋级状态：`QUALIFIED` 直接晋级，`BEST_THIRD_QUALIFIED` 最佳第三名晋级，`ELIMINATED` 淘汰，`PENDING` 待定。

## 淘汰赛对阵接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 对阵图 | GET | `/bracket` | Public | 返回淘汰赛阶段比赛列表 |
| 更新晋级结果 | PUT | `/bracket/matches/{id}` | Admin | 修改淘汰赛比分和晋级球队 |

## 评论接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 比赛公开评论 | GET | `/comments/match/{matchId}` | Public | 只返回已通过评论 |
| 我的评论 | GET | `/comments/my` | User | 查看本人评论审核状态 |
| 发表评论 | POST | `/comments` | User | 评论比赛 |
| 评论分页 | GET | `/comments` | Admin | 后台审核列表 |
| 审核评论 | PUT | `/comments/{id}/review` | Admin | 审核通过或驳回 |
| 删除评论 | DELETE | `/comments/{id}` | Admin | 删除评论 |

评论状态：`PENDING`、`APPROVED`、`REJECTED`。

## 收藏接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 我的收藏 | GET | `/favorites/my` | User | 查询我的比赛和球队收藏 |
| 查询收藏状态 | GET | `/favorites/status` | User | 参数 objectType、objectId |
| 收藏 | POST | `/favorites` | User | 收藏比赛或球队 |
| 取消收藏 | DELETE | `/favorites` | User | 参数 objectType、objectId |

收藏类型：`TEAM`、`MATCH`。

## 图表接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 后台概览 | GET | `/charts/dashboard-stats` | Admin | 用户、球队、比赛、评论统计 |
| 比赛阶段分布 | GET | `/charts/match-stage-distribution` | Admin | 饼图 |
| 城市比赛数量 | GET | `/charts/city-match-count` | Admin | 柱状图 |
| 小组积分图 | GET | `/charts/group-points` | Admin | 柱状图 |
| 热门球队收藏 | GET | `/charts/top-favorite-teams` | Admin | 排行 |
| 热门比赛收藏 | GET | `/charts/top-favorite-matches` | Admin | 排行 |
| 前台公开统计 | GET | `/charts/public-summary` | Public | 首页真实统计和热门收藏排行 |

## 数据维护接口

| 功能 | 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- | --- |
| 维护记录分页 | GET | `/data-maintenance` | Admin | 支持 dataType、page、pageSize |
| 新增维护记录 | POST | `/data-maintenance` | Admin | 记录数据来源、动作和备注 |

维护记录字段：`id`、`dataType`、`source`、`operatorId`、`actionType`、`remark`、`createTime`。
