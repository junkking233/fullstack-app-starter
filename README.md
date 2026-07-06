# 业务项目脚手架 AppScaffold

这是一个全栈业务项目脚手架，包含 Spring Boot 后端、Vue Web 前端、原生微信小程序、FastAPI AI 助手服务、MySQL 脚本，以及 `Lovart -> Stitch -> Figma -> 代码实现` 的 Figma-first 开发工作流。

`fullstack-app-starter` 只是远程脚手架仓库名，不是业务项目名。每次克隆后，先把目录和文档中的项目名改成真实业务项目名，并删除脚手架自带 `.git`，避免把业务代码提交回脚手架仓库。

脚手架地址：`https://github.com/junkking233/fullstack-app-starter.git`

## 新项目先看

1. `README.md`：项目结构和启动信息。
2. `AGENTS.md`：AI 开发硬约束。
3. `docs/项目说明-ProjectGuide.md`：给人看的 6 步工作流。
4. `docs/Goal任务计划-GoalPlan.md`：当前任务、复选框、缺陷和阻塞。
5. `index.html`：可直接打开的进度看板；仅第 2 步显示 Lovart 提示词翻页复制区。

## 目录结构

```text
{真实业务项目名}/
├── AGENTS.md
├── README.md
├── index.html
├── apps/
│   ├── backend/
│   ├── frontend/
│   ├── miniprogram/
│   ├── aiassistant/
│   └── docker-compose.yml
├── assets/
│   ├── README.md
│   ├── icon-set.html
│   └── icons/
├── db/
│   └── db.sql
├── design/
│   ├── lovart/
│   │   └── pages/                # Lovart 单页原型图，可选生成
│   └── stitch/                   # Stitch 重建后的 screen/HTML/截图，可选生成
├── docs/
│   ├── 项目说明-ProjectGuide.md
│   ├── 产品需求文档-PRD.md
│   ├── 设计还原文档-UIDesign.md
│   ├── 技术设计文档-TechDesign.md
│   └── Goal任务计划-GoalPlan.md
└── templates/
    ├── 原型生图提示词模板-LovartPromptTemplate.md
    ├── 设计还原文档模板-UIDesignTemplate.md
    └── Goal任务计划模板-GoalPlanTemplate.md
```

正式业务项目主要看 `docs/` 的 5 个文件。`templates/` 只用于生成正式文档，业务项目完成初始化并生成正式文档后必须删除模板，减少上下文噪音。

## 6 步工作流

| 顺序 | 阶段 | 重点产物 |
| --- | --- | --- |
| 0 | 初始化/恢复上下文 | 从脚手架地址克隆到真实项目目录、删除脚手架 `.git`、确认当前阶段 |
| 1 | PRD 需求分析 | `docs/产品需求文档-PRD.md` |
| 2 | Lovart Prompt | `design/lovart/原型生图提示词-LovartPrompt.md`、`index.html` 阶段 2 Lovart 翻页复制区 |
| 3 | Stitch 重建与 Figma 设计稿拆解 | `design/stitch/`、`docs/设计还原文档-UIDesign.md` |
| 4 | 基于 Figma 实现功能 | `apps/`、`db/db.sql`、`docs/技术设计文档-TechDesign.md`、`docs/Goal任务计划-GoalPlan.md` |
| 5 | 对抗式审查、修复与提交 | 问题清单、修复提交 |

核心门禁：

- PRD 先做 `ScopeBudget`：默认角色不超过 3 个，每个角色 P0/P1 核心功能最多 5 个。
- Lovart、Stitch、Figma、API、DB、代码实现只做 P0/P1；P2/暂缓功能不得进入首版。
- 第 2 步默认生成可复制的 Lovart 单页提示词；如已安装 `lovart-skill` 并配置 `LOVART_ACCESS_KEY` / `LOVART_SECRET_KEY`，可自动生成单页原型图到 `design/lovart/pages/`，但不能因此扩大页面范围。自动出图固定优先 `generate_image_gpt_image_2_medium`，APP/小程序默认 9:16，网页默认 16:9，默认先切 `unlimited` 省积分，每个新业务需求都新建以业务中文名命名的 Lovart Project，并在生成后校正 Project 名称。
- 推荐设计链路：Lovart 生成 P0/P1 单页高保真图片；Stitch 用 Lovart 图片 + 页面完整提示词重建可编辑 UI screen；再通过 AI 自动落地或用户在 Stitch 页面 Copy/Paste 到目标 Figma 文件，形成可读取的 Figma Frame；代码阶段仍以 Figma Frame 为准。
- Stitch 是可选执行器：只有安装 `stitch-design`、`stitch-utilities` 且 Stitch MCP/API Key 可用时才自动执行；否则记录为待手动形成 Figma Frame，不阻塞已有 Figma 设计稿的拆解。
- AI 不能伪造 Figma nodeId：如果无法自动完成 Stitch -> Figma，就必须把 Stitch Project/screenId 和操作说明交给用户，由用户手动 Copy/Paste 后回传 Frame 链接。
- UI 还原以 Figma Frame 为准，Lovart PNG/PSD、Stitch HTML 和截图只做备份参考。
- 第 4 步实现 Web/小程序页面前，必须逐页重新读取 Figma Frame，并在 GoalPlan 记录读取证据和对照结论；不能只凭 UIDesign 摘要或组件库默认样式开发。
- 第 2 步完成后，必须从 `design/lovart/原型生图提示词-LovartPrompt.md` 同步 `index.html` 的 Lovart 翻页复制区：每张卡片包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”，用户按上一张/下一张逐个复制到 Lovart 出图。该翻页区只在 `data-current-stage="2"` 时显示，其他阶段只保留进度看板。
- 第 4 步未通过 Goal 复选框和完成门禁前，不得进入对抗式审查。

## 技术约束

| 模块 | 规则 |
| --- | --- |
| 后端 | Spring Boot `3.3.4`、JDK `21`、MyBatis Plus `3.5.7`、MySQL 驱动 `8.0.26`、禁止 Lombok |
| Web | Vue + Vite + npm + Element Plus，前端端口 `9999`，禁止 Vue CLI，禁止紫色主色 |
| 小程序 | 原生 WXML/WXSS/JS/JSON + Vant Weapp，AppID `wxd84d204ed36b05b5`，目录固定 `apps/miniprogram/` |
| AI 助手 | FastAPI，端口 `8000`，可按需删除 |
| 后端端口 | `8888` |
| Docker Compose | 固定 `apps/docker-compose.yml`，不创建 MySQL/Nginx 容器 |
| MySQL | 使用已有 `mysql-docker`，账号 `root/admin123` |
| 密码 | 管理员 `admin123`，普通用户 `123456`，MD5，不加 salt |

## 手动启动

AI 不自动启动服务。需要运行时由开发者手动执行：

```bash
docker compose -f apps/docker-compose.yml up --build -d
docker compose -f apps/docker-compose.yml down
```

小程序依赖：

```bash
cd apps/miniprogram
npm install
```

导入数据库：

```bash
docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db/db.sql
```

导出数据库：

```bash
docker exec mysql-docker mysqldump -uroot -padmin123 --default-character-set=utf8mb4 template_db > db/db.sql
```

## 图标资源

共享图标在 `assets/icons/`，预览打开 `assets/icon-set.html`。Web 图标副本放 `apps/frontend/src/assets/icons/`，小程序图标副本放 `apps/miniprogram/assets/icons/`。缺图标时先补共享库，再复制到子应用。
