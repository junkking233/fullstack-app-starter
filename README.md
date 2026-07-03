# Fullstack App Starter

全栈项目模板，包含 Spring Boot 后端、Vue Web 前端、原生微信小程序和 FastAPI AI 助手服务。适合按需裁剪为 Web 系统、小程序系统或带 AI 能力的业务系统。

`fullstack-app-starter` 只是脚手架仓库名，不是业务项目名。每次克隆后应按真实业务项目改名，并切断与脚手架仓库的 Git 关联。

第一次打开项目，先读本文件，再读：

1. `docs/开发规范-AGENTS.md`
2. `docs/开发工作流-Workflow.md`
3. `docs/工作流状态-WorkflowState.md`
4. `index.html`（可直接打开的阶段看板）

## 能力概览

| 模块 | 说明 |
| --- | --- |
| 后端 API | 登录鉴权、用户管理、健康检查、统一响应结构 |
| Web 前端 | Vue + Vite + Element Plus，门户和管理后台基础页面 |
| 微信小程序 | 原生 WXML/WXSS/JS/JSON + Vant Weapp |
| AI 助手服务 | FastAPI + DeepSeek，提供 SSE 流式聊天接口 |
| 数据库 | MySQL 初始化脚本和基础账号数据 |

## 作为业务项目使用

新需求使用本脚手架时，必须先完成这几步：

1. 克隆 `https://github.com/junkking233/fullstack-app-starter.git` 到新的业务项目目录。
2. 将目录名、文档中的项目名和必要配置改成真实业务项目名，不要继续叫 `fullstack-app-starter`。
3. 删除脚手架自带的 `.git`，避免把业务代码提交回脚手架仓库。
4. 需要版本控制时，在业务项目目录重新 `git init`，再绑定新的业务仓库 remote。

AI 接手业务项目时，不得把业务项目称为 `fullstack-app-starter`，也不得保留指向脚手架仓库的 Git remote。

## 目录结构

```text
fullstack-app-starter/
├── apps/
│   ├── backend/              # Spring Boot 后端 API
│   ├── frontend/             # Vue + Vite Web 前端
│   ├── miniprogram/          # 原生微信小程序
│   ├── aiassistant/          # FastAPI AI 助手服务
│   └── docker-compose.yml    # 项目服务编排
├── assets/
│   ├── README.md             # 共享图标规范
│   ├── icon-set.html         # 图标预览
│   └── icons/                # 通用 SVG 图标
├── db/
│   └── db.sql                # 数据库脚本
├── design/
│   └── lovart/               # Lovart 原型图、PSD、PNG
├── index.html                # 可直接打开的工作流状态看板
└── docs/
    ├── 开发规范-AGENTS.md
    ├── 开发工作流-Workflow.md
    ├── 工作流状态-WorkflowState.md
    ├── Goal长程计划模板-GoalPlanTemplate.md
    ├── 原型生图提示词模板-LovartPromptTemplate.md
    ├── 产品需求文档-PRD.md
    └── API接口文档-API.md
```

按需生成的文档：

| 文件 | 用途 |
| --- | --- |
| `docs/原型生图提示词模板-LovartPromptTemplate.md` | 约束 Lovart Prompt 的页面清单、设计系统、单页提示词和验收结构 |
| `docs/原型生图提示词-LovartPrompt.md` | 基于 PRD 生成 Lovart 可复制提示词 |
| `docs/页面设计文档-UIDesign.md` | 基于 Lovart 设计稿拆解页面结构、状态和资源 |
| `docs/Goal长程计划-GoalPlan.md` | 长程任务执行计划 |

子应用说明：

| 子应用 | 文档 |
| --- | --- |
| 后端 API | `apps/backend/README.md` |
| Web 前端 | `apps/frontend/README.md` |
| 微信小程序 | `apps/miniprogram/README.md` |
| AI 助手服务 | `apps/aiassistant/README.md` |

## 环境要求

| 环境 | 说明 |
| --- | --- |
| Java | JDK 21 |
| Maven | 后端构建 |
| Node.js + npm | Web 和小程序依赖 |
| Docker + Docker Compose | 编排项目服务 |
| 微信开发者工具 | 调试小程序 |
| MySQL | 使用已有 `mysql-docker`，账号 `root/admin123` |

## 手动启动

调试规范要求 AI 不自动启动服务。下面仅作为开发者手动启动参考。

```bash
docker compose -f apps/docker-compose.yml up --build -d
docker compose -f apps/docker-compose.yml down
```

AI 助手服务需要 DeepSeek Key：

```bash
cp apps/aiassistant/.env.example apps/aiassistant/.env
export DEEPSEEK_API_KEY=你的DeepSeekKey
```

## 访问地址

| 服务 | 地址 |
| --- | --- |
| Web 前端 | `http://localhost:9999` |
| 后端 API | `http://localhost:8888` |
| 后端健康检查 | `http://localhost:8888/api/health` |
| AI 助手服务 | `http://localhost:8000` |
| AI 健康检查 | `http://localhost:8000/health` |

## 微信小程序

```bash
cd apps/miniprogram
npm install
```

然后用微信开发者工具导入 `apps/miniprogram/`，执行“工具 -> 构建 npm”。AppID 固定为 `wxd84d204ed36b05b5`。

## 登录账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 服务方 | `partner` | `123456` |
| 普通用户 | `user` | `123456` |

密码使用 MD5，不加 salt。

## 数据库

导入：

```bash
docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db/db.sql
```

导出：

```bash
docker exec mysql-docker mysqldump -uroot -padmin123 --default-character-set=utf8mb4 template_db > db/db.sql
```

数据库变化后必须同步 `db/db.sql`。

## 阶段看板

根目录 `index.html` 可直接双击或用浏览器打开。每推进一个阶段，必须同步更新：

- `docs/工作流状态-WorkflowState.md`
- `index.html`
- `docs/Goal长程计划-GoalPlan.md`（如存在）

阶段推进不能只看“做了一部分”。如果存在 GoalPlan，必须先通过 Goal 完成门禁：实现拆解、验收矩阵、风险和执行记录都确认完成或标明不适用后，才能从功能实现进入对抗式审查。

## 按需裁剪

- 只做小程序：保留 `apps/backend/`、`apps/miniprogram/`、`db/` 和必要文档，可删除 Web 和 AI 助手。
- 移除小程序：删除 `apps/miniprogram/`。
- 移除 AI 助手：删除 `apps/aiassistant/`，并同步清理 Compose 和前端引用。
