# 工作流状态 WorkflowState

每次 AI 开始开发前必须读取本文件。没有历史上下文时，以本文件判断当前阶段和下一步。

## 当前状态

| 项目 | 内容 |
| --- | --- |
| 当前阶段 | 0. 脚手架已准备 |
| 当前任务 | 尚未开始业务开发 |
| 最近完成 | 根目录 `index.html` 阶段看板视觉与对齐机制已完善 |
| 下一步 | 根据用户资料生成 PRD；PRD 完成后生成 Lovart Prompt |
| 阻塞项 | 暂无 |
| 最后更新 | 2026-07-02 |

## 阶段状态表

| 阶段 | 状态 | 证据/产物 | 备注 |
| --- | --- | --- | --- |
| 0. 脚手架准备 | 已完成 | `README.md`、`index.html`、`docs/开发规范-AGENTS.md`、`apps/miniprogram/`、`apps/aiassistant/`、`apps/docker-compose.yml`、`assets/README.md`、`design/lovart/` | 原生微信小程序 + Vant Weapp；AI 助手目录为 `apps/aiassistant/` |
| 1. PRD 需求分析 | 待开始 | `docs/产品需求文档-PRD.md` |  |
| 2. Lovart Prompt | 待开始 | `docs/原型生图提示词-LovartPrompt.md` | 按需生成 |
| 3. UI Design 设计稿拆解 | 待开始 | `docs/页面设计文档-UIDesign.md`、`design/lovart/` | 等用户放入 Lovart 设计稿 |
| 4. API 设计 | 待开始 | `docs/API接口文档-API.md` |  |
| 5. 数据库设计 | 待开始 | `db/db.sql` |  |
| 6. Goal 长程计划 | 待开始 | `docs/Goal长程计划-GoalPlan.md` | 按需生成 |
| 7. 功能实现 | 待开始 | `apps/`、`db/` |  |
| 8. 对抗式代码审查 | 待开始 | 问题清单 |  |
| 9. 回归验收与修复 | 待开始 | 验收结论 |  |

## 更新规则

1. 进入新阶段时，更新本文件、根目录 `index.html` 和 GoalPlan（如存在）。
2. 完成阶段时，同步最近完成、阶段状态、证据路径和阻塞项。
3. 遇到阻塞时，写清影响、需要用户确认的问题和建议下一步。
4. 本文件只记录状态，不写长总结。
