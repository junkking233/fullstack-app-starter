# uni-app 跨平台端

基于 uni-app 的跨平台子应用，使用 Vue 3 + TypeScript + uni-ui，可编译到微信小程序、H5、Android 和 iOS。

> **可选模块**：与 `apps/wechat-miniprogram/` 并存，后期可逐步替代原生小程序开发。

## 技术栈

Vue 3、Vite、TypeScript、uni-app、uni-ui。

## 编译目标

| 平台 | 命令 |
| --- | --- |
| 微信小程序（开发） | `npm run dev:mp-weixin` |
| 微信小程序（构建） | `npm run build:mp-weixin` |
| H5（开发） | `npm run dev:h5` |
| H5（构建） | `npm run build:h5` |
| App（开发） | `npm run dev:app` |
| App（构建） | `npm run build:app` |

## 常用命令

```bash
cd apps/uniapp
npm install
npm run dev:h5            # H5 开发模式，浏览器访问 http://localhost:5173
npm run dev:mp-weixin      # 微信小程序开发模式，用微信开发者工具打开 dist/dev/mp-weixin
```

## 目录结构

| 目录/文件 | 说明 |
| --- | --- |
| `src/pages/` | 页面文件，每个页面一个文件夹。 |
| `src/static/` | 静态资源（图片、图标）。 |
| `src/api/` | 接口请求封装。 |
| `src/utils/` | 工具函数。 |
| `src/pages.json` | 页面路由和 tabBar 配置。 |
| `src/manifest.json` | 应用配置（AppID、平台设置）。 |
| `src/App.vue` | 应用入口。 |
| `src/main.ts` | 入口脚本。 |
| `src/uni.scss` | 全局样式变量。 |

## 微信小程序编译说明

编译后的小程序代码在 `dist/dev/mp-weixin`（开发）或 `dist/build/mp-weixin`（生产）目录中。

使用微信开发者工具导入该目录即可预览和调试。

## 接口配置

- H5 开发模式：`manifest.json` 中的 `h5.devServer.proxy` 配置了 `/api` 代理到后端。
- 微信小程序：需要在 `manifest.json` 的 `mp-weixin` 中配置合法域名，或在微信开发者工具中关闭域名校验。

## 图标使用

项目图标库在 `apps/assests/icons/` 下（84 个 SVG 线性图标）。使用时按需复制需要的 SVG 到 `src/static/icons/`，通过 `<image>` 标签引用。图标预览可打开 `apps/assests/icon-set.html`。

## 注意事项

- 使用 Vue 3 组合式 API（`<script setup>`）编写页面。
- UI 组件使用 uni-ui，通过 easycom 自动引入，无需手动 import。
- 跨平台兼容：避免使用平台特有 API，优先使用 `uni.*` 统一接口。
- 页面样式使用 `rpx` 单位，自动适配不同屏幕尺寸。
