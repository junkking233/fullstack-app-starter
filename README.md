# 业务项目脚手架 AppScaffold

这是一个全栈业务项目脚手架，包含 Spring Boot 后端、Vue Web 前端、原生微信小程序、FastAPI AI 助手服务、MySQL 脚本，以及 `Lovart -> Stitch -> Figma -> 代码实现` 的 Figma-first 开发工作流。阶段 4 支持 Git Worktree 并行开发模式，用于把边界清楚的代码任务分配到独立工作区后统一合并验证。

`fullstack-app-starter` 只是远程脚手架仓库名，不是业务项目名。每次克隆后，先把目录和文档中的项目名改成真实业务项目名，并删除脚手架自带 `.git`，避免把业务代码提交回脚手架仓库。

脚手架地址：`https://github.com/junkking233/fullstack-app-starter.git`

## 新项目先看

1. `README.md`：项目结构和启动信息。
2. `AGENTS.md`：AI 开发硬约束。
3. `docs/项目说明-ProjectGuide.md`：给人看的 6 步工作流。
4. `workflow/state.json`：阶段状态的唯一数据源。
5. `docs/Goal任务计划-GoalPlan.md`：任务证据、缺陷和阻塞；状态区由脚本生成。
6. `index.html`：读取同一状态源的进度看板；仅第 2 步显示自动生成的 Lovart 提示词翻页区。

## 业务项目初始化

模板仓库始终保持 `project.mode=template` 和 `0/6`。复制到真实业务目录后：

1. 删除脚手架 `.git`，按需重新 `git init` 并绑定业务仓库，确认 remote 不再指向脚手架。
2. 初始化独立业务名称、数据库、Compose 项目名和随机 Token 密钥：

```bash
python3 scripts/workflow.py init \
  --project-name "真实业务中文名" \
  --database-name real_business_db \
  --compose-project-name real-business
```

如果目标数据库已存在，脚本会停止；只有确认它属于当前业务时才能增加 `--reuse-existing-database`。随后执行：

```bash
python3 scripts/workflow.py preflight
python3 scripts/workflow.py sync
python3 scripts/workflow.py validate
```

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
├── workflow/
│   ├── state.json                # 阶段状态唯一数据源
│   └── state.generated.js        # 自动生成，供 index.html 读取
├── scripts/
│   └── workflow.py
├── design/
│   ├── lovart/
│   │   └── pages/                # Lovart 单页原型图，阶段 2 必选产物
│   └── stitch/                   # Stitch screen/htmlCode/designSystem/截图备份，阶段 3 必选产物
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

正式业务项目主要看 `docs/` 的 5 个文件。`templates/` 只用于阶段 1-3 生成正式文档；进入阶段 4 开发前必须删除模板，减少上下文噪音。

## 6 步工作流

| 顺序 | 阶段 | 重点产物 |
| --- | --- | --- |
| 0 | 初始化/恢复上下文 | 解除脚手架 Git 关联，运行 `workflow.py init/preflight/validate`，完成业务、数据库、Compose 与密钥隔离 |
| 1 | PRD 需求分析 | `docs/产品需求文档-PRD.md` |
| 2 | Lovart Prompt | `design/lovart/原型生图提示词-LovartPrompt.md`、`index.html` 阶段 2 Lovart 翻页复制区、`design/lovart/pages/` 单页原型图、GoalPlan Lovart 执行记录 |
| 3 | Stitch 重建与 Figma 设计稿拆解 | `design/stitch/`、`docs/设计还原文档-UIDesign.md` |
| 4 | 基于 Figma 实现功能 | 任务拆解、串行或 Git Worktree 并行模式选择、`apps/`、`db/db.sql`、`docs/技术设计文档-TechDesign.md`、`docs/Goal任务计划-GoalPlan.md` |
| 5 | 对抗式审查、修复与提交 | 问题清单、Worktree 合并/清理复核、修复提交 |

核心门禁：

