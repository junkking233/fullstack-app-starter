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
3. Stitch 重建与 Figma 设计稿拆解
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
- P0/P1 才进入 Lovart、Stitch、Figma 拆解、API、DB、GoalPlan 和代码实现。
- P2、暂缓功能、待确认功能不得做进首版。
- 超出预算时，先合并、删减、延期或等待用户确认。

## 设计与 UI 还原

- Lovart 只生成单页开发稿，不生成作品集总览、交互概览、独立状态稿或独立弹层稿。
- Lovart PNG/PSD 放 `design/lovart/`，并通过 AI 自动落地、Stitch Copy/Paste 或手动整理形成真实 Figma Frame。
- Lovart Prompt 正式文档写入 `design/lovart/原型生图提示词-LovartPrompt.md`，并同步到 `index.html` 的阶段 2 翻页复制区；每张卡片必须包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”。
- 第 2 步默认只生成可复制提示词；如果本地已安装 `lovart-skill` 且存在 `LOVART_ACCESS_KEY`、`LOVART_SECRET_KEY`，可把它作为可选执行器直接生成单页原型图。未安装或未配置密钥时，不得阻塞阶段完成。
- 使用 `lovart-skill` 自动出图时，只允许生成 P0/P1 单页开发稿，产物保存到 `design/lovart/pages/`，并在 GoalPlan 记录生成方式、输出文件、Lovart project/thread（如有）和失败原因（如有）。
- 使用 `lovart-skill` 自动出图时，图片模型固定优先 `generate_image_gpt_image_2_medium`；APP/微信小程序页面默认 9:16，网页页面默认 16:9。
- 默认按省积分策略出图：生成前先切换 `set-mode --unlimited`；除非用户明确要求加速或 fast credits，不主动使用 fast 模式。
- 每个新业务需求都必须新建 Lovart Project，Project 名称使用业务中文名称；不要把新业务原型图生成到旧项目里。生成后必须再次检查并校正本地 Project 名称，防止被 prompt 前缀覆盖。
- 新业务首次自动出图时必须显式传入新 Project 的 `--project-id`，并且不传旧 `--thread-id`；只有同一业务、同一页面的修正重试才允许复用该页面 thread。
- 每次只生成当前页面 1 张图，不批量生成作品集、交互图、状态图或多变体；失败时先记录原因和优化提示词，不连续重试消耗额度。
- `lovart-skill` 不能替代 Figma；Lovart 出图后仍要形成真实 Figma Frame，第 4 步代码还原仍以 Figma Frame 为准。
- Stitch 是 Lovart 与 Figma 之间的可选重建层：`Lovart 单页图 + 页面完整提示词 -> Stitch UI screen -> Figma 落地 -> Figma Frame`。
- 只有已安装 `stitch-design`、`stitch-utilities`，且 Stitch MCP/API Key 可用时，才自动执行 Stitch；未配置时不伪造结果，改为记录“待手动形成 Figma Frame/待配置 Stitch”。
- 每个新业务需求必须新建或定位同名 Stitch Project；每个 P0/P1 页面只上传当前 Lovart 单页图，并用对应页面提示词重建 1 个 UI screen，不生成多变体、作品集图或额外状态图。
- Stitch 上传图片或 DESIGN.md 前必须确认文件路径、大小和目标 Project；不得在用户不知情时消耗 Stitch 额度。
- Stitch 输出的 screen 信息和 HTML/截图备份归档到 `design/stitch/` 或 `.stitch/`，并在 GoalPlan 记录 Project、screenId、来源 Lovart 图、页面提示词、Figma 落地方式、目标文件和 Figma Frame 链接/nodeId。
- Figma 落地优先尝试 AI 自动模式：只有同时具备目标 Figma 文件链接、可用 Figma/Browser 工具、且能创建或读取到真实 Frame 时，AI 才能标记为已落地。
- AI 无法稳定操作 Stitch 网页 `Copy/Paste to Figma`、浏览器剪贴板或目标 Figma 文件时，必须进入人工交接模式：AI 输出 Stitch Project、screenId、HTML/截图备份和操作说明；用户在 Stitch 页面点击 Copy/Paste to Figma，粘贴到目标 Figma 文件并返回 Frame 链接/nodeId；AI 再更新 GoalPlan、UIDesign 和 index.html。
- Stitch 不能替代 Figma，HTML/截图也不能作为最终设计稿。若最终没有可访问的 Figma Frame，第 4 步只能标为阻塞或待验收，不能作为 1:1 完成依据。
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
