# 项目开发规范

本文件只放 AI 开发必须遵守的硬约束。项目说明看 `README.md`，阶段流程看 `docs/工作流-Workflow/开发工作流-Workflow.md`，当前状态看 `docs/工作流-Workflow/工作流状态-WorkflowState.md`。

## 进入项目先读

每次开发前必须读取：

1. `README.md`
2. `docs/工作流-Workflow/开发规范-AGENTS.md`
3. `docs/工作流-Workflow/开发工作流-Workflow.md`
4. `docs/工作流-Workflow/工作流状态-WorkflowState.md`
5. `index.html`
6. `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`（如存在）
7. `docs/原型设计-Design/视觉设计系统-DESIGN.md`（如存在）
8. 当前任务相关文档，例如 `docs/产品需求-PRD/产品需求文档-PRD.md`、`docs/技术设计-TechDesign/API接口文档-API.md`、`docs/原型设计-Design/页面设计文档-UIDesign.md`

没有上下文时，以 `docs/工作流-Workflow/工作流状态-WorkflowState.md` 判断下一步，不要猜。

## 项目约束

- `fullstack-app-starter` 只代表脚手架仓库名，不是业务项目名。
- 克隆脚手架开始业务开发后，必须先删除脚手架 `.git`，再按真实业务项目重新 `git init` 并绑定新的业务 remote。
- 不得把业务项目提交或推送到脚手架仓库；文档中的项目名称必须使用真实业务项目名。
- 后端端口：`8888`；前端端口：`9999`；AI 助手服务端口：`8000`。
- Docker Compose 固定在 `apps/docker-compose.yml`。
- 不创建 MySQL 容器，统一连接已有 `mysql-docker`。
- 不使用 Nginx 容器处理网页请求。
- MySQL：`root/admin123`，连接参数包含 `characterEncoding=UTF-8` 和 `allowPublicKeyRetrieval=true`。
- 终端连接 MySQL 使用：`mysql -u root -padmin123 --default-character-set=utf8mb4`。
- 本机 MySQL 位于 `/usr/local/mysql/bin`，不要用 brew 验证。
- 管理员密码：`admin123`；普通用户密码：`123456`；密码使用 MD5，不加 salt。

## 开发流程

固定顺序：

1. 产品需求分析（PRD）
2. 原型生图提示词（Lovart Prompt）
3. Figma 设计稿拆解（UI Design）
4. API 设计
5. 数据库设计
6. Goal 长程计划
7. 功能实现
8. 对抗式代码审查
9. 回归验收与修复

长程任务或整套系统开发时，必须先基于 `docs/工作流-Workflow/Goal长程计划模板-GoalPlanTemplate.md`（如存在）生成 `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`，不能只写普通 TODO。

每完成一个阶段或局部任务，必须同步更新 `docs/工作流-Workflow/工作流状态-WorkflowState.md` 和根目录 `index.html`；如果存在 `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`，也必须同步更新。

阶段推进必须有门禁：

- 当前阶段产物、证据和验收项没有全部完成时，不得把状态推进到下一阶段。
- 阶段 7 功能实现完成前，必须读取 GoalPlan 并逐项检查实现拆解、Goal 完成门禁和验收矩阵。
- GoalPlan 中任何必做项仍为待开始、进行中、待验收、待完成、阻塞或空白时，当前阶段仍是功能实现，不能进入对抗式审查。
- 不能运行服务或测试时，必须把影响写为阻塞或风险；不能把未验证的必做项标为已完成。
- 阶段同步三件套只能反映真实进度，不能为了进入下一步而提前改阶段。

## 模板清理

- `*Template.md` 只用于生成正式文档，不是业务项目交付物。
- 脚手架仓库必须保留模板；业务项目生成正式文档并通过对应阶段门禁后，必须删除已用过的模板文件。
- 阶段证据只认正式产物：`LovartPrompt.md`、`DESIGN.md`、`UIDesign.md`、`GoalPlan.md` 等。
- 如果模板已被删除，不得因此判定阶段缺失；需要重生成时，从脚手架、飞书工作流或 Skills 获取模板结构。

## 设计稿与资源

