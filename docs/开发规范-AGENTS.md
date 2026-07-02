# 项目开发规范

本文件只放 AI 开发必须遵守的硬约束。项目说明看 `README.md`，阶段流程看 `docs/开发工作流-Workflow.md`，当前状态看 `docs/工作流状态-WorkflowState.md`。

## 进入项目先读

每次开发前必须读取：

1. `README.md`
2. `docs/开发规范-AGENTS.md`
3. `docs/开发工作流-Workflow.md`
4. `docs/工作流状态-WorkflowState.md`
5. `index.html`
6. `docs/Goal长程计划-GoalPlan.md`（如存在）
7. 当前任务相关文档，例如 `docs/产品需求文档-PRD.md`、`docs/API接口文档-API.md`、`docs/页面设计文档-UIDesign.md`

没有上下文时，以 `docs/工作流状态-WorkflowState.md` 判断下一步，不要猜。

## 项目约束

- 后端端口：`8888`；前端端口：`9999`；AI 助手服务端口：`8000`。
- Docker Compose 固定在 `apps/docker-compose.yml`。
- 不创建 MySQL 容器，统一连接已有 `mysql-docker`。
- 不使用 Nginx 容器处理网页请求。
- MySQL：`root/admin123`，连接参数包含 `characterEncoding=UTF-8` 和 `allowPublicKeyRetrieval=true`。
- 终端连接 MySQL 使用：`mysql -u root -padmin123 --default-character-set=utf8mb4`。
- 本机 MySQL 位于 `/usr/local/mysql/bin`，不要用 brew 验证。
- 管理员密码：`admin123`；普通用户密码：`123456`；密码使用 MD5，不加 salt。

## 开发流程

固定顺序：

1. 产品需求分析（PRD）
2. 原型生图提示词（Lovart Prompt）
3. Lovart 设计稿拆解（UI Design）
4. API 设计
5. 数据库设计
6. Goal 长程计划
7. 功能实现
8. 对抗式代码审查
9. 回归验收与修复

长程任务或整套系统开发时，必须先基于 `docs/Goal长程计划模板-GoalPlanTemplate.md` 生成 `docs/Goal长程计划-GoalPlan.md`，不能只写普通 TODO。

每完成一个阶段或局部任务，必须同步更新 `docs/工作流状态-WorkflowState.md` 和根目录 `index.html`；如果存在 `docs/Goal长程计划-GoalPlan.md`，也必须同步更新。

## 设计稿与资源

- 原型生图提示词输出到 `docs/原型生图提示词-LovartPrompt.md`。
- Lovart 原型图、PSD、PNG 默认放到 `design/lovart/`。
- 页面设计拆解输出到 `docs/页面设计文档-UIDesign.md`。
- 页面实现必须以 Lovart 原型图、PSD、PNG 和 UI Design 文档为依据，不要跳过设计稿自由发挥。
- 通用图标先检查 `assets/icons/`。
- Web 图标副本放到 `apps/frontend/src/assets/icons/`。
- 小程序图标副本放到 `apps/miniprogram/assets/icons/`。
- 缺少图标时先补 `assets/icons/`，再复制到子应用。

## 后端规范

- Java 项目禁止 Lombok。
- Spring Boot `3.3.4`，JDK `21`，MyBatis Plus `3.5.7`，MySQL 驱动 `8.0.26`。
- Spring Boot 配置需要支持 Maven 热加载。
- 统一响应结构：`code`、`message`、`data`、`timestamp`。
- Controller 只处理 HTTP 入参、出参和权限上下文，业务逻辑放 Service。
- 数据库结构变化后同步更新 `db/db.sql`。

## Web 前端规范

- 使用 Vue + Vite + npm，必须包含 `index.html`，禁止 Vue CLI。
- UI 框架使用 Element UI/Element Plus，优先使用组件能力。
- 禁止紫色主色。
- 门户菜单用水平模式；后台管理可用侧栏垂直菜单。
- 下拉框宽度按内容设置，避免显示不全。
- 避免用 `!important` 覆盖 `.el-button--primary`；确需覆盖时排除 `.is-link` 和 `.is-text`。
- npm、nodejs、pip 使用国内镜像地址。

推荐脚本语义：

```json
{
  "scripts": {
    "install:dev": "npm install && npm run start",
    "start": "vite --port 9999",
    "dev": "npm run install:dev",
    "build": "vite build"
  }
}
```

## 微信小程序规范

- 小程序目录固定为 `apps/miniprogram/`，微信开发者工具直接导入该目录。
- 使用原生 WXML、WXSS、JavaScript、JSON，不使用 uni-app。
- UI 组件使用 Vant Weapp，包名 `@vant/weapp`。
- AppID 固定为 `wxd84d204ed36b05b5`。
- 样式使用 `rpx`。
- 普通操作按钮优先用 `view` / `text` + `bindtap`。
- 原生 `button` 只用于 `open-type`、授权、分享、手机号、表单提交等微信原生能力。
- 自定义原生 `button` 必须重置 margin、padding、border、line-height、text-align、background、border-radius，并设置 `button::after { border: 0; }`。
- 底部固定操作栏使用 `env(safe-area-inset-bottom)`，页面内容区预留底部空间。
- 不提交 `apps/miniprogram/miniprogram_npm/` 和 `project.private.config.json`。

## 测试、Git 与文档

- 调试时不要自动启动服务，端口由开发者手动控制。
- 数据库操作用 mysql 命令，完成后立即 `mysqldump database_name > db/db.sql`，不保留临时 SQL 文件。
- 页面测试使用 chrome-devtools 或可用浏览器调试工具。
- 保留 `.gitignore`，忽略依赖、构建产物、IDE 配置、日志、环境变量和运行数据。
- 开发代码后不要额外生成总结文档，除非用户明确要求。
