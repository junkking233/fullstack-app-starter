## 项目开发规范

### 端口配置
- 后端 API 端口固定为 `8888`。
- 前端服务端口固定为 `9999`。

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

### 前端
- 使用 Vue + Vite + npm，必须包含 `index.html`，禁止使用 Vue CLI。
- UI 框架使用 Element UI/Element Plus，优先用组件实现交互。
- 禁止使用紫色作为主色。
- 门户菜单使用水平模式；后台管理可使用侧栏垂直菜单并允许内嵌子菜单。
- 下拉框宽度要根据内容合理设置，避免选项显示不全。
- 避免用 `!important` 覆盖 `.el-button--primary` 背景；确需覆盖时排除 `.is-link` 和 `.is-text`。
- npm、nodejs、pip 使用国内镜像地址。

### 后端
- Java 项目禁止使用 Lombok。
- Spring Boot 版本固定为 `3.3.4`。
- Java JDK 版本固定为 `21`。
- MyBatis Plus 版本固定为 `3.5.7`。
- MySQL Maven 依赖版本固定为 `8.0.26`。
- Spring Boot 配置需要支持 Maven 热加载。
- 统一响应结构包含 `code`、`message`、`data`、`timestamp`。

### 测试调试
- 调试过程中不要自动启动程序或服务，端口调试由开发者手动控制。
- 数据库操作使用 mysql 命令执行，完成后立即运行 `mysqldump database_name > db.sql` 更新数据库文件，不保留临时 SQL 文件。
- 需要页面测试时使用 chrome-devtools。

### Git
- 项目必须保留 `.gitignore`。
- 忽略编译产物、依赖包、IDE 配置、日志、环境变量和运行数据。

### 文档
- 每次开发代码之后不要额外生成总结性文档，除非用户主动要求。
