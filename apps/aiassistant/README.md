# AI 助手服务

FastAPI AI 助手服务，调用 DeepSeek 大模型，给前端提供 SSE 流式聊天接口。

> **可选模块**：默认 Docker Compose 不启动本服务；需要时使用 `docker compose -f apps/docker-compose.yml --profile ai up --build -d`。如果项目不需要 AI 助手，可以删除整个目录并同步移除 Compose profile。

Compose 只把端口绑定到本机 `127.0.0.1:8000`。上线前必须由受认证的后端代理，并补充用户级权限和限流；不要直接把本服务暴露到公网。

## 技术栈

FastAPI、Uvicorn、Pydantic、httpx、python-dotenv。

## 常用命令

```bash
cd apps/aiassistant
pip install -r requirements.txt
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
python3 -m unittest discover -s tests
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
