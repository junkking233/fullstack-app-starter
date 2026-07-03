# 工作流状态 WorkflowState

每次 AI 开始开发前必须读取本文件。没有历史上下文时，以本文件判断当前阶段和下一步。

## 当前状态

| 项目 | 内容 |
| --- | --- |
| 当前阶段 | 0. 脚手架已准备 |
| 当前任务 | 尚未开始业务开发 |
| 最近完成 | 确认 Figma Connector 可访问设计稿，改为 Figma-first UI 还原流程 |
| 下一步 | 根据用户资料生成 PRD；PRD 完成后生成 Lovart Prompt |
| 阻塞项 | 暂无 |
| 最后更新 | 2026-07-03 |

## 阶段状态表

| 阶段 | 状态 | 证据/产物 | 备注 |
| --- | --- | --- | --- |
| 0. 脚手架准备 | 已完成 | `README.md`、`index.html`、`docs/工作流-Workflow/开发规范-AGENTS.md`、`apps/miniprogram/`、`apps/aiassistant/`、`apps/docker-compose.yml`、`assets/README.md`、`design/lovart/` | 克隆后必须删除脚手架 `.git` 并按真实业务项目重新初始化 Git |
| 1. PRD 需求分析 | 待开始 | `docs/产品需求-PRD/产品需求文档-PRD.md` |  |
| 2. Lovart Prompt | 待开始 | `docs/原型设计-Design/原型生图提示词-LovartPrompt.md` | 只生成页面清单、设计系统、导航规则和单页开发稿提示词；业务项目生成正式产物并过门禁后必须删除模板文件 |
| 3. Figma UI Design 设计稿拆解 | 待开始 | `docs/原型设计-Design/视觉设计系统-DESIGN.md`、`docs/原型设计-Design/页面设计文档-UIDesign.md`、`design/lovart/`，UI Design/GoalPlan 中的 Figma Frame 链接 | 以具体 Figma 页面 Frame 为准，拆出设计 token 和逐页还原标准 |
| 4. API 设计 | 待开始 | `docs/技术设计-TechDesign/API接口文档-API.md` |  |
| 5. 数据库设计 | 待开始 | `db/db.sql` |  |
| 6. Goal 长程计划 | 待开始 | `docs/工作流-Workflow/Goal长程计划-GoalPlan.md` | 按需生成 |
| 7. 功能实现 | 待开始 | `apps/`、`db/`、`assets/`、`docs/工作流-Workflow/Goal长程计划-GoalPlan.md` | 必须逐页按 Figma 还原 UI 并通过 Goal 完成门禁；未完成项不得推进到阶段 8 |
| 8. 对抗式代码审查 | 待开始 | 问题清单 | 开始前先确认阶段 7 已通过 Goal 完成门禁 |
| 9. 回归验收与修复 | 待开始 | 验收结论 |  |

## 更新规则

1. 进入新阶段时，更新本文件、根目录 `index.html` 和 GoalPlan（如存在）。
2. 完成阶段时，同步最近完成、阶段状态、证据路径和阻塞项；存在 GoalPlan 时必须先通过对应阶段门禁。
3. 遇到阻塞时，写清影响、需要用户确认的问题和建议下一步。
4. 本文件只记录状态，不写长总结。
