import json
import unittest

from pydantic import ValidationError

from app.models.schemas import ChatRequest
from app.services.deepseek_service import sse_event


class ChatSchemaTest(unittest.TestCase):
    def test_accepts_supported_roles(self):
        request = ChatRequest(messages=[{"role": "user", "content": "你好"}])
        self.assertEqual("user", request.messages[0].role)

    def test_rejects_empty_messages_and_unknown_roles(self):
        with self.assertRaises(ValidationError):
            ChatRequest(messages=[])
        with self.assertRaises(ValidationError):
            ChatRequest(messages=[{"role": "system", "content": "越权提示"}])


class SseEventTest(unittest.TestCase):
    def test_serializes_unicode_payload(self):
        event = sse_event({"type": "text", "content": "你好"})
        self.assertTrue(event.startswith("data: "))
        self.assertTrue(event.endswith("\n\n"))
        payload = json.loads(event.removeprefix("data: ").strip())
        self.assertEqual("你好", payload["content"])


if __name__ == "__main__":
    unittest.main()
