import json
import os
from typing import AsyncGenerator

import httpx

DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY", "")
DEEPSEEK_BASE_URL = os.getenv("DEEPSEEK_BASE_URL", "https://api.deepseek.com").rstrip("/")
DEEPSEEK_MODEL = os.getenv("DEEPSEEK_MODEL", "deepseek-v4-pro")

SYSTEM_PROMPT = "你是一个专业、清晰、可靠的AI助手。请使用中文优先回答，保持简洁，但在用户需要时提供完整步骤。"


def sse_event(payload: dict) -> str:
    return f"data: {json.dumps(payload, ensure_ascii=False)}\n\n"


async def chat_stream(
    messages: list[dict],
) -> AsyncGenerator[str, None]:
    if not DEEPSEEK_API_KEY:
        yield sse_event({
            "type": "error",
            "message": "缺少 DEEPSEEK_API_KEY，请在 apps/ai-service/.env 中配置 DeepSeek API Key",
        })
        yield sse_event({"type": "done"})
        return

    system_msg = {"role": "system", "content": SYSTEM_PROMPT}
    api_messages = [system_msg] + [m for m in messages if m.get("role") != "system"]

    payload = {
        "model": DEEPSEEK_MODEL,
        "messages": api_messages,
        "stream": True,
        "temperature": 0.7,
    }
    headers = {
        "Authorization": f"Bearer {DEEPSEEK_API_KEY}",
        "Content-Type": "application/json",
    }

    try:
        async with httpx.AsyncClient(timeout=None) as client:
            async with client.stream(
                "POST",
                f"{DEEPSEEK_BASE_URL}/chat/completions",
                headers=headers,
                json=payload,
            ) as response:
                if response.status_code >= 400:
                    body = await response.aread()
                    yield sse_event({
                        "type": "error",
                        "message": f"DeepSeek API 请求失败：HTTP {response.status_code} {body.decode('utf-8', errors='ignore')}",
                    })
                    yield sse_event({"type": "done"})
                    return

                async for line in response.aiter_lines():
                    if not line.startswith("data:"):
                        continue

                    data = line.removeprefix("data:").strip()
                    if not data:
                        continue
                    if data == "[DONE]":
                        break

                    try:
                        chunk = json.loads(data)
                    except json.JSONDecodeError:
                        continue

                    choices = chunk.get("choices") or []
                    if not choices:
                        continue

                    delta = choices[0].get("delta") or {}
                    content = delta.get("content")
                    if not content:
                        continue

                    yield sse_event({"type": "text", "content": content})

        yield sse_event({"type": "text_done"})
        yield sse_event({"type": "done"})

    except Exception as exc:
        yield sse_event({"type": "error", "message": f"DeepSeek API 调用异常：{exc}"})
        yield sse_event({"type": "done"})
