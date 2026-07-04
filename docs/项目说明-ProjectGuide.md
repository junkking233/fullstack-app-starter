# 项目说明 ProjectGuide

本文件给第一次接手项目的人看，说明这套脚手架怎么从需求走到代码。执行细节由 `AGENTS.md`、`templates/`、Skills 和飞书提示词承接。

脚手架地址：`https://github.com/junkking233/fullstack-app-starter.git`

## 先看哪里

| 想知道什么 | 看哪里 |
| --- | --- |
| 项目是什么、怎么启动 | `README.md` |
| AI 必须遵守的硬约束 | `AGENTS.md` |
| 当前做到哪一步 | `index.html` |
| 任务清单、缺陷、阻塞 | `docs/Goal任务计划-GoalPlan.md` |
| 需求边界 | `docs/产品需求文档-PRD.md` |
| Figma 设计还原规则 | `docs/设计还原文档-UIDesign.md` |
| API / 数据库计划 | `docs/技术设计文档-TechDesign.md` |

## 6 步工作流

| 阶段 | 目标 | 产物 |
| --- | --- | --- |
| 0. 初始化/恢复上下文 | 从脚手架地址克隆到真实项目目录、删除脚手架 `.git`、判断当前阶段 | `index.html`、`GoalPlan` |
| 1. PRD 需求分析 | 用第一性原理确认用户、问题、角色、流程和边界 | `产品需求文档-PRD.md` |
| 2. Lovart Prompt | 基于 PRD 生成受控的单页原型提示词 | `design/lovart/原型生图提示词-LovartPrompt.md` |
| 3. Figma 设计稿拆解 | 把 Figma Frame 拆成可还原的视觉与页面规则 | `设计还原文档-UIDesign.md` |
| 4. 基于 Figma 实现功能 | 实现页面、接口、数据库、状态、资源和联调 | `apps/`、`db/db.sql`、`技术设计文档-TechDesign.md`、`GoalPlan` |
| 5. 对抗式审查、修复与提交 | 查遗漏、范围膨胀、设计偏差、接口漂移和运行风险 | 问题清单、修复提交 |

## 范围预算 ScopeBudget

PRD 阶段先控制范围，避免后续原型和代码变复杂。

- 默认角色不超过 3 个。
- 每个角色 P0/P1 核心功能最多 5 个。
- P0 是业务闭环必需，P1 是首版体验必需，P2 是后续迭代。
- Lovart、Figma、API、DB 和代码只做 P0/P1。
- P2、暂缓功能和待确认功能只记录，不进入首版实现。

## Figma-first UI 还原

- Lovart 用来生成单页原型图，出图后沉淀到 Figma。
- 代码还原以具体 Figma 页面 Frame 为准。
- Figma 中多出来但不属于 P0/P1 的页面，只标记为“仅参考/暂不开发”。
- 每个页面实现后，要在 GoalPlan 中记录 UI 还原结果。
- 如果无法截图或访问 Figma，也要完成静态对照并写清阻塞。

## Goal 复选框机制

`docs/Goal任务计划-GoalPlan.md` 是长程任务的核心跟踪文件。

- 每个阶段、页面、接口、数据、资源、UI 还原项都用复选框跟踪。
- AI 完成任务后必须勾选对应项。
- 未完成、待验收、阻塞或空白项存在时，不能推进到下一阶段。
- `index.html` 只展示进度摘要，详细任务以 GoalPlan 为准。

## 模板使用

`templates/` 是脚手架给 AI 用的文档模板，不是业务项目交付物。

| 模板 | 用途 |
| --- | --- |
| `templates/原型生图提示词模板-LovartPromptTemplate.md` | 生成 Lovart 单页原型提示词 |
| `templates/设计还原文档模板-UIDesignTemplate.md` | 生成 Figma 设计还原文档 |
| `templates/Goal任务计划模板-GoalPlanTemplate.md` | 生成长程任务计划和验收清单 |

业务项目完成初始化并生成正式文件后，必须删除 `templates/`，避免模板内容干扰后续代码开发上下文。
