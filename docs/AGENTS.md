# 项目开发规范

本文件只维护开发规范和约束。项目启动、模块介绍、目录说明请看根目录 `README.md`。

## 通用约定

### 端口配置

- 后端 API 端口固定为 `8888`。
- 前端服务端口固定为 `9999`。
- AI 服务端口固定为 `8000`。

### Docker 与数据库

- 默认使用根目录 `docker-compose.yml` 管理项目服务。
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

- 项目在 `apps/assests/icons/` 下维护了一套通用线性图标库（84 个 SVG），使用 `stroke="currentColor"`，可通过 CSS color 控制颜色。
- 开发时按需将用到的 SVG 复制到子应用目录中（如前端放 `src/assets/icons/`），不要一次性全部复制。
- 图标列表和预览可打开 `apps/assests/icon-set.html` 查看。
- 常用图标：`home`、`user`、`search`、`settings`、`bell`、`cart`、`mail`、`edit`、`trash`、`plus`、`close`、`check-circle`、`warning`、`error`、`info`。

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

## uni-app 规范

- 小程序端统一使用 `apps/uniapp/` 开发，禁止新增原生微信小程序目录或页面。
- 为避免微信开发者工具出现 `Error: tourist appid`，`manifest.json` 中 AppID 固定配置为 `wxd84d204ed36b05b5`。
- 使用 Vue 3 组合式 API（`<script setup>`）编写页面。
- UI 组件使用 uni-ui，通过 easycom 自动引入，无需手动 import。
- 跨平台兼容：优先使用 `uni.*` 统一接口，避免使用平台特有 API。
- 页面样式使用 `rpx` 单位，自动适配不同屏幕尺寸。
- 底部固定操作栏必须适配安全区，并为页面内容区预留空间，避免最后一块内容被遮挡。
- 编译产物（`dist/`）不提交到 Git。

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