- PRD 先做 `ScopeBudget`：默认角色不超过 3 个，每个角色 P0/P1 核心功能最多 5 个。
- 页面数量也要受控：单端首版建议不超过 8-10 个页面，多端合计超过 12 个页面时先合并、删减或延期。
- Lovart、Stitch、Figma、API、DB、代码实现只做 P0/P1；P2/暂缓功能不得进入首版。
- 第 2 步不是“只写提示词”。原型图固定指定 `generate_image_seedream_v5_pro`、1K 输出；APP/小程序为 9:16，网页为 16:9。先探测并记录当前环境实际可用的 Lovart 能力、认证和额度，获得用户对页面数量与本批额度消耗的批准；再切换账户 `fast` 模式并使用 `--mode fast` 生成 1 张代表页，确认方向后受控并发生成其余 P0/P1 单页图。修改正式 Prompt 后运行 `workflow.py sync` 自动生成看板翻页数据。未安装、未配置、未获额度批准、代表页未确认、模型不符或生成失败时，阶段 2 只能记录为阻塞。
- 固定设计链路：Lovart 生成 P0/P1 单页高保真图片；Stitch 用 Lovart 图片 + 页面完整提示词重建 UI screen；再由 Figma MCP 将 Stitch 的 htmlCode/designSystem/screenMetadata 同步到目标 Figma 文件，形成可读取、可编辑的 Figma Frame；代码阶段仍以 Figma Frame 为准。
- Stitch 和 Figma 都是必选门禁：先探测当前环境实际暴露的 Stitch/Figma MCP 能力并记录认证、额度和目标文件；阶段 3 必须产出 Stitch Project/screenId/htmlCode/designSystem/截图备份，并通过 Figma MCP 创建或更新 `Pxx-页面名` Frame，记录真实 Frame 链接/nodeId 和可编辑节点检查结果。真实 Frame 和用户明确设计确认缺一不可。
- AI 默认负责 Stitch -> Figma MCP 同步：不得要求用户手动 Copy/Paste，不得把 Stitch 截图或 Lovart 图片作为 Figma 中唯一图层。只有 MCP 权限、额度或工具能力不可用时，才记录阻塞与恢复动作；手动 Copy/Paste 只能作为用户明确同意的降级方案，不能替代默认门禁。
- UI 还原以 Figma Frame 为准，Lovart PNG/PSD、Stitch HTML 和截图只做备份参考。
- 第 4 步实现 Web/小程序页面前，必须逐页重新读取 Figma Frame，并在 GoalPlan 记录读取证据和对照结论；不能只凭 UIDesign 摘要或组件库默认样式开发。
- 第 4 步开发前必须先拆分 P0/P1 任务，并记录串行开发或 Git Worktree 并行开发模式。启用 Worktree 时，主控目录保留集成分支，每个独立任务使用一个独立分支和一个位于项目目录外部的 worktree；Worker 只修改分配范围，不能推进全局状态。
- 创建 Worktree 前必须确认集成分支已经包含最新工作流、接口契约、设计还原文档和基础代码；主控目录未提交的修改不会自动进入新 worktree。
- Worktree 只适合边界清楚的并行任务，例如独立页面、独立 API、独立测试修复。全局状态文件、公共鉴权、全局样式、数据库核心结构和 `workflow/state.json` 不得被多个 worktree 同时修改。
- 第 5 步必须复核 Worktree 使用情况：运行 `python3 scripts/workflow.py worktree-status`，确认额外 worktree 不在项目目录内部；若启用了 Worktree，还要确认分支已合并、冲突已处理、临时 worktree 已移除或明确保留原因。
- 第 2 步完成前必须同时满足：正式 Lovart Prompt 已生成、`workflow.py sync` 已自动生成看板翻页数据、代表页已确认、每个 P0/P1 页面都已出图并保存，GoalPlan 已记录实际能力、用户额度批准、Project、每页 thread、批次、输出文件、模型、画幅和失败原因。
- 第 4 步未通过当前阶段 gates 和完成门禁前，不得进入对抗式审查。

## Git Worktree 并行开发

Git Worktree 是阶段 4 的可选执行模式，不是新阶段。只有当任务边界清楚、文件范围不冲突、前置设计和接口已确认时才启用；否则按串行开发执行，并在 GoalPlan 写明原因。创建 worktree 前，先确保集成分支已包含最新基线；未提交修改不会自动出现在新 worktree 中。

推荐分支模型：

