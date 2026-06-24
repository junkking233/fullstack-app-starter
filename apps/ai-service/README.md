# AI 服务

FastAPI AI 服务，调用 DeepSeek 大模型，给前端提供 SSE 流式聊天接口。

> **可选模块**：如果项目不需要 AI 助手，可以删除整个 `apps/ai-service/` 目录，并从 `docker-compose.yml` 中移除 `ai-service` 服务。

## 技术栈

FastAPI、Uvicorn、Pydantic、httpx、python-dotenv。

## 常用命令

```bash
cd apps/ai-service
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

## 环境变量

启动前需要配置环境变量：

```bash
cp .env.example .env
```

然后编辑 `.env`，填入 DeepSeek API Key。

## 主要文件

| 文件 | 说明 |
| --- | --- |
| `main.py` | FastAPI 应用入口。 |
| `app/api/routes.py` | AI 接口路由。 |
| `app/models/schemas.py` | 请求模型。 |
| `app/services/deepseek_service.py` | DeepSeek 调用逻辑。 |
| `.env.example` | 环境变量示例。 |

## 接口

| 路径 | 说明 |
| --- | --- |
| `GET /health` | 健康检查。 |
| `POST /api/ai/chat/stream` | SSE 流式聊天接口。 |
