# Goal 任务计划 GoalPlan

本文件跟踪当前项目阶段、任务复选框、缺陷和阻塞。AI 每完成一项任务，都必须同步勾选本文件，并让根目录 `index.html` 展示同一进度摘要；阶段 2 完成后，还必须同步 Lovart 提示词翻页复制区。该翻页区只在阶段 2 显示，其他阶段只显示进度看板。

## 1. 当前状态

| 项目 | 内容 |
| --- | --- |
| 当前阶段 | 0. 初始化/恢复上下文 |
| 当前任务 | 脚手架已准备，等待输入真实业务需求 |
| 总进度 | 1 / 6 阶段 |
| 最近完成 | 收缩 docs 结构，建立 Goal checklist 与进度看板机制 |
| 下一步 | 基于用户资料生成 PRD，并先完成 ScopeBudget |
| 阻塞项 | 暂无 |
| 最后更新 | 2026-07-04 |

## 2. 阶段 Checklist

- [x] 0. 初始化/恢复上下文：脚手架结构、规范、资源和进度看板已就位。
- [ ] 1. PRD 需求分析：生成 `docs/产品需求文档-PRD.md`，完成 ScopeBudget。
- [ ] 2. Lovart Prompt：生成 `design/lovart/原型生图提示词-LovartPrompt.md`，只覆盖 P0/P1 单页开发稿，并同步 `index.html` Lovart 翻页复制区；如使用 `lovart-skill`，记录输出文件。
- [ ] 3. Stitch 重建与 Figma 设计稿拆解：如使用 Stitch，已重建 UI screen，并已由用户手动 Copy/Paste 到 Figma 形成真实 Frame；生成 `docs/设计还原文档-UIDesign.md`。
- [ ] 4. 基于 Figma 设计稿实现功能：完成页面、接口、数据库、资源、状态和联调。
- [ ] 5. 对抗式审查、修复与提交：完成审查、修复和提交。

## 3. ScopeBudget Checklist

- [ ] 角色不超过 3 个，或已写明合并/延期原因。
- [ ] 每个角色 P0/P1 核心功能不超过 5 个。
- [ ] P2/暂缓功能没有进入 Lovart、Stitch、Figma、API、DB 或代码实现。
- [ ] 超出范围的功能已写入待确认或后续迭代。

## 4. 实现任务 Checklist

业务项目生成后，把每个页面、接口、数据表、资源和 UI 还原任务写成可勾选项。

### 页面与 UI 还原

- [ ] Lovart 生成方式：手动复制提示词 / `lovart-skill` 自动出图 / 不适用。
- [ ] Lovart 项目：已按业务中文名称新建 Project；已切 `unlimited` 省积分；图片模型为 `generate_image_gpt_image_2_medium`；画幅为 APP/小程序 9:16 或网页 16:9；生成后已校正 Project 名称。
- [ ] Lovart thread：新业务首次出图已使用新 Project 的 `--project-id`，未复用旧 `thread-id`。
- [ ] Lovart 输出文件：`design/lovart/pages/` 中的 P0/P1 单页原型图，或写明未自动出图原因。
- [ ] Stitch 执行方式：自动调用 Stitch / 手动导入 Stitch / 不适用。
- [ ] Stitch 项目：已按业务中文名称新建或定位 Project；每个 P0/P1 页面只重建 1 个 screen。
- [ ] Stitch 输入：已使用当前页面 Lovart 单页图 + 页面完整提示词；未生成多变体、作品集图、交互图或独立状态图。
- [ ] Stitch 备份：screenId、HTML、截图已保存到 `design/stitch/` 或 `.stitch/`；无法执行时已写明原因。
- [ ] Stitch -> Figma：已向用户输出 Stitch Project、screenId、HTML/截图备份和 Copy/Paste 操作说明。
- [ ] 用户手动交接：用户已在 Stitch 页面 Copy/Paste 到目标 Figma 文件，并回传 Frame 链接/nodeId。
- [ ] Figma Frame：目标 Figma 文件、Page、Frame 链接/nodeId 已记录。
- [ ] 页面 Pxx：Figma Frame 已匹配。
- [ ] 页面 Pxx：实现前已重新读取 Figma Frame，并记录 nodeId、读取时间和关键视觉 token。
- [ ] 页面 Pxx：代码已实现。
- [ ] 页面 Pxx：布局、颜色、字号、间距、圆角、阴影、图标、Tab、安全区已对照；有截图证据或静态对照说明。

Figma 读取证据模板：

| 页面 | Figma Frame / nodeId | 读取时间 | 关键视觉 token / 图层要点 | 实现文件 | 对照方式 | 结论 |
| --- | --- | --- | --- | --- | --- | --- |
| Pxx |  |  | 尺寸、颜色、字号、间距、圆角、图标、图片 |  | 截图 / 静态对照 / 阻塞 | 待验收 |

### API 与数据库

- [ ] API 模块：接口路径、权限、参数、返回、错误码已定稿。
- [ ] DB 模块：表、字段、索引、种子数据已写入 `db/db.sql`。

### 联调与检查

- [ ] 前后端字段一致。
- [ ] 权限、空态、错误态、加载态闭环。
- [ ] 能运行的构建、测试、lint 或静态检查已执行；不能运行时已写明阻塞。

## 5. 缺陷与阻塞

| 编号 | 类型 | 内容 | 影响 | 处理人/责任层 | 状态 |
| --- | --- | --- | --- | --- | --- |
| D1 | 缺陷 / 阻塞 / 待确认 | 暂无 |  |  | 已关闭 |

## 6. 阶段完成门禁

进入下一阶段前必须检查：

- [ ] 当前阶段所有必做任务已完成或明确不适用。
- [ ] `index.html` 当前阶段、当前任务、进度、缺陷和阻塞与本文件一致。
- [ ] 阶段 2 完成时，`index.html` Lovart 翻页复制区已包含每个页面的完整单页提示词，且每张卡片都包含“全局设计系统 + 导航规则 + Pxx 页面完整提示词”；非阶段 2 时该区域不显示。
- [ ] 阶段 2 如使用 `lovart-skill`，已把生成方式、输出文件、Lovart project/thread（如有）和失败原因（如有）写入本文件；未配置 `lovart-skill` 时已保留手动复制提示词路径。
- [ ] 阶段 2 如使用 `lovart-skill`，已确认新建业务中文名 Project、`unlimited` 模式、模型 `generate_image_gpt_image_2_medium`、正确画幅和生成后项目名校正；不得复用旧业务 Lovart Project。
- [ ] 阶段 2 如使用 `lovart-skill`，新业务首次出图未复用旧 `thread-id`；同一页面微调重试才允许复用该页面 thread。
- [ ] 阶段 3 如使用 Stitch，已记录 Stitch Project、screenId、来源 Lovart 图、页面提示词、HTML/截图备份、用户手动 Copy/Paste 状态、目标文件和 Figma Frame 链接/nodeId；没有 Figma Frame 时不得进入第 4 步完成态。
- [ ] 第 4 步进入第 5 步前，所有 P0/P1 页面、接口、数据、资源和 UI 还原项已完成或明确不适用；每个已完成页面都有 Figma Frame 读取证据和对照结论。
- [ ] 不能运行的检查已写明阻塞原因。
