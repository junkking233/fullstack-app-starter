# 项目说明 ProjectGuide

本文件给第一次接手项目的人看，说明这套脚手架怎么从需求走到代码。执行细节由 `AGENTS.md`、`workflow/state.json`、`templates/` 和实际可用的 Skills 承接。

脚手架地址：`https://github.com/junkking233/fullstack-app-starter.git`

## 先看哪里

| 想知道什么 | 看哪里 |
| --- | --- |
| 项目是什么、怎么启动 | `README.md` |
| AI 必须遵守的硬约束 | `AGENTS.md` |
| 机器可判定的阶段状态 | `workflow/state.json` |
| 当前进度；第 2 步 Lovart 提示词翻页复制 | `index.html`（自动读取生成状态） |
| 任务清单、缺陷、阻塞 | `docs/Goal任务计划-GoalPlan.md` |
| 需求边界 | `docs/产品需求文档-PRD.md` |
| Figma 设计还原规则 | `docs/设计还原文档-UIDesign.md` |
| API / 数据库计划 | `docs/技术设计文档-TechDesign.md` |

## 6 步工作流

| 阶段 | 目标 | 产物 |
| --- | --- | --- |
| 0. 初始化/恢复上下文 | 解除脚手架 Git 关联，设置业务身份、独立数据库、Compose 项目名和密钥并执行预检 | `workflow/state.json`、`apps/.env`、`index.html`、`GoalPlan` |
| 1. PRD 需求分析 | 用第一性原理确认用户、问题、角色、流程和边界 | `产品需求文档-PRD.md` |
| 2. Lovart Prompt | 基于 PRD 生成受控单页提示词，自动生成网页翻页数据，并调用实际可用的 Lovart 能力生成 P0/P1 单页原型图 | `design/lovart/原型生图提示词-LovartPrompt.md`、`design/lovart/pages/`、`index.html`、GoalPlan Lovart 执行记录 |
| 3. Stitch 重建与 Figma 设计稿拆解 | 用 Lovart 图片和页面提示词在 Stitch 重建 UI screen，等待用户手动 Copy/Paste 到目标 Figma 文件，最后拆解 Figma Frame | `design/stitch/`、`设计还原文档-UIDesign.md` |
| 4. 基于 Figma 实现功能 | 实现页面、接口、数据库、状态、资源和联调 | `apps/`、`db/db.sql`、`技术设计文档-TechDesign.md`、`GoalPlan` |
| 5. 对抗式审查、修复与提交 | 查遗漏、范围膨胀、设计偏差、接口漂移和运行风险 | 问题清单、修复提交 |

## 范围预算 ScopeBudget

PRD 阶段先控制范围，避免后续原型和代码变复杂。

- 默认角色不超过 3 个。
- 每个角色 P0/P1 核心功能最多 5 个。
- 页面数量由需求判断，但必须受控：单端首版建议不超过 8-10 个页面，多端合计超过 12 个页面时先合并页面、删减功能或延期角色。
- P0 是业务闭环必需，P1 是首版体验必需，P2 是后续迭代。
- Lovart、Stitch、Figma、API、DB 和代码只做 P0/P1。
- P2、暂缓功能和待确认功能只记录，不进入首版实现。
- PRD 里的“必须实现的页面、必须实现的接口、必须保存的数据、暂不开发范围、补充资料”由 AI 基于需求和 ScopeBudget 判断，不要求用户提前填完整。

## Figma-first UI 还原

