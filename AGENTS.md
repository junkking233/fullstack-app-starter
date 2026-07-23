# 项目开发规范 AGENTS

本文件只放 AI 开发硬约束。人类理解流程看 `docs/项目说明-ProjectGuide.md`；机器可判定的阶段状态以 `workflow/state.json` 为唯一数据源，任务证据看 `docs/Goal任务计划-GoalPlan.md`，摘要看根目录 `index.html`。

## 进入项目先读

1. `README.md`
2. `AGENTS.md`
3. `docs/项目说明-ProjectGuide.md`
4. `docs/Goal任务计划-GoalPlan.md`
5. `workflow/state.json`
6. `index.html`
7. 当前任务相关文档：`docs/产品需求文档-PRD.md`、`docs/设计还原文档-UIDesign.md`、`docs/技术设计文档-TechDesign.md`

## 项目身份

- `fullstack-app-starter` 只代表脚手架仓库名，不是业务项目名。
- 脚手架地址固定为 `https://github.com/junkking233/fullstack-app-starter.git`。
- 克隆脚手架开始业务开发后，必须删除脚手架 `.git`。
- 业务项目需要版本控制时，重新 `git init` 并绑定新的业务仓库 remote。
- 不得把业务项目提交或推送到脚手架仓库。
- 业务项目在阶段 1-3 生成正式文档后、进入阶段 4 开发前，必须删除 `templates/`，避免模板干扰代码开发上下文。
- 模板仓库保持 `project.mode=template`、阶段 0 `pending`、总进度 `0/6`；不得把模板准备状态记作业务阶段完成。
- 业务副本解除脚手架 Git 关联后，使用 `python3 scripts/workflow.py init --project-name <中文名> --database-name <独立库名> --compose-project-name <slug>` 初始化；目标数据库已存在时必须确认归属并显式使用 `--reuse-existing-database`。
- 阶段 0 完成前必须运行 `python3 scripts/workflow.py preflight` 和 `validate`，并确认业务 remote 不指向脚手架仓库。

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
- 阶段状态只允许 `pending`、`in_progress`、`blocked`、`completed`、`not_applicable`；只检查当前阶段门禁，未来阶段的待开始项不得阻塞当前阶段。
- 当前阶段所有 gate 必须为 `completed` 或 `not_applicable` 后才能标记完成；`blocked` 必须记录原因和恢复动作。
- 修改 `workflow/state.json` 或正式 Lovart Prompt 后，必须运行 `python3 scripts/workflow.py sync` 和 `validate`，禁止手工双写看板状态或提示词数组。
- 第 2 步 Lovart Prompt 完成前，必须生成 `design/lovart/原型生图提示词-LovartPrompt.md`、运行工作流同步，并调用当前环境可用的 Lovart 能力新建业务中文名 Project、生成 P0/P1 单页原型图到 `design/lovart/pages/`、把实际能力标识/project/thread/输出文件/失败原因写入 GoalPlan；缺任一项不得推进阶段 3。
- 第 4 步未通过 Goal 完成门禁前，不得进入第 5 步。
- 第 4 步开始开发前必须完成任务拆解，并记录采用串行开发还是 Git Worktree 并行开发。选择串行时写清原因；选择 Worktree 时写清任务、分支、worktree 路径、修改范围、依赖和合并顺序。

## ScopeBudget

- PRD 必须建立 `ScopeBudget` 范围预算。
- 默认角色不超过 3 个。
- 每个角色 P0/P1 核心功能最多 5 个。
- 页面数量必须受控：单端首版建议不超过 8-10 个页面，多端合计超过 12 个页面时先合并、删减或延期。
- P0/P1 才进入 Lovart、Stitch、Figma 拆解、API、DB、GoalPlan 和代码实现。
- P2、暂缓功能、待确认功能不得做进首版。
- 超出预算时，先合并、删减、延期或等待用户确认。

## 设计与 UI 还原

