#!/bin/sh
set -eu

ROOT=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)

python3 "$ROOT/scripts/workflow.py" validate
python3 -m unittest discover -s "$ROOT/scripts/tests"
node "$ROOT/scripts/check-dashboard.js"
ENV_FILE="$ROOT/apps/.env.example"
if [ -f "$ROOT/apps/.env" ]; then
  ENV_FILE="$ROOT/apps/.env"
fi
COMPOSE_PROJECT_NAME=app-scaffold-check \
APP_DB_NAME=template_db \
AUTH_TOKEN_SECRET=check-only-token-secret-with-at-least-32-characters \
  docker compose --env-file "$ENV_FILE" -f "$ROOT/apps/docker-compose.yml" config >/dev/null
COMPOSE_PROJECT_NAME=app-scaffold-check \
APP_DB_NAME=template_db \
AUTH_TOKEN_SECRET=check-only-token-secret-with-at-least-32-characters \
  docker compose --env-file "$ENV_FILE" -f "$ROOT/apps/docker-compose.yml" --profile ai config >/dev/null

(
  cd "$ROOT/apps/backend"
  mvn test -q
)

(
  cd "$ROOT/apps/frontend"
  npm run check
)

(
  cd "$ROOT/apps/miniprogram"
  npm run check
)

(
  cd "$ROOT/apps/aiassistant"
  python3 -m unittest discover -s tests
)
