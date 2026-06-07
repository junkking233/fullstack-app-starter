# Fullstack App Starter

这是一个包含后端、网页前端、微信小程序和 AI 服务的全栈项目模板。你可以把它当成一个已经搭好基础框架的项目：登录、角色、用户管理、门户页面、管理后台、小程序页面、AI 流式聊天接口和数据库初始化都已经放好。

如果你是第一次打开这个项目，先看这一页就够了。

## 这个项目能做什么

| 能力 | 入口 |
| --- | --- |
| 网页门户 | 普通用户访问服务大厅、我的业务、个人中心。 |
| 管理后台 | 管理员查看运营概览、用户管理、数据图表、AI 助手。 |
| 服务方门户 | 服务方查看工单、资源和报表。 |
| 微信小程序 | 首页、分类、购物车、订单、地址、优惠券、登录注册。 |
| 后端 API | 登录鉴权、用户管理、图表数据、健康检查。 |
| AI 服务 | FastAPI 调用 DeepSeek，给前端提供 SSE 流式聊天接口。 |

## 开发路线

| 你想做什么 | 去哪里改 |
| --- | --- |
| 改后端接口、数据库实体、业务逻辑 | `apps/backend/` |
| 改网页页面、后台管理、门户页面 | `apps/frontend/` |
| 改微信小程序页面和小程序接口 | `apps/wechat-miniprogram/` |
| 改 AI 助手、DeepSeek 调用、流式接口 | `apps/ai-service/` |
| 改数据库初始化脚本 | `db/db.sql` 和 `apps/backend/src/main/resources/db/init.sql` |
| 看项目开发规范 | `docs/AGENTS.md` |
| 看 ER 图、用例图、模板 | `docs/` |
| 改 Docker 启动编排 | `docker-compose.yml` |

AI 协作开发前，请先让 AI 读取 `docs/AGENTS.md`。很多 AI 工具默认只找根目录 `AGENTS.md`，但本项目为了根目录干净，把规范放到了 `docs/AGENTS.md`。

## 目录结构

```text
fullstack-app-starter/
├── apps/
│   ├── backend/              # Spring Boot 后端 API
│   ├── frontend/             # Vue + Vite 网页前端
│   ├── wechat-miniprogram/   # 微信小程序
│   └── ai-service/           # FastAPI AI 服务
├── db/
│   ├── db.sql                # 数据库初始化脚本
│   └── start-mysql.sh        # 启动共享 MySQL 容器
├── docs/
│   ├── AGENTS.md             # 项目开发规范
│   ├── templates/            # 文档模板
│   └── diagrams/             # ER 图、用例图
├── docker-compose.yml        # Docker Compose 编排
└── README.md                 # 项目说明入口
```

## 环境要求

| 环境 | 说明 |
| --- | --- |
| Java | JDK 21 |
| Maven | 后端构建工具 |
| Node.js + npm | 网页前端和小程序依赖管理 |
| Docker + Docker Compose | 启动后端、前端、AI 服务和连接 MySQL |
| 微信开发者工具 | 开发微信小程序时需要 |
| MySQL | 使用共享容器 `mysql-docker`，账号密码 `root/admin123` |

## 启动项目

调试规范要求不要自动启动服务。下面命令只作为手动启动参考。

1. 启动共享 MySQL：

```bash
chmod +x db/start-mysql.sh
./db/start-mysql.sh
```

2. 如果要使用 AI 助手，先配置环境变量：

```bash
cp apps/ai-service/.env.example apps/ai-service/.env
```

然后编辑 `apps/ai-service/.env`，填入 DeepSeek Key。

3. 启动项目：

```bash
docker compose up --build -d
```

4. 停止项目：

```bash
docker compose down
```

## 访问地址

| 服务 | 地址 |
| --- | --- |
| 前端页面 | `http://localhost:9999` |
| 登录页 | `http://localhost:9999/login` |
| 后端 API | `http://localhost:8888` |
| 后端健康检查 | `http://localhost:8888/api/health` |
| AI 服务 | `http://localhost:8000` |
| AI 健康检查 | `http://localhost:8000/health` |

## 登录账号