- Lovart 只生成单页开发稿，不生成作品集总览、交互概览、独立状态稿或独立弹层稿。
- Lovart PNG/PSD 放 `design/lovart/`，并经 Stitch 重建后由 Figma MCP 同步到目标 Figma 文件，形成真实可编辑 Figma Frame；截图或图片不能作为唯一图层。
- Lovart Prompt 正式文档写入 `design/lovart/原型生图提示词-LovartPrompt.md`，再运行 `workflow.py sync` 自动生成 `index.html` 的阶段 2 翻页数据；每张卡片必须包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”。
- 第 2 步必须调用当前环境实际可用的 Lovart 能力出图（能力名可能显示为 `lovart-api`、`lovart-skill` 或其他已安装标识），不能只生成提示词文档或只同步 `index.html` 就标记完成。
- Lovart 只允许生成 P0/P1 单页开发稿，产物保存到 `design/lovart/pages/`，每次显式使用 `--prefix "Pxx-页面名"`；并在 GoalPlan 记录生成方式、输出文件、Lovart project/thread、模型、画幅、原模式/恢复结果和失败原因。
- Lovart 图片模型固定优先 `generate_image_gpt_image_2_medium`；APP/微信小程序页面默认 9:16，网页页面默认 16:9。
- 没有可用 Lovart 能力、缺少认证、额度不足、生成失败或没有输出文件时，必须把阶段 2 标记为阻塞并记录恢复动作；不得把“手动复制提示词路径”当作阶段完成。
- 默认按省积分策略出图：生成前先执行 `query-mode` 记录原账户模式，再切换 `set-mode --unlimited`；除非用户明确要求加速或 fast credits，不主动使用 fast 模式。批次正常完成或失败退出后恢复原模式，用户明确要求保持新模式时除外。
- 批量出图前必须记录实际能力标识、认证、额度、模型、页面数量和预计批次，并获得用户对本批额度消耗的明确批准；不能把普通开发授权视为付费生成授权。
- 先生成 1 张代表页面并获得用户方向确认，再生成其余页面；代表页未确认不得批量消耗额度。
- 每个新业务需求都必须新建 Lovart Project，Project 名称使用业务中文名称；不要把新业务原型图生成到旧项目里。生成后必须再次检查并校正本地 Project 名称，防止被 prompt 前缀覆盖。
- Lovart 多页面出图必须受控并发执行：同一业务共用同一个 Lovart Project，每个 P0/P1 页面使用独立新 thread 并显式传入新 Project 的 `--project-id`，默认并发上限为 3；页面数量超过 3 时分批执行。
- 每个页面只生成 1 张单页开发稿，不生成作品集、交互图、状态图或多变体；同一页面修正重试才允许复用该页面 thread。
- 并发出图遇到 409/429、额度、风控、下载失败或无输出文件时，先记录失败页面和原因，再降低并发或排队重试；不得连续无控制重试消耗额度。
- Lovart 能力不能替代 Figma；Lovart 出图后仍要形成真实 Figma Frame，第 4 步代码还原仍以 Figma Frame 为准。
- Stitch 是 Lovart 与 Figma 之间的必选重建层：`Lovart 单页图 + 页面完整提示词 -> Stitch UI screen -> Figma MCP 同步 -> Figma Frame`。
- 必须有可执行 Stitch 重建和上传能力（当前环境可能以多个 `stitch-design:*`、`stitch-utilities:*` 能力暴露），且 Stitch MCP/API Key 可用。还必须有可写入目标 Figma 文件的 Figma MCP 能力。阶段 3 必须先记录实际能力与额度，再自动执行 Stitch 和 Figma 同步；未配置、调用失败、无 screenId、无 htmlCode/designSystem/截图备份或没有真实可编辑 Figma Frame 时，必须记录阻塞，不得推进阶段 4。
- 每个新业务需求必须新建或定位同名 Stitch Project；每个 P0/P1 页面只上传当前 Lovart 单页图，并用对应页面提示词重建 1 个 UI screen，不生成多变体、作品集图或额外状态图。
- Stitch 上传图片或 DESIGN.md 前必须确认文件路径、大小和目标 Project；不得在用户不知情时消耗 Stitch 额度。
- Stitch 输出的 screen 信息、htmlCode、designSystem 和截图备份归档到 `design/stitch/` 或 `.stitch/`，并在 GoalPlan 记录 Project、screenId、来源 Lovart 图、页面提示词、目标 Figma 文件、Figma MCP 同步方式、Figma Frame 链接/nodeId 和可编辑节点检查结果。
- `Stitch -> Figma` 默认由 AI 通过 Stitch MCP + Figma MCP 自动完成：读取 Stitch Project/screen/htmlCode/designSystem/screenMetadata，使用 Figma MCP 在目标 Figma 文件创建或更新 `Pxx-页面名` Frame，并把 fileKey、Page、Frame 链接、nodeId 和可编辑节点检查结果写入 GoalPlan 与 UIDesign。
- 不得要求用户默认手动 Copy/Paste，也不得伪造 Figma nodeId。MCP 权限、额度或能力不可用时，阶段 3 必须记录阻塞和恢复动作；只有用户明确同意时，手动 Copy/Paste 才能作为降级方案。
- Figma Frame 存在和用户设计确认是两个独立门禁；用户未明确确认 P0/P1 页面设计可开发时，不得进入阶段 4。
- Stitch 不能替代 Figma，HTML/截图也不能作为最终设计稿。若最终没有可访问且可编辑的 Figma Frame，第 4 步必须阻塞，不能作为 1:1 完成依据。
- Figma 页面 Frame 是视觉还原主依据。
- `docs/设计还原文档-UIDesign.md` 必须记录 Figma Frame、视觉 token、页面结构、资源、状态、接口需求和逐页还原标准。
- 如果用户只给 Figma 文件链接或 Page 根节点链接，先按页面编号、页面名和 Frame 名称自动匹配 Frame；只有重名、缺失或冲突时才要求用户补具体 Frame 链接。
- 第 4 步实现 Web 或小程序页面前，必须重新读取当前页面对应的 Figma Frame，不能只读 `docs/设计还原文档-UIDesign.md` 或 Lovart PNG/PSD 就写页面。
- 每个页面都必须在 GoalPlan 记录 Figma 读取证据：页面编号、Frame/nodeId、读取时间、提取到的尺寸/颜色/字号/间距/圆角/图标/图片要点、实现文件和对照结论。
- 无法访问 Figma、找不到 Frame、额度不足或无法截图对照时，只能把该页面标为阻塞，不能标为 1:1 还原完成。
- Web/小程序每完成一个页面，都要在 GoalPlan 勾选对应 UI 还原项或写明阻塞。
- Vant Weapp / Element Plus 只能作为组件基础，不能用默认视觉替代 Figma。

