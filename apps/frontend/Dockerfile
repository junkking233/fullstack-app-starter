# syntax=docker/dockerfile:1
FROM node:20.18-alpine

LABEL maintainer="template-team"
LABEL description="Template Vue Frontend"

WORKDIR /app

COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm \
    npm config set registry https://registry.npmmirror.com && \
    (npm ci 2>/dev/null || npm install)

COPY . .

EXPOSE 9999

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD wget -qO- http://127.0.0.1:9999/ >/dev/null || exit 1

CMD ["npm", "run", "start"]
