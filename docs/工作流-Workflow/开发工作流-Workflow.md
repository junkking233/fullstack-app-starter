# 开发工作流 Workflow

目标：让无上下文 Agent 能快速判断当前阶段、读取正确文档、执行下一步。

## 先读顺序

1. `README.md`
2. `docs/工作流-Workflow/开发规范-AGENTS.md`
3. `docs/工作流-Workflow/开发工作流-Workflow.md`
4. `docs/工作流-Workflow/工作流状态-WorkflowState.md`
5. `index.html`
6. `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`（如存在）
7. `docs/原型设计-Design/视觉设计系统-DESIGN.md`（如存在）
8. 当前任务相关文档

## 阶段状态机

| 阶段 | 名称 | 产物 | 下一步 |
| --- | --- | --- | --- |
| 0 | 脚手架准备 | 项目结构、规范、状态文件 | 1 |
| 1 | 产品需求分析 PRD | `docs/产品需求-PRD/产品需求文档-PRD.md` | 2 |
| 2 | 原型生图提示词 Lovart Prompt | `docs/原型设计-Design/原型生图提示词-LovartPrompt.md` | 3 |
| 3 | Figma 设计稿拆解 UI Design | `docs/原型设计-Design/视觉设计系统-DESIGN.md`、`docs/原型设计-Design/页面设计文档-UIDesign.md`、`design/lovart/`，并在 UI Design/GoalPlan 中记录 Figma Frame 链接 | 4 |
| 4 | API 设计 | `docs/技术设计-TechDesign/API接口文档-API.md` | 5 |
| 5 | 数据库设计 | `db/db.sql` | 6 |
| 6 | Goal 长程计划 | `docs/工作流-Workflow/Goal长程计划-GoalPlan.md` | 7 |
| 7 | 功能实现 | `apps/`、`db/` | 8 |
| 8 | 对抗式代码审查 | 问题清单 | 9 |
| 9 | 回归验收与修复 | 验收结论、修复提交 | 完成 |

## 状态更新规则

每完成阶段或局部任务，必须同步更新三处：

| 文件 | 必须同步的内容 |
| --- | --- |
| `docs/工作流-Workflow/工作流状态-WorkflowState.md` | 当前阶段、当前任务、最近完成、下一步、阻塞项、证据路径 |
| `index.html` | `data-current-stage`、顶部徽章、当前状态卡片、阶段状态表 |
| `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`（如存在） | 执行记录、风险、验收矩阵、阶段状态 |

中断恢复时，先读状态文件、`index.html` 和 Goal 计划，不要从头猜。

## 模板清理规则

- `*Template.md` 只用于生成正式文档，不是业务项目交付物。
- 脚手架仓库必须保留模板；业务项目生成正式文档并通过对应阶段门禁后，必须删除已用过的模板文件。
- 阶段证据只认正式产物，不认模板文件。例如阶段 2 看 `LovartPrompt.md`，阶段 3 看 `DESIGN.md` 和 `UIDesign.md`，阶段 6 看 `GoalPlan.md`。
- 如果后续需要重新生成，可从脚手架仓库、飞书工作流或 Skills 重新取模板。

## 阶段推进门禁

- 只有当前阶段的产物、证据和验收项全部完成后，才能把状态推进到下一阶段。
- 不能因为“已完成一部分代码”“已生成部分页面”或“准备审查”就提前修改当前阶段。
- 如果存在 `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`，阶段推进必须先检查 GoalPlan：实现拆解、验收矩阵、风险和执行记录必须同步。
- 未完成项必须留在当前阶段，并在 WorkflowState、index.html 和 GoalPlan 写清下一步；阻塞项写清原因和需要用户提供的信息。
- 阶段 7 进入阶段 8 前，必须通过“Goal 完成门禁”：GoalPlan 中所有必做模块、页面、接口、数据、状态和资源项都已完成或明确标为不适用。

## 阶段要求

### 0. 脚手架准备

- 克隆 `https://github.com/junkking233/fullstack-app-starter.git`。
- `fullstack-app-starter` 只代表脚手架仓库名，不是业务项目名；业务项目必须改成真实项目名称。
- 克隆后必须删除脚手架 `.git`，避免业务代码继续关联脚手架仓库。
- 需要版本控制时，在业务项目目录重新 `git init`，并绑定新的业务仓库 remote。
- 不要向脚手架仓库提交或推送业务项目代码。
- 按需求裁剪 Web、小程序、AI 助手服务和 Compose 配置。
- 不自动启动服务，不重置数据库。

### 1. PRD 需求分析

- 用第一性原理确认用户、问题、角色、对象、流程、边界。
- 只更新 PRD，不进入接口、数据库或代码实现。
- 页面视觉只定义内容结构和状态，交给 Lovart 生成。

### 2. Lovart Prompt 原型生图提示词

- 基于 PRD 和 `docs/原型设计-Design/原型生图提示词模板-LovartPromptTemplate.md`（如存在）生成 Lovart 可复制提示词。
- 先生成 `ScreenInventory` 页面清单，再生成 `DesignSystemPrompt`、`NavigationContract`、单页开发稿提示词和验收清单。
- 页面数量由 PRD 的角色、流程、Tab 和二级功能决定，不固定为 4 个页面。
- 开发稿必须单页生成：一个提示词只生成 1 张完整 iPhone 竖屏页面。
- 不生成作品集总览图、交互概览图、独立状态稿或独立弹层稿；弹窗、底部弹层、错误态、空态和成功提示只写入对应页面提示词的必要状态备注。
- 每个页面必须标注页面类型：一级 Tab 页面、二级功能页面、详情页面或表单页面，并写清是否显示底部 Tab。
- 当前阶段只写提示词，不写代码，不生成图片。
- Lovart 出图后，由用户或设计工具沉淀为 Figma 设计文件；后续代码还原以具体 Figma 页面 Frame 为主依据。