## Git Worktree 并行开发

- Git Worktree 只作为阶段 4 和阶段 5 的执行模式，不新增主流程阶段。
- 启用前必须先拆分 P0/P1 任务，确认任务之间文件范围清楚、依赖顺序明确、不会同时修改同一批核心文件。
- 适合并行的任务包括独立页面、独立 API、独立测试修复、独立资源整理；不适合并行的任务包括全局鉴权、公共请求层、全局样式 token、数据库核心结构、工作流状态和跨端共享契约。
- 推荐由主控目录创建 `stage4/integration` 集成分支，每个任务使用 `ai/<task>` 独立分支和项目目录外部的独立 worktree。额外 worktree 不得创建在业务项目目录内部。
- 创建 worktree 前必须确认集成分支已经包含最新工作流、接口契约、设计还原文档和基础代码；主控目录未提交的修改不会自动进入新 worktree。
- 每个 worktree 是独立工作区，未跟踪的 `apps/.env`、`node_modules`、构建产物和本地缓存不会共享。Worker 运行局部检查前必须按任务范围复制或重建本地 `.env` 并安装必要依赖；这些本地文件和依赖目录不得提交。
- Worker worktree 只处理分配给自己的文件范围。Worker 不得推进 `workflow/state.json`，不得手工修改 `workflow/state.generated.js`、`index.html` 或 GoalPlan 生成区域，不得提交或推送。
- 全局状态、GoalPlan 任务矩阵、阶段门禁和 `index.html` 只能在主控目录统一更新。
- 每个 Worker 完成后先运行与任务范围相称的局部检查，并把修改范围、检查结果、阻塞和 Figma 对照证据交回主控目录。
- 主控目录逐个合并任务分支。每次合并后检查冲突、接口契约、数据库快照、资源路径和 UI 还原证据；全部合并后运行完整检查。
- 阶段 5 必须运行 `python3 scripts/workflow.py worktree-status` 作为 Git/Worktree 安全证据。启用过 Worktree 时，还必须确认临时 worktree 已移除或写明保留原因，并执行 `git worktree prune`。
- 不得让多个 Agent 在同一个工作目录中并行修改代码；并行开发必须使用独立 worktree 或明确改为串行。

## 技术约束

- 后端端口 `8888`，Web 端口 `9999`，AI 助手端口 `8000`。
- Docker Compose 固定在 `apps/docker-compose.yml`。
- Compose 默认管理后端和 Web；AI 助手使用 `--profile ai` 可选启用，仍由同一 Compose 管理。
- 不创建 MySQL 容器，统一连接已有 `mysql-docker`。
- 不使用 Nginx 容器处理网页请求。
- MySQL：`root/admin123`，连接使用 `--default-character-set=utf8mb4`。
- 每个业务项目必须使用独立数据库名；导入前先检查库是否存在和归属，禁止静默复用 `template_db`。
- `db/db.sql` 是快照，不是迁移系统。已有数据库只做前向非破坏性变更，禁止导入包含 `DROP TABLE` 的 dump；导出必须使用 README 规定的安全参数。
- 管理员密码 `admin123`，普通用户密码 `123456`，MD5，不加 salt。
- Java 项目禁止 Lombok；Spring Boot `3.3.4`，JDK `21`，MyBatis Plus `3.5.7`，MySQL 驱动 `8.0.26`。
- Web 使用 Vue + Vite + npm + Element Plus，禁止 Vue CLI，禁止紫色主色。
- 小程序使用原生 WXML/WXSS/JS/JSON + Vant Weapp，不使用 uni-app，AppID 固定 `wxd84d204ed36b05b5`。

## 调试与提交

- 不自动启动服务，端口由开发者手动控制。
- 数据库操作后立即导出 `db/db.sql`，不保留临时 SQL 文件。
- 页面测试使用 chrome-devtools、Browser 或可用浏览器调试工具。
- 阶段 5 至少执行工作流校验、后端测试、前端类型检查与构建、小程序静态检查和 AI 服务测试；没有测试用例时不得用“命令退出 0”冒充功能已验证。
- 保留 `.gitignore`，忽略依赖、构建产物、IDE 配置、日志、环境变量和运行数据。
- 提交前再次检查 `git remote -v`，业务项目 remote 指向脚手架仓库时禁止提交或推送。
- 开发代码后不要额外生成总结文档，除非用户明确要求。
