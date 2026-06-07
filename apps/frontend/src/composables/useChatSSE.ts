import { ref } from 'vue';
import type { ChatMessage, SSEEvent } from '@/types/chat';

let messageCounter = 0;

function nextId() {
  messageCounter += 1;
  return `msg_${messageCounter}_${Date.now()}`;
}

export function useChatSSE() {
  const messages = ref<ChatMessage[]>([]);
  const streaming = ref(false);
  let abortController: AbortController | null = null;

  async function sendMessage(content: string) {
    const userMessage: ChatMessage = { id: nextId(), role: 'user', content };
    const assistantMessage: ChatMessage = { id: nextId(), role: 'assistant', content: '' };
    const history = [...messages.value, userMessage].map((item) => ({
      role: item.role,
      content: item.content,
    }));

    messages.value.push(userMessage, assistantMessage);
    streaming.value = true;
    abortController = new AbortController();

    try {
      const response = await fetch('/api/ai/chat/stream', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ messages: history }),
        signal: abortController.signal,
      });

      const reader = response.body?.getReader();
      if (!reader) {
        throw new Error('响应内容为空');
      }

      const decoder = new TextDecoder();
      let buffer = '';

      while (true) {
        const { done, value } = await reader.read();
        if (done) {
          break;
        }

        buffer += decoder.decode(value, { stream: true });
        const lines = buffer.split('\n');
        buffer = lines.pop() || '';

        for (const line of lines) {
          const trimmed = line.trim();
          if (!trimmed.startsWith('data: ')) {
            continue;
          }

          const json = trimmed.slice(6);
          if (json === '[DONE]') {
            continue;
          }

          handleEvent(JSON.parse(json) as SSEEvent, assistantMessage.id);
        }
      }
    } catch (error) {
      if (error instanceof DOMException && error.name === 'AbortError') {
        return;
      }
      patchAssistantMessage(assistantMessage.id, (item) => {
        item.content = item.content || '请求失败，请稍后重试';
      });
    } finally {
      streaming.value = false;
      abortController = null;
    }
  }

  function stop() {
    abortController?.abort();
  }

  function handleEvent(event: SSEEvent, assistantId: string) {
    if (event.type === 'text') {
      patchAssistantMessage(assistantId, (item) => {
        item.content += event.content;
      });
      return;
    }

    if (event.type === 'error') {
      patchAssistantMessage(assistantId, (item) => {
        item.content = item.content || event.message;
      });
    }
  }

  function patchAssistantMessage(id: string, patcher: (message: ChatMessage) => void) {
    const target = messages.value.find((item) => item.id === id);
    if (target) {
      patcher(target);
    }
  }

  return {
    messages,
    streaming,
    sendMessage,
    stop,
  };
}
