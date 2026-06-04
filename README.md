# SpringBoot Vue Starter

## 启动命令

1. 启动共享 MySQL：

```bash
chmod +x scripts/start-mysql.sh
./scripts/start-mysql.sh
```

2. 如需手动同步数据库结构：

```bash
docker exec -i mysql-docker mysql -uroot -padmin123 --default-character-set=utf8mb4 < db.sql
```

3. 配置 DeepSeek Key：

```bash
cp ai-service/.env.example ai-service/.env
```

然后编辑 `ai-service/.env`：

```env
DEEPSEEK_API_KEY=你的DeepSeek API Key
DEEPSEEK_MODEL=deepseek-v4-pro
DEEPSEEK_BASE_URL=https://api.deepseek.com
```

4. 启动项目：

```bash
docker compose up --build -d
```

5. 停止项目：

```bash
docker compose down
```

## 访问地址

- 前端页面：http://localhost:9999
- 后端 API：http://localhost:8888
- AI 服务：http://localhost:8000

## 登录页面和账号

启动后访问登录页：

```text
http://localhost:9999/login
```

预置系统角色和账号：

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 服务方 | `partner` | `123456` |
| 普通用户 | `user` | `123456` |

密码在数据库中使用 MD5 加密保存。
