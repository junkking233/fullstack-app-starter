#!/bin/bash

# 启动共享 MySQL 容器（mysql-docker）
# 所有项目共享这个数据库，通过 localhost:3306 或 mysql-docker:3306 访问

set -e

DATABASE_NAME="template_db"
MYSQL_ROOT_PASSWORD="admin123"
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DB_SQL="${PROJECT_ROOT}/db/db.sql"

docker network create shared-network >/dev/null 2>&1 || true

if docker ps -a --format '{{.Names}}' | grep -q "^mysql-docker$"; then
    echo "容器 mysql-docker 已存在，正在启动..."
    docker start mysql-docker >/dev/null
    docker network connect shared-network mysql-docker 2>/dev/null || true
else
    echo "首次运行，创建 mysql-docker 容器..."
    docker run -d \
      --name mysql-docker \
      --network shared-network \
      -p 3306:3306 \
      -e MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD}" \
      -e MYSQL_DATABASE="${DATABASE_NAME}" \
      -e TZ=Asia/Shanghai \
      -v mysql-docker-data:/var/lib/mysql \
      -v "${DB_SQL}:/docker-entrypoint-initdb.d/db.sql:ro" \
      --restart unless-stopped \
      mysql:8.0
fi

until docker exec mysql-docker mysqladmin ping -uroot -p"${MYSQL_ROOT_PASSWORD}" --silent >/dev/null 2>&1; do
    sleep 1
done

docker exec -i mysql-docker mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" --default-character-set=utf8mb4 <<SQL
CREATE DATABASE IF NOT EXISTS ${DATABASE_NAME} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SQL

if [ -f "${DB_SQL}" ]; then
    docker exec -i mysql-docker mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" --default-character-set=utf8mb4 < "${DB_SQL}"
fi

echo ""
echo "✅ MySQL 容器 mysql-docker 已就绪"
docker ps --filter "name=mysql-docker" --format "   状态: {{.Status}}"
echo ""
echo "连接方式:"
echo "   • 本机程序 / IDEA 调试:  jdbc:mysql://localhost:3306/${DATABASE_NAME}"
echo "   • 其他 Docker 容器:      jdbc:mysql://mysql-docker:3306/${DATABASE_NAME}"
echo "   • 账号: root / 密码: ${MYSQL_ROOT_PASSWORD}"