```text
main
└── stage4/integration
    ├── ai/miniprogram-home
    ├── ai/backend-order-api
    └── ai/db-schema
```

推荐目录模型：

```text
真实业务项目/
../真实业务项目-worktrees/
  miniprogram-home/
  backend-order-api/
  db-schema/
```

创建示例：

```bash
git switch -c stage4/integration
mkdir -p ../真实业务项目-worktrees
git worktree add ../真实业务项目-worktrees/miniprogram-home -b ai/miniprogram-home stage4/integration
git worktree add ../真实业务项目-worktrees/backend-order-api -b ai/backend-order-api stage4/integration
python3 scripts/workflow.py worktree-status
```

每个 worktree 都是独立工作区。`.env`、`node_modules`、构建产物和其他未跟踪本地文件不会共享；Worker 运行局部检查前，需要按任务范围复制或重建本地 `.env`，并在对应端执行必要的依赖安装，例如 `npm ci --ignore-scripts`。这些本地环境文件和依赖目录不得提交。

合并示例：

```bash
git switch stage4/integration
git merge --no-ff ai/miniprogram-home
git merge --no-ff ai/backend-order-api
python3 scripts/workflow.py worktree-status
sh scripts/check-all.sh
```

清理示例：

```bash
git worktree remove ../真实业务项目-worktrees/miniprogram-home
git branch -d ai/miniprogram-home
git worktree prune
```

Worker worktree 不负责推进 `workflow/state.json`、`workflow/state.generated.js`、`index.html` 或 GoalPlan 生成区域；这些全局状态只在主控目录统一更新。

## 技术约束

| 模块 | 规则 |
| --- | --- |
| 后端 | Spring Boot `3.3.4`、JDK `21`、MyBatis Plus `3.5.7`、MySQL 驱动 `8.0.26`、禁止 Lombok |
| Web | Vue + Vite + npm + Element Plus，前端端口 `9999`，禁止 Vue CLI，禁止紫色主色 |
| 小程序 | 原生 WXML/WXSS/JS/JSON + Vant Weapp，AppID `wxd84d204ed36b05b5`，目录固定 `apps/miniprogram/` |
| AI 助手 | FastAPI，端口 `8000`，通过 Compose `ai` profile 可选启用，也可按需删除 |
| 后端端口 | `8888` |
| Docker Compose | 固定 `apps/docker-compose.yml`，不创建 MySQL/Nginx 容器 |
| MySQL | 使用已有 `mysql-docker`，账号 `root/admin123` |
| 密码 | 管理员 `admin123`，普通用户 `123456`，MD5，不加 salt |

## 手动启动

AI 不自动启动服务。首次运行先复制并填写环境配置；业务项目推荐使用 `workflow.py init` 自动生成随机密钥：

```bash
cp apps/.env.example apps/.env
python3 scripts/workflow.py preflight
```

默认只启动后端和 Web：

```bash
docker compose -f apps/docker-compose.yml up --build -d
docker compose -f apps/docker-compose.yml down
```

需要 AI 助手时：

```bash
docker compose -f apps/docker-compose.yml --profile ai up --build -d
```

小程序依赖：

```bash
cd apps/miniprogram
npm install
```

仅在独立业务数据库不存在、或已明确确认归属后导入快照：

```bash
export APP_DB_NAME=real_business_db
docker exec mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 -N \
  -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='${APP_DB_NAME}'"
docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db/db.sql
```

导出数据库：

```bash
docker exec mysql-docker mysqldump -uroot -padmin123 \
  --default-character-set=utf8mb4 \
  --single-transaction --skip-add-drop-table --skip-add-locks \
  <database_name> > db/db.sql
```

禁止把包含 `DROP TABLE` 的 dump 导入已有数据库。`db/db.sql` 只作为当前快照；已有库的结构变化先执行经过审查的前向 `ALTER TABLE`，再重新导出快照。

## 图标资源

共享图标在 `assets/icons/`，预览打开 `assets/icon-set.html`。Web 图标副本放 `apps/frontend/src/assets/icons/`，小程序图标副本放 `apps/miniprogram/assets/icons/`。缺图标时先补共享库，再复制到子应用。
