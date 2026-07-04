# 项目开发规范 AGENTS

本文件只放 AI 开发硬约束。人类理解流程看 `docs/项目说明-ProjectGuide.md`，任务进度看 `docs/Goal任务计划-GoalPlan.md` 和根目录 `index.html`。

## 进入项目先读

1. `README.md`
2. `AGENTS.md`
3. `docs/项目说明-ProjectGuide.md`
4. `docs/Goal任务计划-GoalPlan.md`
5. `index.html`
6. 当前任务相关文档：`docs/产品需求文档-PRD.md`、`docs/设计还原文档-UIDesign.md`、`docs/技术设计文档-TechDesign.md`

## 项目身份

- `fullstack-app-starter` 只代表脚手架仓库名，不是业务项目名。
- 脚手架地址固定为 `https://github.com/junkking233/fullstack-app-starter.git`。
- 克隆脚手架开始业务开发后，必须删除脚手架 `.git`。
- 业务项目需要版本控制时，重新 `git init` 并绑定新的业务仓库 remote。
- 不得把业务项目提交或推送到脚手架仓库。
- 业务项目完成初始化并生成正式文档后，必须删除 `templates/`，避免模板干扰后续代码开发上下文。

## 阶段规则

固定顺序：

0. 初始化/恢复上下文
1. PRD 需求分析
2. Lovart Prompt
3. Figma 设计稿拆解
4. 基于 Figma 设计稿实现功能
5. 对抗式审查、修复与提交

阶段推进必须满足：

- 当前阶段产物、证据和验收项全部完成。
- `docs/Goal任务计划-GoalPlan.md` 中对应复选框已勾选或明确不适用。
- `index.html` 与 GoalPlan 的当前阶段、当前任务、未完成项、缺陷和阻塞一致。
- 第 2 步 Lovart Prompt 完成后，必须从 `design/lovart/原型生图提示词-LovartPrompt.md` 同步 `index.html` 的 Lovart 翻页复制区；该区域仅在 `data-current-stage="2"` 时显示，其他阶段只显示进度控制看板。
- 第 4 步未通过 Goal 完成门禁前，不得进入第 5 步。

## ScopeBudget

- PRD 必须建立 `ScopeBudget` 范围预算。
- 默认角色不超过 3 个。
- 每个角色 P0/P1 核心功能最多 5 个。
- P0/P1 才进入 Lovart、Figma 拆解、API、DB、GoalPlan 和代码实现。
- P2、暂缓功能、待确认功能不得做进首版。
- 超出预算时，先合并、删减、延期或等待用户确认。

## 设计与 UI 还原

- Lovart 只生成单页开发稿，不生成作品集总览、交互概览、独立状态稿或独立弹层稿。
- Lovart PNG/PSD 放 `design/lovart/`，并沉淀到 Figma。
- Lovart Prompt 正式文档写入 `design/lovart/原型生图提示词-LovartPrompt.md`，并同步到 `index.html` 的阶段 2 翻页复制区；每张卡片必须包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”。
- Figma 页面 Frame 是视觉还原主依据。
- `docs/设计还原文档-UIDesign.md` 必须记录 Figma Frame、视觉 token、页面结构、资源、状态、接口需求和逐页还原标准。
- 如果用户只给 Figma 文件链接或 Page 根节点链接，先按页面编号、页面名和 Frame 名称自动匹配 Frame；只有重名、缺失或冲突时才要求用户补具体 Frame 链接。
- 第 4 步实现 Web 或小程序页面前，必须重新读取当前页面对应的 Figma Frame，不能只读 `docs/设计还原文档-UIDesign.md` 或 Lovart PNG/PSD 就写页面。
- 每个页面都必须在 GoalPlan 记录 Figma 读取证据：页面编号、Frame/nodeId、读取时间、提取到的尺寸/颜色/字号/间距/圆角/图标/图片要点、实现文件和对照结论。
- 无法访问 Figma、找不到 Frame、额度不足或无法截图对照时，只能把该页面标为阻塞/待验收，不能标为 1:1 还原完成。
- Web/小程序每完成一个页面，都要在 GoalPlan 勾选对应 UI 还原项或写明阻塞。
- Vant Weapp / Element Plus 只能作为组件基础，不能用默认视觉替代 Figma。

## 技术约束

- 后端端口 `8888`，Web 端口 `9999`，AI 助手端口 `8000`。
- Docker Compose 固定在 `apps/docker-compose.yml`。
- 不创建 MySQL 容器，统一连接已有 `mysql-docker`。
- 不使用 Nginx 容器处理网页请求。
- MySQL：`root/admin123`，连接使用 `--default-character-set=utf8mb4`。
- 管理员密码 `admin123`，普通用户密码 `123456`，MD5，不加 salt。
- Java 项目禁止 Lombok；Spring Boot `3.3.4`，JDK `21`，MyBatis Plus `3.5.7`，MySQL 驱动 `8.0.26`。
- Web 使用 Vue + Vite + npm + Element Plus，禁止 Vue CLI，禁止紫色主色。
- 小程序使用原生 WXML/WXSS/JS/JSON + Vant Weapp，不使用 uni-app，AppID 固定 `wxd84d204ed36b05b5`。

## 调试与提交

- 不自动启动服务，端口由开发者手动控制。
- 数据库操作后立即导出 `db/db.sql`，不保留临时 SQL 文件。
- 页面测试使用 chrome-devtools、Browser 或可用浏览器调试工具。
- 保留 `.gitignore`，忽略依赖、构建产物、IDE 配置、日志、环境变量和运行数据。
- 开发代码后不要额外生成总结文档，除非用户明确要求。
