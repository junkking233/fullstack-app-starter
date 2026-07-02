# Fullstack App Starter

这是一个全栈项目模板，包含后端 API、网页前端、原生微信小程序端和 AI 助手服务四个子应用。登录、角色、用户管理、门户页面、管理后台、小程序页面、AI 流式聊天接口和数据库初始化都已经搭好。

如果你是第一次打开这个项目，先看这一页就够了。

## 这个项目能做什么

| 能力 | 说明 |
| --- | --- |
| 网页门户 | 普通用户访问服务大厅、我的业务、个人中心。 |
| 管理后台 | 管理员查看运营概览、用户管理、数据图表、AI 助手。 |
| 服务方门户 | 服务方查看工单、资源和报表。 |
| 原生微信小程序端 | 使用原生微信语法和 Vant Weapp，提供首页、登录和接口请求基础封装。 |
| 后端 API | 登录鉴权、用户管理、图表数据、健康检查。 |
| AI 助手服务 | FastAPI 调用 DeepSeek，给前端提供 SSE 流式聊天接口。 |

## 目录结构

```text
fullstack-app-starter/
├── apps/
│   ├── backend/              # Spring Boot 后端 API（必须）
│   ├── docker-compose.yml    # Docker Compose 编排
│   ├── frontend/             # Vue + Vite 网页前端（默认包含）
│   ├── miniprogram/          # 原生微信小程序端（可选）
│   └── aiassistant/          # FastAPI AI 助手服务（可选）
├── db/
│   └── db.sql                # 数据库初始化脚本
├── assets/
│   ├── README.md             # 共享图标资源使用规范
│   ├── icon-set.html         # 通用图标库预览
│   └── icons/                # 通用线性图标 SVG
├── docs/
│   ├── AGENTS.md             # 项目开发规范
│   ├── API文档.md             # 接口文档
│   ├── DEVELOPMENT_WORKFLOW.md # 阶段化开发工作流
│   ├── GOAL_PLAN_TEMPLATE.md # 长程任务 Goal 计划模板
│   ├── WORKFLOW_STATE.md     # 当前开发阶段和下一步状态
│   └── 需求文档.md            # 需求文档
└── README.md                 # 项目说明入口
```

每个子应用的开发说明在各自的 README 中：

| 子应用 | 详细文档 |
| --- | --- |
| 后端 API | [`apps/backend/README.md`](apps/backend/README.md) |
| 网页前端 | [`apps/frontend/README.md`](apps/frontend/README.md) |
| 原生微信小程序端 | [`apps/miniprogram/README.md`](apps/miniprogram/README.md) |
| AI 助手服务 | [`apps/aiassistant/README.md`](apps/aiassistant/README.md) |

## 按需裁剪

默认项目包含 `apps/backend/`、`apps/frontend/`、`apps/miniprogram/` 和 `apps/aiassistant/`。实际开发时应按需求裁剪，不需要的端不要强行保留。

### 只做小程序项目

1. 保留 `apps/backend/`、`apps/miniprogram/`、`db/` 和必要文档。
2. 可删除 `apps/frontend/`，并从 `apps/docker-compose.yml` 中删除 `frontend` 服务。
3. 如果不需要 AI 能力，也删除 `apps/aiassistant/`；如果后续在 Compose 中启用了 AI 助手服务，也同步移除相关配置。

### 移除微信小程序端

1. 删除 `apps/miniprogram/` 目录。
2. `apps/docker-compose.yml` 中微信小程序没有独立服务，无需修改。

### 移除 AI 助手服务

1. 删除 `apps/aiassistant/` 目录。
2. 如果 `apps/docker-compose.yml` 中启用了 `aiassistant` 服务定义，也同步删除该服务。
3. 如果 `frontend` 服务中配置了对 `aiassistant` 的 `depends_on`，也同步移除。
4. 如果前端代码中有 AI 助手相关页面和路由，也需要同步清理。

## 环境要求

| 环境 | 说明 |
| --- | --- |
| Java | JDK 21 |
| Maven | 后端构建工具 |
| Node.js + npm | 网页前端和微信小程序端依赖管理 |
| Docker + Docker Compose | 按需编排项目服务并连接共享 MySQL |
| 微信开发者工具 | 调试原生微信小程序时需要 |
| MySQL | 使用共享容器 `mysql-docker`，账号密码 `root/admin123` |

## 启动项目

调试规范要求不要自动启动服务。下面命令只作为手动启动参考。

确保共享 MySQL 容器 `mysql-docker` 已在运行。

1. 如果要使用 AI 助手，先配置环境变量：

```bash
cp apps/aiassistant/.env.example apps/aiassistant/.env
```

然后编辑 `apps/aiassistant/.env`，填入 DeepSeek Key。

2. 启动项目：

```bash
docker compose -f apps/docker-compose.yml up --build -d
```

3. 停止项目：

```bash
docker compose -f apps/docker-compose.yml down
```

## 访问地址

| 服务 | 地址 |
| --- | --- |
| 前端页面 | `http://localhost:9999` |
| 登录页 | `http://localhost:9999/login` |
| 后端 API | `http://localhost:8888` |
| 后端健康检查 | `http://localhost:8888/api/health` |
| AI 助手服务 | `http://localhost:8000` |
| AI 健康检查 | `http://localhost:8000/health` |

## 微信小程序开发

微信小程序端在 `apps/miniprogram/`，使用原生微信语法和 Vant Weapp。

```bash
cd apps/miniprogram
npm install
```

然后用微信开发者工具导入 `apps/miniprogram/`，执行“工具 -> 构建 npm”。本地接口调试默认访问 `http://localhost:8888/api`，正式环境需要替换为合法 HTTPS 域名。

## 登录账号

| 角色 | 账号 | 密码 | 能访问什么 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin123` | 管理后台、数据大屏、用户管理、AI 助手。 |
| 服务方 | `partner` | `123456` | 服务方门户、工单、资源、报表。 |
| 普通用户 | `user` | `123456` | 普通用户门户、服务大厅、我的业务。 |

密码使用 MD5 加密保存，不使用 salt。

## 数据库开发

| 文件 | 用途 |
| --- | --- |
| `db/db.sql` | 项目共享数据库脚本，用于导入和导出。 |
| `apps/backend/src/main/resources/db/init.sql` | 后端资源目录中的初始化 SQL。 |
| `apps/backend/src/main/resources/application.yml` | 后端数据库连接配置。 |

手动导入：

```bash
docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db/db.sql
```

导出更新：

```bash
docker exec mysql-docker mysqldump -uroot -padmin123 --default-character-set=utf8mb4 template_db > db/db.sql
```

数据库发生变化时，要同步更新 `db/db.sql`。

## 开发规范

AI 协作开发前，请先让 AI 读取 `docs/AGENTS.md`。很多 AI 工具默认只找根目录 `AGENTS.md`，但本项目为了根目录干净，把规范放到了 `docs/AGENTS.md`。

## 文档资料

| 文件 | 说明 |
| --- | --- |
| `docs/AGENTS.md` | 项目开发规范，AI 开发前需要读取。 |
| `docs/API文档.md` | 接口文档。 |
| `docs/DEVELOPMENT_WORKFLOW.md` | 阶段化开发工作流，说明每一步该读什么、用哪个 skill、产出什么。 |
| `docs/GOAL_PLAN_TEMPLATE.md` | 长程任务 Goal 计划模板。 |
| `docs/WORKFLOW_STATE.md` | 当前开发阶段、最近完成、下一步和阻塞项。 |
| `docs/需求文档.md` | 需求文档。 |
