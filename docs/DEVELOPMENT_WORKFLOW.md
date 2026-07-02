# 代码开发工作流

> 适用范围：基于 `fullstack-app-starter` 脚手架进行 Web、后端、原生微信小程序、AI 服务等项目开发。
> 核心目标：每次 AI 重新进入项目、没有上下文时，都能通过项目文档判断当前处于哪一步、该使用哪个 skill、下一步做什么。

## 1. 每次进入项目必须先读

无论是完整系统开发、模块开发、修复问题，还是只做前端/后端，每次开始前必须先读取：

1. `README.md`
2. `docs/AGENTS.md`
3. `docs/DEVELOPMENT_WORKFLOW.md`
4. `docs/WORKFLOW_STATE.md`
5. `docs/GOAL_PLAN.md`（如果存在）
6. 当前任务相关文档，例如 `docs/需求文档.md`、`docs/API文档.md`、`db/db.sql`

如果没有上下文，必须以 `docs/WORKFLOW_STATE.md` 判断当前阶段，不要直接猜下一步。

## 2. 阶段状态机

| 阶段 | 名称 | 目标 | 主要产物 | 推荐 skill |
| --- | --- | --- | --- | --- |
| 0 | 脚手架准备 | 克隆、裁剪、确认目录与规范 | `README.md`、`docs/AGENTS.md`、`docs/WORKFLOW_STATE.md` | 主 Agent |
| 1 | 产品需求分析 | 第一性原理梳理角色、对象、流程、边界 | `docs/需求文档.md` | 主 Agent / 多模型审查 |
| 2 | 原型与页面设计 | 检查原型、页面清单、交互状态、图标需求 | 页面设计说明、原型检查记录 | 主 Agent / frontend-worker |
| 3 | API 设计 | 定义前后端与小程序统一接口契约 | `docs/API文档.md` | api-designer |
| 4 | 数据库设计 | 设计表结构、状态字段、索引、种子数据 | `db/db.sql` | db-builder |
| 5 | Goal 计划定稿 | 把需求、页面、接口、数据库和实现拆解合并 | `docs/GOAL_PLAN.md` | 主 Agent |
| 6 | 功能实现 | 按计划实现后端、Web、小程序、可选 AI 服务 | `apps/`、`db/` | backend-worker / frontend-worker |
| 7 | 对抗式代码审查 | 主动找需求遗漏、契约漂移、权限与测试缺口 | 审查问题清单 | regression-tester / 主 Agent |
| 8 | 回归验收与修复 | 静态验收、必要运行验收、修复和复测 | 验收结果、修复提交 | regression-tester / 对应 worker |

## 3. 状态更新规则

每完成一个阶段或局部任务，必须更新 `docs/WORKFLOW_STATE.md`：

- 当前阶段
- 当前任务
- 最近完成
- 下一步
- 阻塞项
- 阶段状态表
- 证据路径，例如 `docs/API文档.md`、`db/db.sql`、具体代码文件

如果任务被中断，下次恢复时先读 `docs/WORKFLOW_STATE.md` 和 `docs/GOAL_PLAN.md` 继续。

## 4. 新项目脚手架准备

1. 克隆脚手架：`git clone https://github.com/junkking233/fullstack-app-starter.git {项目目录}`。
2. 如果这是业务项目而不是维护脚手架本身，克隆后删除脚手架 `.git`，再按实际项目重新 `git init`。
3. 判断项目形态：是否需要后端、Web 前端、原生微信小程序、AI 服务。
4. 按需求裁剪不需要的目录和 `apps/docker-compose.yml` 配置。
5. 不自动启动服务，不重置数据库，不覆盖用户已有改动。
6. 更新 `docs/WORKFLOW_STATE.md`，将阶段 0 标记为已完成，并写明下一步。

## 5. 阶段执行要求

### 5.1 产品需求分析

- 用第一性原理梳理系统为什么存在、服务谁、解决什么问题、边界是什么。
- 输出或更新需求文档，至少包含项目概述、用户角色、功能模块、核心业务流程、数据对象、非功能需求、待确认问题。
- 不进入接口、数据库或代码实现。
- 完成后更新 `docs/WORKFLOW_STATE.md`，下一步指向阶段 2。

