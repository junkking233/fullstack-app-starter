# 设计还原文档模板 UIDesignTemplate

本模板用于阶段 3，生成或更新 `docs/设计还原文档-UIDesign.md`。它合并 Stitch 输出、Figma Frame、视觉设计系统和页面设计拆解，避免业务项目中出现过多设计文档。

## 必须包含

1. 设计来源：PRD、Lovart Prompt、Lovart 备份稿、Stitch screen/htmlCode/designSystem/截图备份、Figma MCP 同步结果、Figma 链接。
2. Figma Frame 索引：页面编号、页面名、端、关联 P0/P1 功能、Frame、匹配来源、置信度、是否有效。
3. 视觉 token：颜色、字体、间距、圆角、阴影、图标风格。
4. 导航与安全区：Tab、返回、固定底部操作区、微信按钮规则。
5. 页面规格：每个有效页面单独拆解。
6. 逐页还原验收：布局、颜色、字号、间距、圆角、阴影、图标、Tab、安全区、状态。
7. 资源规则：共享图标库和各端副本路径。
8. 第 4 步读取要求：每个有效页面实现前必须重新读取对应 Figma Frame，读取证据写入 GoalPlan。

## 关键规则

- 只把 PRD ScopeBudget 中 P0/P1 页面标为有效。
- P2/暂缓页面、作品集总览、交互概览、独立弹层图、被裁切页面、底部 Tab 缺失页面只能标为仅参考或无效。
- 用户只提供 Figma 文件链接或 Page 根节点链接时，先读取 Figma metadata 自动匹配 Frame。
- 必须使用 Stitch，先记录 Project、screenId、来源 Lovart 图、htmlCode/designSystem/截图备份；再通过 Figma MCP 同步为可编辑 Figma Frame，并记录 fileKey、Page、Frame、nodeId、同步方式和可编辑节点检查结果，缺失则阻塞。
- 截图只用于视觉对照，不能作为同步到 Figma 的唯一设计对象；Figma Frame 内必须存在可编辑 Text、Rectangle、Frame、Component 等设计图层。
- 必须单独记录用户设计确认；Figma Frame 存在只代表交接完成，不能替代用户确认设计可进入开发。
- UIDesign 是实现索引，不是 Figma 原型替代品；第 4 步不能只凭 UIDesign 摘要写页面。
- 不要因为 Figma 中存在额外页面就扩大实现范围。
- Vant Weapp / Element Plus 默认样式必须按 Figma token 覆盖。

## 页面规格模板

### Pxx 页面名称

| 项 | 内容 |
| --- | --- |
| 页面路径 |  |
| Figma Frame |  |
| Figma nodeId |  |
| 第 4 步读取要求 | 实现前必须重新读取该 Frame；读取证据写入 GoalPlan |
| Frame 匹配来源 | Figma MCP 同步 / 用户直给 / metadata 自动匹配 / 待确认 |
| 匹配置信度 | 高 / 中 / 低 / 阻塞 |
| 备用 Lovart 稿 |  |
| Stitch screen / htmlCode |  |
| Stitch -> Figma | Figma MCP 同步 / 用户明确降级手动 Copy/Paste / 阻塞 |
| 可编辑节点检查 | Frame / Text / Rectangle / Component 等节点数量与检查方式 |
| 页面类型 | 一级 Tab / 二级功能 / 详情 / 表单 |
| 范围状态 | P0/P1 有效 / P2 暂缓 / 仅参考 / 待确认 |
| 页面目标 |  |
| 顶部区域 |  |
| 内容结构 |  |
| 底部区域 | Tab / 固定按钮 / 无 |
| 关键状态 | 空态、加载、错误、成功、弹层、筛选、输入态 |
| 使用组件 | Vant Weapp / Element Plus / 自定义组件 |
| 样式覆盖 |  |
| 素材使用说明 |  |
| 接口需求 |  |