| 角色 | 账号 | 密码 | 能访问什么 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin123` | 管理后台、数据大屏、用户管理、AI 助手。 |
| 服务方 | `partner` | `123456` | 服务方门户、工单、资源、报表。 |
| 普通用户 | `user` | `123456` | 普通用户门户、服务大厅、我的业务。 |

密码使用 MD5 加密保存，不使用 salt。

## 后端开发

位置：`apps/backend/`

技术栈：Java 21、Spring Boot 3.3.4、MyBatis Plus 3.5.7、MySQL Connector 8.0.26、Maven。

常用命令：

```bash
cd apps/backend
mvn clean package
mvn spring-boot:run
```

常用目录：

| 目录 | 放什么 |
| --- | --- |
| `src/main/java/com/example/controller/` | HTTP 接口入口。 |
| `src/main/java/com/example/service/` | 业务逻辑。 |
| `src/main/java/com/example/mapper/` | MyBatis Mapper 接口。 |
| `src/main/java/com/example/entity/` | 数据库实体。 |
| `src/main/java/com/example/dto/` | 请求和响应对象。 |
| `src/main/resources/mapper/` | MyBatis XML。 |
| `src/main/resources/application.yml` | 后端配置。 |
| `src/main/resources/db/init.sql` | 后端初始化 SQL。 |

新增后端功能一般按这个顺序做：先改 SQL，再建实体，再写 Mapper，再写 Service，最后写 Controller。

## 网页前端开发

位置：`apps/frontend/`

技术栈：Vue 3、Vite、TypeScript、Element Plus、Vue Router、Axios、ECharts。

常用命令：

```bash
cd apps/frontend
npm install
npm run start
npm run build
```

页面和代码位置：

| 内容 | 位置 |
| --- | --- |
| 路由 | `src/router/index.ts` |
| 接口请求 | `src/api/` |
| 登录态工具 | `src/utils/auth.ts` |
| 普通用户页面 | `src/views/portal/` |
| 服务方页面 | `src/views/partner/` |
| 管理后台页面 | `src/views/admin/`、`src/admin-platform/views/` |
| 公共组件 | `src/components/` |
| 全局样式 | `src/styles.css` |

路由分区：

| 区域 | 路由 |
| --- | --- |
| 普通用户门户 | `/portal/*` |
| 服务方门户 | `/partner/*` |
| 管理后台 | `/admin/*` |
| 数据大屏 | `/admin/screen` |
| 登录页 | `/login` |

前端代理在 `apps/frontend/vite.config.ts` 中配置：`/api/*` 转发到后端，`/api/ai/*` 转发到 AI 服务。

## 微信小程序开发

位置：`apps/wechat-miniprogram/`

常用命令：

```bash
cd apps/wechat-miniprogram
npm install
```

然后用微信开发者工具导入 `apps/wechat-miniprogram`，并在工具里执行“构建 npm”。

常用目录：

| 目录 | 放什么 |
| --- | --- |
| `pages/` | 小程序页面。 |
| `services/` | 商品、购物车、订单等业务接口封装。 |
| `utils/` | 请求、登录态、格式化工具。 |
| `assets/` | 图片资源。 |

本地接口地址在 `apps/wechat-miniprogram/app.js` 的 `apiBaseUrl` 中配置。真机调试时，需要把它改成手机可访问的局域网或公网地址。

## AI 服务开发

位置：`apps/ai-service/`

技术栈：FastAPI、Uvicorn、Pydantic、httpx、python-dotenv。

常用命令：

```bash
cd apps/ai-service
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

主要文件：

| 文件 | 说明 |
| --- | --- |
| `main.py` | FastAPI 应用入口。 |
| `app/api/routes.py` | AI 接口路由。 |
| `app/models/schemas.py` | 请求模型。 |
| `app/services/deepseek_service.py` | DeepSeek 调用逻辑。 |
| `.env.example` | 环境变量示例。 |

主要接口：

| 路径 | 说明 |
| --- | --- |
| `GET /health` | 健康检查。 |
| `POST /api/ai/chat/stream` | SSE 流式聊天接口。 |

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

## 文档资料

| 文件 | 说明 |
| --- | --- |
| `docs/AGENTS.md` | 项目开发规范，AI 开发前需要读取。 |
| `docs/templates/模板.docx` | 文档模板。 |
| `docs/diagrams/database/数据库ER图.png` | 数据库 ER 图。 |
| `docs/diagrams/use-cases/用例图样例.drawio` | 用例图 draw.io 源文件。 |
| `docs/diagrams/use-cases/用例图样例.png` | 用例图图片。 |

## 不要提交

- `node_modules/`
- `dist/`
- `target/`
- `.env`
- `__pycache__/`
- `apps/wechat-miniprogram/miniprogram_npm/`
- `apps/wechat-miniprogram/project.private.config.json`