- 原型生图提示词输出到 `docs/原型设计-Design/原型生图提示词-LovartPrompt.md`。
- 原型生图提示词优先参考 `docs/原型设计-Design/原型生图提示词模板-LovartPromptTemplate.md`（如存在），先产出页面清单、全局设计系统和导航规则，再产出单页开发稿提示词。
- Lovart 只生成单页开发稿，不生成作品集总览图、交互概览图、独立状态稿或独立弹层稿；弹窗、底部弹层和错误态只写在页面备注中。
- Lovart 原型图、PSD、PNG 默认放到 `design/lovart/`，并由用户或设计工具沉淀为 Figma 文件。
- Figma 文件与页面 Frame 链接记录到 `docs/原型设计-Design/页面设计文档-UIDesign.md`；长程任务同步记录到 `docs/工作流-Workflow/Goal长程计划-GoalPlan.md`。代码还原优先使用具体页面 Frame 链接，不优先读取整个 Figma 文件。
- 如果用户只给 Figma 文件链接或 Page 根节点链接，必须先通过 Figma metadata 按页面编号、页面中文名和 Frame 名称自动匹配页面 Frame；只有重名、缺失或命名冲突时才要求用户补充具体 Frame 链接。
- UI Design 阶段必须基于 Figma Frame 和 `docs/原型设计-Design/视觉设计系统模板-DESIGNTemplate.md`（如存在）生成 `docs/原型设计-Design/视觉设计系统-DESIGN.md`，用于记录全局颜色、字体、间距、圆角、阴影和组件 token。
- 如环境允许，可运行 `npx @google/design.md lint docs/原型设计-Design/视觉设计系统-DESIGN.md` 检查设计系统格式；不能运行时写入阻塞或风险。
- 页面设计拆解优先参考 `docs/原型设计-Design/页面设计文档模板-UIDesignTemplate.md`（如存在），输出到 `docs/原型设计-Design/页面设计文档-UIDesign.md`。
- 页面实现必须以 Figma Frame、DESIGN 和 UI Design 为依据，不要跳过设计稿自由发挥；Lovart PSD/PNG 只作为备份参考。
- 前端和小程序实现阶段必须逐页做 UI 还原循环：定位 Figma Frame、读取设计上下文、实现页面、截图或人工对照、修复偏差、记录证据。
- 没有完成 UI 还原记录的页面不能在 GoalPlan 或验收矩阵中标为已完成。
- 通用图标先检查 `assets/icons/`。
- Web 图标副本放到 `apps/frontend/src/assets/icons/`。
- 小程序图标副本放到 `apps/miniprogram/assets/icons/`。
- 缺少图标时先补共享资源库，再复制到子应用。

## 后端规范

- Java 项目禁止 Lombok。
- Spring Boot `3.3.4`，JDK `21`，MyBatis Plus `3.5.7`，MySQL 驱动 `8.0.26`。
- Spring Boot 配置需要支持 Maven 热加载。
- 统一响应结构：`code`、`message`、`data`、`timestamp`。
- Controller 只处理 HTTP 入参、出参和权限上下文，业务逻辑放 Service。
- 数据库结构变化后同步更新 `db/db.sql`。

## Web 前端规范

- 使用 Vue + Vite + npm，必须包含 `index.html`，禁止 Vue CLI。
- UI 框架使用 Element UI/Element Plus，优先使用组件能力。
- 禁止紫色主色。
- 门户菜单用水平模式；后台管理可用侧栏垂直菜单。
- 下拉框宽度按内容设置，避免显示不全。
- 避免用 `!important` 覆盖 `.el-button--primary`；确需覆盖时排除 `.is-link` 和 `.is-text`。
- npm、nodejs、pip 使用国内镜像地址。

推荐脚本语义：

```json
{
  "scripts": {
    "install:dev": "npm install && npm run start",
    "start": "vite --port 9999",
    "dev": "npm run install:dev",
    "build": "vite build"
  }
}
```

## 微信小程序规范

- 小程序目录固定为 `apps/miniprogram/`，微信开发者工具直接导入该目录。
- 使用原生 WXML、WXSS、JavaScript、JSON，不使用 uni-app。
- UI 组件使用 Vant Weapp，包名 `@vant/weapp`。
- Vant Weapp 只是组件基础，不能用默认样式或默认图标替代 Figma；字段、按钮、弹层、Tab、卡片、图片和空状态必须按 Figma、DESIGN 和 UI Design 覆盖间距、字号、圆角、颜色、阴影和资源。
- AppID 固定为 `wxd84d204ed36b05b5`。
- 样式使用 `rpx`。
- 普通操作按钮优先用 `view` / `text` + `bindtap`。
- 原生 `button` 只用于 `open-type`、授权、分享、手机号、表单提交等微信原生能力。
- 自定义原生 `button` 必须重置 margin、padding、border、line-height、text-align、background、border-radius，并设置 `button::after { border: 0; }`。
- 底部固定操作栏使用 `env(safe-area-inset-bottom)`，页面内容区预留底部空间。
- 不提交 `apps/miniprogram/miniprogram_npm/` 和 `project.private.config.json`。

## 测试、Git 与文档

- 调试时不要自动启动服务，端口由开发者手动控制。
- 数据库操作用 mysql 命令，完成后立即 `mysqldump database_name > db/db.sql`，不保留临时 SQL 文件。
- 页面测试使用 chrome-devtools 或可用浏览器调试工具。
- 保留 `.gitignore`，忽略依赖、构建产物、IDE 配置、日志、环境变量和运行数据。
- 开发代码后不要额外生成总结文档，除非用户明确要求。
