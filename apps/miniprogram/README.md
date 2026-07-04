# 原生微信小程序端

基于原生微信小程序语法开发，使用 Vant Weapp 组件库。微信开发者工具可以直接导入本目录。

## 技术栈

- 原生微信小程序：WXML、WXSS、JavaScript、JSON
- Vant Weapp：`@vant/weapp`
- npm 包管理：使用国内镜像安装依赖

## 常用命令

```bash
cd apps/miniprogram
npm install
```

安装后在微信开发者工具中执行“工具 -> 构建 npm”，再预览或调试。

## 目录结构

| 目录/文件 | 说明 |
| --- | --- |
| `project.config.json` | 微信开发者工具项目配置，AppID 固定为 `wxd84d204ed36b05b5`。 |
| `app.json` | 页面、窗口、组件和 sitemap 配置入口。 |
| `app.js` | 小程序全局入口。 |
| `app.wxss` | 全局样式。 |
| `pages/` | 页面目录，每个页面包含 `.wxml`、`.wxss`、`.js`、`.json`。 |
| `api/` | 接口请求封装。 |
| `assets/icons/` | 小程序本地 SVG 图标副本。 |
| `utils/` | 登录态等工具函数。 |

## 接口配置

默认接口地址在 `api/request.js`：

```js
const BASE_URL = 'http://localhost:8888/api';
```

微信开发者工具调试本地接口时，可在详情里关闭“不校验合法域名”。真机或正式环境需要替换为合法 HTTPS 域名。

## 图标使用

项目共享图标库在根目录 `assets/icons/`。开发小程序页面前必须先检查共享图标库，已有图标时按需复制到 `apps/miniprogram/assets/icons/`；缺少图标时先把新增 SVG 放入根目录 `assets/icons/`，再复制到小程序端使用。

图标复制示例：

```bash
cp ../../assets/icons/search.svg assets/icons/search.svg
```

页面中引用复制后的图标：

```xml
<image class="icon" src="/assets/icons/search.svg" mode="aspectFit" />
```

详细规则见根目录 `assets/README.md`。

## 开发约束

- 普通页面操作按钮优先使用 `view` / `text` 模拟按钮并绑定 `bindtap`。
- 原生 `button` 只用于授权、分享、获取手机号、表单提交等微信原生能力。
- 使用底部固定操作栏时必须适配安全区，并为页面内容预留底部空间。
- 页面样式使用 `rpx`，主色不要使用紫色。
- Vant Weapp 组件只能作为结构基础，页面颜色、字号、圆角、间距、阴影、底部 Tab、图片和空状态必须按 Figma Frame 和根目录 `docs/设计还原文档-UIDesign.md` 还原。
- 每完成一个页面，先在微信开发者工具或可用截图方式中对照 Figma 设计稿；无法访问 Figma 或截图时，在 GoalPlan 写明阻塞并完成静态样式对照。
- 构建产物 `miniprogram_npm/` 和个人配置 `project.private.config.json` 不提交到 Git。