### 5.2 原型与页面设计

- 输出页面清单：端、页面、角色、入口、主要功能、接口依赖、状态反馈。
- 必要时准备页面设计说明；如需要，可使用 `npx @google/design.md` 生成页面设计文档。
- 图标必须先检查 `assets/icons/`；缺少图标先补共享图标源库，再复制到对应端。
- 完成后更新 `docs/WORKFLOW_STATE.md`，下一步指向阶段 3。

### 5.3 API 设计

- 负责 skill：`api-designer`。
- 建立“角色 × 模块 × 操作”矩阵。
- 每个接口必须包含路径、方法、用途、允许角色、请求参数、返回字段、错误码、业务说明。
- 统一响应结构为 `code`、`message`、`data`、`timestamp`。
- 完成后更新 `docs/API文档.md` 和 `docs/WORKFLOW_STATE.md`。

### 5.4 数据库设计

- 负责 skill：`db-builder`。
- 根据接口文档抽取实体、关系、状态字段、查询条件和约束。
- 更新 `db/db.sql`，包含表结构、索引、状态字段、角色账号和关键种子数据。
- 不重置已有数据库，不新建 MySQL 容器，只使用 `mysql-docker`。
- 完成后更新 `docs/WORKFLOW_STATE.md`。

### 5.5 Goal 计划定稿

- 基于 `docs/GOAL_PLAN_TEMPLATE.md` 生成或更新 `docs/GOAL_PLAN.md`。
- Goal 计划必须包含目标、输入资料、需求分析、页面清单、API 计划、数据库计划、实现拆解、验收矩阵、对抗式审查清单、风险阻塞、执行记录。
- 不要简化成普通 TODO。
- 完成后更新 `docs/WORKFLOW_STATE.md`，下一步指向阶段 6。

### 5.6 功能实现

- 负责 skill：`backend-worker`、`frontend-worker`。
- 后端实现严格按接口文档，不私自漂移路径、字段或响应结构。
- Web 前端使用 Vue + Vite + Element Plus，端口 `9999`，不使用紫色主色。
- 微信小程序使用原生微信语法 + Vant Weapp，不使用 uni-app。
- 图标使用必须遵守 `assets/README.md`。
- 每完成一个模块，更新 `docs/WORKFLOW_STATE.md` 和 `docs/GOAL_PLAN.md` 执行记录。

### 5.7 对抗式代码审查

- 负责 skill：`regression-tester` 或主 Agent。
- 主动检查需求遗漏、接口契约漂移、数据库支撑不足、权限问题、页面交互缺口、测试盲区。
- 输出问题清单：编号、模块、问题、证据、影响、严重程度、责任 skill、建议修复方式。
- 完成后更新 `docs/WORKFLOW_STATE.md`。

### 5.8 回归验收与修复

- 如果开发者没有启动服务，只做静态验收，并把运行验收标记为阻塞。
- 如需页面测试，使用 chrome-devtools 或可用浏览器调试工具；不要自动启动服务。
- 修复问题时只改相关文件，不做无关重构。
- 修复后更新 `docs/WORKFLOW_STATE.md`，记录通过项、未通过项、阻塞项和下一步。

## 6. 常规任务入口

| 任务类型 | 执行方式 |
| --- | --- |
| 一次开发整个系统 | 先走阶段 0-5，再进入阶段 6 实现，最后阶段 7-8 审查验收。 |
| 开发单个功能模块 | 先读状态文件，缺需求/接口/数据库就补对应阶段，再实现和验收。 |
| 只开发后端接口 | 确认 API 和数据库足够支撑，只改后端和必要文档，完成后更新状态。 |
| 只开发 Web 前端或小程序 | 确认接口文档足够支撑页面；Web 用 Element Plus，小程序用原生微信 + Vant Weapp。 |
| 收集问题 | 只收集、复现、归类、定位问题，不修复代码，输出问题清单并更新状态。 |
| 修复问题并回归 | 按问题归属选择 skill，修复后复测并更新状态。 |
