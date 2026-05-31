export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
}

export type SSEEvent =
  | { type: 'text'; content: string }
  | { type: 'text_done' }
  | { type: 'error'; message: string }
  | { type: 'done' };
