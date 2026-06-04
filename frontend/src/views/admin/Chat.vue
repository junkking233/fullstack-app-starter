<script setup lang="ts">
import ChatPanel from '@/components/ChatPanel.vue';
import { useChatSSE } from '@/composables/useChatSSE';

const { messages, streaming, sendMessage, stop } = useChatSSE();
</script>

<template>
  <div class="chat-page">
    <ChatPanel
      :messages="messages"
      :streaming="streaming"
      @send="sendMessage"
      @stop="stop"
    />
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  height: calc(100vh - 172px);
  min-height: 520px;
  overflow: hidden;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
}
</style>