### 3. UI Design 设计稿拆解

- 读取 Figma 设计文件、Page 根节点或用户提供的具体页面 Frame 链接；Lovart PNG/PSD 仅作为生成来源和备份参考。
- 对照 PRD、Lovart Prompt 和 `docs/原型设计-Design/页面设计文档模板-UIDesignTemplate.md`（如存在），生成 UI Design 文档。
- 先用 Lovart Prompt 中的 `ScreenInventory` 检查页面是否出齐，必要状态备注是否记录。
- 如果用户只提供 Figma 文件链接或 Page 根节点链接，先读取 Figma metadata，用页面编号、页面中文名和 Frame 名称自动匹配页面 Frame；把匹配结果写入 UI Design/GoalPlan。只有同一页面有多个候选、找不到候选或命名明显冲突时，才向用户索要具体 Frame 链接。
- 匹配页面 Frame 时，不要遍历整个文件的所有图层；优先读取当前 Page 的第一层 Frame 树，按 `P01`、`01`、页面名、近似名称进行匹配，避免浪费 Figma MCP 额度。
- 先基于 Figma Frame 的图层、样式、尺寸和 `docs/原型设计-Design/视觉设计系统模板-DESIGNTemplate.md`（如存在）生成 `docs/原型设计-Design/视觉设计系统-DESIGN.md`，沉淀颜色、字体、间距、圆角、阴影和组件 token。
- 拆解页面路径、Figma nodeId、布局、视觉 token、组件、状态、交互、接口需求、资源需求和还原验收标准。
- 每个页面必须写清对应 Figma Frame 链接、备用 Lovart PSD/PNG、所需资源、底部 Tab/返回栏规则、固定底部操作区、安全区、Vant/Element 默认样式覆盖点和截图对比要求。
- 如果可用，运行 `npx @google/design.md lint docs/原型设计-Design/视觉设计系统-DESIGN.md`；无法运行时写明原因，不阻塞手工拆解。
- Figma 页面 Frame 是视觉还原主依据；Lovart PSD/PNG 仅作备份参考。如误生成作品集总览、交互概览、独立状态稿或独立弹层稿，只作为辅助参考，不用于代码还原。

### 4. API 设计

- 建立“角色 x 模块 x 操作”矩阵。
- 每个接口写清路径、方法、权限、参数、返回、错误码和业务说明。
- 统一响应结构：`code`、`message`、`data`、`timestamp`。

### 5. 数据库设计

- 根据 API 抽取实体、关系、状态、索引和种子数据。
- 更新 `db/db.sql`。
- 不创建 MySQL 容器，不重置已有数据库。

### 6. Goal 长程计划

- 基于 `docs/工作流-Workflow/Goal长程计划模板-GoalPlanTemplate.md`（如存在）生成 Goal 计划。
- 计划必须覆盖需求、设计稿、API、数据库、实现拆解、验收、风险和执行记录。

### 7. 功能实现

- 实现前必须读取 PRD、DESIGN、UI Design、API、DB 和 GoalPlan；如果是长程任务且 GoalPlan 不存在，先生成 GoalPlan。
- 后端严格按 API 文档实现。
- Web 使用 Vue + Vite + Element Plus。
- 小程序使用原生微信语法 + Vant Weapp。
- 页面必须对照 Figma Frame、DESIGN 和 UI Design 还原；不能只套 Vant/Element 默认样式后标为完成。
- 前端和小程序页面按“Figma Frame 定位 -> 读取设计上下文 -> 实现页面 -> 截图/人工对照 -> 修复偏差 -> 记录证据”的循环逐页推进。
- 每个页面完成时，必须在 GoalPlan 写入页面级 UI 还原记录；布局、颜色、字号、间距、圆角、阴影、图标、底部 Tab、安全区或关键状态仍明显偏离 Figma 时，该页面保持待验收或待完成。
- 无法访问 Figma、运行微信开发者工具或浏览器截图时，必须写明阻塞原因，并至少完成静态对照：文件路径、样式 token、资源引用、底部安全区和 Vant/Element 覆盖点。
- 每完成模块，更新状态文件和 Goal 执行记录，但不自动推进到阶段 8。
- 进入阶段 8 前必须逐项核对 GoalPlan 的实现拆解和验收矩阵；有任何必做项仍为待开始、进行中、待验收、待完成、阻塞或空白，就继续停留在阶段 7。
- 无法运行服务或测试时，可以记录静态检查结果和阻塞原因，但不能把未验证的必做项标为完成。

### 8. 对抗式代码审查

- 审查开始前先确认阶段 7 已通过 Goal 完成门禁；如果未通过，退回阶段 7 补齐实现。
- 主动查需求遗漏、设计稿还原不足、接口漂移、数据库缺口、权限问题、交互缺口和测试盲区。
- 对抗式审查只做最终兜底；如果阶段 7 缺少逐页 UI 还原记录，应退回功能实现，不在审查阶段重新补整套页面。
- 输出问题、证据、影响、严重程度、责任 skill 和修复建议。

### 9. 回归验收与修复

- 不自动启动服务；服务未启动时只做静态验收并标记阻塞。
- 修复只改相关文件，不做无关重构。

## 常用入口

| 任务 | 做法 |
| --- | --- |
| 整套系统 | 0-6 后实现，再审查验收 |
| 单功能 | 先读状态，补缺失阶段，再实现 |
| 只做后端 | 确认 API 和数据库足够 |
| 只做前端/小程序 | 确认 UI Design 和 API 足够 |
| 收集问题 | 只定位归类，不修代码 |
| 修复问题 | 按责任层修复并回归 |
