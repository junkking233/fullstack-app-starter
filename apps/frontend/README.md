# 网页前端

Vue 3 网页前端。脚手架只保留首页、登录注册和后台用户管理这套最小可运行示例；真实业务项目必须按 PRD ScopeBudget 和 Figma 设计稿继续扩展。

## 技术栈

Vue 3、Vite、TypeScript、Element Plus、Vue Router、Axios、ECharts。

## 常用命令

```bash
cd apps/frontend
npm install
npm run start
npm run build
```

## 页面和代码位置

| 内容 | 位置 |
| --- | --- |
| 路由 | `src/router/index.ts` |
| 接口请求 | `src/api/` |
| 登录态工具 | `src/utils/auth.ts` |
| 首页示例 | `src/views/portal/Home.vue` |
| 登录注册 | `src/views/common/` |
| 用户管理示例 | `src/views/admin/ManagementView.vue` |
| 公共组件 | `src/components/` |
| 全局样式 | `src/styles.css` |

## 示例路由

以下是脚手架保留的最小示例路由，业务项目应按 PRD 和 UIDesign 扩展或替换。

| 页面 | 路由 |
| --- | --- |
| 首页 | `/portal/home` |
| 登录页 | `/login` |
| 注册页 | `/register` |
| 用户管理 | `/admin/users` |

## 代理配置

前端代理在 `vite.config.ts` 中配置：

- `/api/*` 转发到后端（`http://localhost:8888`）

Docker 部署时，代理地址通过环境变量 `VITE_PROXY_BACKEND` 覆盖。

## 图标使用

项目图标库在 `assets/icons/` 下（84 个 SVG 线性图标）。开发页面前必须先检查共享图标库，已有图标时按需复制到 `src/assets/icons/`；缺少图标时先把新增 SVG 放入 `assets/icons/`，再复制到本端使用。

图标复制示例：

```bash
cp ../../assets/icons/search.svg src/assets/icons/search.svg
```

详细规则见根目录 `assets/README.md`。图标预览可打开 `assets/icon-set.html`。

## 注意事项

- 使用 Element Plus 组件实现交互，避免手写 CSS 控制样式。
- Element Plus 只是组件基础，页面颜色、字号、圆角、间距、阴影、菜单、表单、图片和空状态必须按 Figma Frame 和根目录 `docs/设计还原文档-UIDesign.md` 还原。
- 当前页面只作为脚手架最小参考；不属于 P0/P1 的页面必须删除、暂缓或标为仅参考。
- 每完成一个页面，先用浏览器截图或人工对照 Figma 检查设计稿；无法访问 Figma 或截图时，在 GoalPlan 写明阻塞并完成静态样式对照。
- 禁止使用紫色作为主色。
- 门户菜单使用水平模式；后台管理可使用侧栏垂直菜单并允许内嵌子菜单。
- 下拉框宽度要根据内容合理设置，避免选项显示不全。
- 避免用 `!important` 覆盖 `.el-button--primary` 背景；确需覆盖时排除 `.is-link` 和 `.is-text`。
