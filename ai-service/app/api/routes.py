from fastapi import APIRouter
from fastapi.responses import StreamingResponse

from app.models.schemas import ChatRequest
from app.services import deepseek_service

router = APIRouter()


@router.post("/chat/stream")
async def chat_stream(req: ChatRequest):
    """SSE streaming chat endpoint."""
    messages = [{"role": m.role, "content": m.content} for m in req.messages]

    async def event_generator():
        async for event in deepseek_service.chat_stream(
            messages,
        ):
            yield event

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Accel-Buffering": "no",
        },
    )