- Lovart 用来生成单页原型图，出图后由用户从 Stitch 手动 Copy/Paste 到 Figma，形成真实 Figma Frame。
- 第 2 步生成正式提示词后，必须运行 `workflow.py sync`，从文档自动生成 `index.html` 的 Lovart 翻页数据；每张卡片包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”。该区域仅第 2 步显示。
- 第 2 步先探测当前环境实际暴露的 Lovart 能力（名称可能是 `lovart-api`、`lovart-skill` 或其他标识），记录认证、额度、模型和画幅；获得用户对页面数量和本批额度消耗的明确批准后，才能生成 P0/P1 单页原型图。
- 先生成 1 张代表页面并获得用户方向确认，再受控并发生成其余页面。图片保存到 `design/lovart/pages/`，生成命令显式使用 `--prefix "Pxx-页面名"`；并在 GoalPlan 记录能力标识、用户批准、Project、thread、输出文件、模型、画幅和失败原因。
- Lovart Skill 自动出图固定优先使用 `generate_image_gpt_image_2_medium`；APP/微信小程序页面默认 9:16，网页页面默认 16:9。切换模式前先执行 `query-mode` 记录原账户模式，默认临时切到 `unlimited` 省积分；只有用户明确要求速度时才使用 fast 模式。批次正常完成或失败退出后恢复原模式，用户明确要求保持新模式时除外。
- 每个新业务需求都新建 Lovart Project，名称使用业务中文名称；生成后再次检查并校正 Project 名称，避免本地状态被 prompt 前缀覆盖。
- Lovart 多页面出图必须受控并发执行：同一业务共用同一个 Lovart Project，每个 P0/P1 页面使用独立新 thread 并显式传入新 Project 的 `--project-id`；默认并发上限为 3，页面数量超过 3 时分批执行。
- 每个页面只生成 1 张单页开发稿，不生成多变体、作品集图、交互图或独立状态图；同一页面微调重试时才复用该页面 thread。
- 并发出图遇到 409/429、额度、风控、下载失败或无输出文件时，先记录失败页面和原因，再降低并发或排队重试；不得连续无控制重试消耗额度。
- 没有可用 Lovart 能力、没有认证、未获额度批准、生成失败或没有输出文件时，阶段 2 必须记录阻塞和恢复动作，不得推进阶段 3。
- 固定设计链路是 `Lovart 单页图 -> Stitch UI screen -> 用户手动 Copy/Paste 到 Figma -> Figma Frame -> 代码实现`。
- Stitch 是必选重建层：先探测实际可执行的 Stitch 命名空间能力并检查 MCP/API Key 与额度；每张 Lovart 单页图和对应页面完整提示词都要上传到同名 Stitch Project，重建 1 个 UI screen。
- Stitch 完成后，AI 输出 Stitch Project、screenId、HTML/截图备份和操作说明；用户在 Stitch 页面点击 Copy/Paste to Figma，把 screen 放入目标 Figma 文件，并按 `Pxx-页面名称` 命名 Frame；用户返回 Frame 链接/nodeId 后，AI 更新 GoalPlan 和 UIDesign。
- AI 不代替用户完成 Stitch 网页 Copy/Paste，不伪造 Figma nodeId；没有用户回传的真实 Figma Frame 时，阶段 3 必须标为阻塞，不得推进阶段 4。
- 真实 Figma Frame 只证明交接完成，不代表设计已经通过；必须再获得用户对 P0/P1 页面“可进入开发”的明确确认。
- Stitch 的 HTML、截图和 screenId 只保存到 `design/stitch/` 或 `.stitch/` 做备份，并写入 GoalPlan。不要让 Stitch 生成多变体、作品集拼贴、交互大图或额外状态图。
- 如果只有 Stitch HTML/截图、没有可访问的 Figma Frame，第 4 步不能开始；必须阻塞等待真实 Figma Frame。
- 代码还原以具体 Figma 页面 Frame 为准。
- 第 4 步写页面代码前，AI 必须重新打开并读取当前页面的 Figma Frame；UIDesign 文档只是索引和摘要，不能替代 Figma 原型文件。
- 每个页面按“读取 Figma Frame -> 提取视觉 token 和图层结构 -> 实现代码 -> 截图或静态对照 -> 修复偏差 -> 在 GoalPlan 记录证据”循环推进。
- 如果无法访问 Figma、找不到 Frame、额度不足或无法截图，就把页面留在第 4 步阻塞，不能推进到对抗式审查。
- Figma 中多出来但不属于 P0/P1 的页面，只标记为“仅参考/暂不开发”。
- 每个页面实现后，要在 GoalPlan 中记录 UI 还原结果。
- 如果无法截图或访问 Figma，也要完成静态对照并写清阻塞。

## 阶段状态与门禁机制

`workflow/state.json` 是阶段状态的唯一数据源；`docs/Goal任务计划-GoalPlan.md` 是任务、证据、缺陷和阻塞的详细记录。

- 阶段状态使用 `pending / in_progress / blocked / completed / not_applicable`。
- 只检查当前阶段 gates；未来阶段的待开始项不参与当前门禁。
- `blocked` 必须记录原因与恢复动作；恢复后改回 `in_progress`。
- 修改状态或正式 Lovart Prompt 后运行 `python3 scripts/workflow.py sync`，再运行 `validate`；不要手工维护第二份阶段摘要或 HTML 提示词数组。
- `index.html` 默认展示进度摘要、任务、缺陷和阻塞；只有阶段 2 额外展示由正式 Prompt 文档自动生成的翻页区。

## 初始化和数据库安全

- 模板仓库固定为 `project.mode=template`、阶段 0 待开始、进度 `0/6`。
- 业务副本先解除脚手架 Git 关联，再运行 `workflow.py init`；脚本会拒绝 `template_db`、脚手架 remote 和未经确认的已存在数据库。
- 阶段 0 使用 `workflow.py preflight` 只读检查 `mysql-docker`、`shared-network` 和数据库是否存在。
- 每个业务项目使用独立数据库和 Compose 项目名；`db/db.sql` 是快照，不是迁移系统。
- 已有数据库只做前向非破坏性变更；禁止导入含 `DROP TABLE` 的 dump。

## 模板使用

`templates/` 是脚手架给 AI 用的文档模板，不是业务项目交付物。

| 模板 | 用途 |
| --- | --- |
| `templates/原型生图提示词模板-LovartPromptTemplate.md` | 生成 Lovart 单页原型提示词 |
| `templates/设计还原文档模板-UIDesignTemplate.md` | 生成 Figma 设计还原文档 |
| `templates/Goal任务计划模板-GoalPlanTemplate.md` | 生成长程任务计划和验收清单 |

业务项目在阶段 1-3 生成正式文件后、进入阶段 4 开发前，必须删除 `templates/`，避免模板内容干扰代码开发上下文。
