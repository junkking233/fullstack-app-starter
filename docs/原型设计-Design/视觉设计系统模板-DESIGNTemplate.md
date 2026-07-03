# 视觉设计系统模板 DESIGNTemplate

使用方式：阶段 3 基于 Figma 页面 Frame 生成 `docs/原型设计-Design/视觉设计系统-DESIGN.md`。该文件遵循 Google `DESIGN.md` 思路：YAML token 给 Agent 精确数值，Markdown 说明告诉 Agent 如何应用。Lovart PSD/PNG 只作为备份参考。

生成 `docs/原型设计-Design/视觉设计系统-DESIGN.md` 时，删除本说明，只保留下方结构。

```markdown
---
version: "alpha"
name: "{项目名称}"
description: "{一句话描述产品视觉风格}"
colors:
  primary: "#{主色}"
  secondary: "#{辅助色}"
  background: "#{页面背景色}"
  surface: "#{卡片/容器色}"
  textPrimary: "#{主文字色}"
  textSecondary: "#{次文字色}"
  border: "#{边框色}"
  success: "#{成功色}"
  warning: "#{警告色}"
  danger: "#{错误色}"
typography:
  page-title:
    fontFamily: "system-ui"
    fontSize: "20px"
    fontWeight: "700"
    lineHeight: "28px"
  section-title:
    fontFamily: "system-ui"
    fontSize: "16px"
    fontWeight: "600"
    lineHeight: "22px"
  body:
    fontFamily: "system-ui"
    fontSize: "14px"
    fontWeight: "400"
    lineHeight: "20px"
  caption:
    fontFamily: "system-ui"
    fontSize: "12px"
    fontWeight: "400"
    lineHeight: "17px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "12px"
  lg: "16px"
  xl: "24px"
rounded:
  sm: "4px"
  md: "8px"
  lg: "12px"
components:
  page:
    backgroundColor: "{colors.background}"
    padding: "{spacing.lg}"
  card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    padding: "{spacing.lg}"
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "#ffffff"
    rounded: "{rounded.md}"
    height: "44px"
  tabbar:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.textSecondary}"
    activeColor: "{colors.primary}"
    height: "56px"
---

## Overview

{说明整体视觉定位，例如校园服务、可信、高效、清爽。}

## Colors

- Primary：{使用场景，例如主按钮、选中态、重点金额。}
- Secondary：{使用场景。}
- Background：{页面底色规则。}
- Surface：{卡片、表单、弹层规则。}
- Text：{主次文字和弱提示规则。}

## Typography

标题、正文、辅助文字必须使用上方 token，不允许每个页面自由设字号。DESIGN token 使用 px 方便 lint；小程序实现时在 UI Design 中换算为 rpx。

## Layout

- 页面左右边距：{例如 16px / 32rpx}。
- 卡片间距：{例如 12px / 24rpx}。
- 列表项高度：{例如 56px / 112rpx}。
- 固定底部操作区必须保留 safe area。

## Elevation & Depth

{写清卡片阴影、边框、弹层遮罩和层级规则。}

## Shapes

{写清卡片、按钮、输入框、标签、头像等圆角规则。}

## Components

- Button：{高度、圆角、颜色、禁用态。}
- Card：{背景、边框、阴影、内边距。}
- Field：{输入框高度、提示文字、边框。}
- Tabbar：{高度、图标、文字、选中态、安全区。}
- Popup：{圆角、遮罩、标题、按钮。}

## Do's and Don'ts

- Do：严格复用 token，先按 Figma 对齐主页面，再复用到其他页面。
- Do：优先从 Figma Frame 提取颜色、字号、间距、圆角、阴影、图标尺寸和组件状态。
- Do：Vant Weapp/Element Plus 默认样式必须按 token 覆盖。
- Don't：不要使用未在 token 中出现的主色、字号、圆角和按钮样式。
- Don't：不要用组件库默认视觉替代 Figma。
```

可用时执行：

```bash
npx @google/design.md lint docs/原型设计-Design/视觉设计系统-DESIGN.md
```
