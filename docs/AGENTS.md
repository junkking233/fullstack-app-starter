# 项目开发规范

本文件只维护开发规范和约束。项目启动、模块介绍、目录说明请看根目录 `README.md`。

## 通用约定

### 端口配置

- 后端 API 端口固定为 `8888`。
- 前端服务端口固定为 `9999`。
- AI 服务端口固定为 `8000`。

### Docker 与数据库

- 默认使用 `apps/docker-compose.yml` 管理项目服务。
- 不要在本项目 Compose 中创建 MySQL 容器；统一连接已有 `mysql-docker`。
- 创建数据库前必须检查库名是否已存在，不得重置已有数据库。
- 不要使用 Nginx 容器处理网页请求。
- MySQL 用户名密码为 `root/admin123`。
- MySQL 连接参数包含 `characterEncoding=UTF-8` 和 `allowPublicKeyRetrieval=true`。
- 终端连接 MySQL 时指定字符集：`mysql -u root -padmin123 --default-character-set=utf8mb4`。
- 本机 MySQL 位于 `/usr/local/mysql/bin`，不要使用 brew 验证 MySQL。

### 账号密码

- 管理员密码：`admin123`。
- 普通用户密码：`123456`。
- 密码使用 MD5 加密，不使用 salt。

### 图标库

- 项目在 `assets/icons/` 下维护了一套通用线性图标库（84 个 SVG），使用 `stroke="currentColor"`，可通过 CSS color 控制颜色。
- 开发 Web 前端或微信小程序页面时，必须先检查 `assets/icons/` 是否已有所需图标。
- 已有图标时，按需将用到的 SVG 复制到子应用目录中，不要一次性全部复制。
- 缺少图标时，必须先把新增 SVG 放入 `assets/icons/`，再复制到对应子应用中使用。
- Web 前端图标副本放在 `apps/frontend/src/assets/icons/`。
- 微信小程序图标副本放在 `apps/wechat-miniprogram/miniprogram/assets/icons/`，页面中可使用 `/assets/icons/<name>.svg` 引用。
- 图标列表和预览可打开 `assets/icon-set.html` 查看。
- 图标新增和复制规则详见 `assets/README.md`。
- 常用图标：`home`、`user`、`search`、`settings`、`bell`、`cart`、`mail`、`edit`、`trash`、`plus`、`close`、`check-circle`、`warning`、`error`、`info`。

## 长程任务与 Goal 规范

- 当用户要求“一次开发整个系统”、“使用 goal 开发”、“长程任务”或类似目标时，必须先基于 `docs/GOAL_PLAN_TEMPLATE.md` 生成或更新 `docs/GOAL_PLAN.md`。
- `docs/GOAL_PLAN.md` 必须把目标拆成阶段、模块、页面、接口、数据库、验收标准、风险和阻塞条件，不得只写简略待办。
- 开发顺序固定为：产品需求分析（第一性原理） -> 原型与页面设计检查 -> API 设计 -> 数据库设计 -> Goal 计划定稿 -> 实现 -> 对抗式代码审查 -> 回归验收。
- 需求分析阶段要明确用户角色、业务对象、核心流程、系统边界和待确认问题；不确定内容必须标注为“待确认”。
- 原型与页面设计阶段要准备必要的页面设计说明，必要时可使用 `npx @google/design.md` 生成页面设计文档。
- 实现阶段必须按 `docs/GOAL_PLAN.md` 推进，每完成一个阶段更新计划状态和发现的问题。
- 对抗式代码审查阶段要主动检查需求遗漏、接口契约不一致、数据库支撑不足、权限问题、页面交互缺口和测试盲区。
- 长程任务过程中仍必须遵守本文件的端口、数据库、Docker、前端、小程序、测试和 Git 规范。

## 后端规范

- Java 项目禁止使用 Lombok。
- Spring Boot 版本固定为 `3.3.4`。
- Java JDK 版本固定为 `21`。
- MyBatis Plus 版本固定为 `3.5.7`。
- MySQL Maven 依赖版本固定为 `8.0.26`。
- Spring Boot 配置需要支持 Maven 热加载。
- 统一响应结构包含 `code`、`message`、`data`、`timestamp`。
- Controller 只处理 HTTP 入参、出参和权限上下文，业务逻辑放到 Service。
- 数据库表结构更新后，需要同步更新 `db/db.sql`。

## 前端规范

- 使用 Vue + Vite + npm，必须包含 `index.html`，禁止使用 Vue CLI。
- UI 框架使用 Element UI/Element Plus，优先用组件实现交互。
- 禁止使用紫色作为主色。
- 门户菜单使用水平模式；后台管理可使用侧栏垂直菜单并允许内嵌子菜单。
- 下拉框宽度要根据内容合理设置，避免选项显示不全。
- 避免用 `!important` 覆盖 `.el-button--primary` 背景；确需覆盖时排除 `.is-link` 和 `.is-text`。
- npm、nodejs、pip 使用国内镜像地址。

### Vue.js 构建脚本规范

前端脚本需要保留以下语义：

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

如项目为了 TypeScript 校验或局域网访问扩展命令，可以保留 `vue-tsc` 或 `--host 0.0.0.0`，但端口仍必须是 `9999`。

## 微信小程序规范

- 小程序端统一使用 `apps/wechat-miniprogram/` 开发。
- 小程序使用原生微信语法：WXML、WXSS、JavaScript、JSON，不使用 uni-app。
- UI 组件使用 Vant Weapp，包名固定为 `@vant/weapp`。
- 为避免微信开发者工具出现 `Error: tourist appid`，`project.config.json` 中 AppID 固定配置为 `wxd84d204ed36b05b5`。
- 页面样式使用 `rpx` 单位，自动适配不同屏幕尺寸。
- 普通页面操作按钮、底部固定操作栏按钮、卡片内按钮，优先使用 `view` / `text` 模拟按钮并绑定 `bindtap`。
- 原生 `button` 仅用于需要微信原生能力的场景，例如 `open-type`、授权、分享、获取手机号、表单提交等。
- 如必须使用原生 `button` 做自定义样式，必须完整重置 `margin`、`padding`、`border`、`line-height`、`text-align`、`background`、`border-radius`，并设置 `button::after { border: 0; }`。
- 底部固定操作栏必须适配安全区：`padding-bottom: calc(基础间距 + env(safe-area-inset-bottom))`。
- 页面内容区必须为底部固定操作栏预留空间，避免最后一块内容被遮挡。
- 构建产物 `apps/wechat-miniprogram/miniprogram_npm/` 和个人配置 `project.private.config.json` 不提交到 Git。

## 测试调试规范

- 调试过程中不要自动启动程序或服务，端口调试由开发者手动控制。
- 数据库操作使用 mysql 命令执行，完成后立即运行 `mysqldump database_name > db/db.sql` 更新数据库文件，不保留临时 SQL 文件。
- 需要页面测试时使用 chrome-devtools。

## Git 规范

- 项目必须保留 `.gitignore`。
- 忽略编译产物、依赖包、IDE 配置、日志、环境变量和运行数据。

## 文档规范

- 每次开发代码之后不要额外生成总结性文档，除非用户主动要求。
- 项目只保留根目录一个 `README.md`。
- 根目录 `README.md` 负责项目总览、启动入口、模块说明和资料索引。
- 开发规范只维护在本文件，避免分散到业务目录。
