# 后端 API

Spring Boot 后端服务，提供登录鉴权、用户管理、图表数据、健康检查等接口。

## 技术栈

Java 21、Spring Boot 3.3.4、MyBatis Plus 3.5.7、MySQL Connector 8.0.26、Maven。

## 常用命令

```bash
cd apps/backend
mvn clean package
mvn spring-boot:run
```

## 目录结构

| 目录 | 说明 |
| --- | --- |
| `src/main/java/com/example/controller/` | HTTP 接口入口。 |
| `src/main/java/com/example/service/` | 业务逻辑。 |
| `src/main/java/com/example/mapper/` | MyBatis Mapper 接口。 |
| `src/main/java/com/example/entity/` | 数据库实体。 |
| `src/main/java/com/example/dto/` | 请求和响应对象。 |
| `src/main/java/com/example/common/` | 通用响应结构等。 |
| `src/main/java/com/example/config/` | 拦截器、跨域、MyBatis 配置。 |
| `src/main/java/com/example/util/` | 工具类（密码、Token）。 |
| `src/main/java/com/example/exception/` | 全局异常处理。 |
| `src/main/resources/mapper/` | MyBatis XML。 |
| `src/main/resources/application.yml` | 后端配置。 |
| `src/main/resources/db/init.sql` | 后端初始化 SQL。 |

## 开发顺序

新增后端功能一般按这个顺序做：

1. 改 SQL（`db/db.sql` 和 `src/main/resources/db/init.sql`）
2. 建实体（`entity/`）
3. 写 Mapper（`mapper/` + `resources/mapper/`）
4. 写 Service（`service/`）
5. 写 Controller（`controller/`）

## 注意事项

- 禁止使用 Lombok。
- 统一响应结构包含 `code`、`message`、`data`、`timestamp`。
- Controller 只处理 HTTP 入参、出参和权限上下文，业务逻辑放到 Service。
- 数据库表结构更新后，需要同步更新 `db/db.sql`。
