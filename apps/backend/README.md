# 后端 API

Spring Boot 后端服务，提供登录鉴权、用户管理、健康检查等基础接口。

`com.fullstack.starter` 是脚手架示例包名。真实业务项目可按需要改成业务包名；改名时同步更新代码、配置和文档引用。

## 技术栈

Java 21、Spring Boot 3.3.4、MyBatis Plus 3.5.7、MySQL Connector 8.0.26、Maven。

## 常用命令

```bash
cd apps/backend
mvn test
mvn clean package
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/<business_db>?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai'
export AUTH_TOKEN_SECRET='<at-least-32-random-characters>'
mvn spring-boot:run
```

数据库 URL 和 Token 密钥必须显式设置；正式业务不得使用 `template_db` 或公开固定密钥。Compose 模式由 `scripts/workflow.py init` 生成 `apps/.env`。

## 目录结构

| 目录 | 说明 |
| --- | --- |
| `src/main/java/com/fullstack/starter/controller/` | HTTP 接口入口。 |
| `src/main/java/com/fullstack/starter/service/` | 业务接口。 |
| `src/main/java/com/fullstack/starter/service/impl/` | 业务逻辑实现。 |
| `src/main/java/com/fullstack/starter/mapper/` | MyBatis Mapper 接口。 |
| `src/main/java/com/fullstack/starter/entity/` | 数据库实体。 |
| `src/main/java/com/fullstack/starter/dto/` | 请求和响应对象。 |
| `src/main/java/com/fullstack/starter/common/` | 通用响应结构等。 |
| `src/main/java/com/fullstack/starter/config/` | 拦截器、跨域、MyBatis 配置。 |
| `src/main/java/com/fullstack/starter/util/` | 工具类（密码、Token）。 |
| `src/main/java/com/fullstack/starter/exception/` | 全局异常处理。 |
| `src/main/resources/mapper/` | MyBatis XML。 |
| `src/main/resources/application.yml` | 后端配置。 |

## 开发顺序

新增后端功能一般按这个顺序做：

1. 改 SQL（只改根目录 `db/db.sql`）
2. 建实体（`entity/`）
3. 写 Mapper（`mapper/` + `resources/mapper/`）
4. 写 Service（`service/`）
5. 写 Controller（`controller/`）

## 注意事项

- 禁止使用 Lombok。
- 统一响应结构包含 `code`、`message`、`data`、`timestamp`。
- Controller 只处理 HTTP 入参、出参和权限上下文，业务逻辑放到 Service。
- 数据库表结构更新后，只同步根目录 `db/db.sql`，不要在后端资源目录保留第二份 SQL。
- `db/db.sql` 是快照，不是迁移脚本；已有数据库禁止导入含 `DROP TABLE` 的 dump。
