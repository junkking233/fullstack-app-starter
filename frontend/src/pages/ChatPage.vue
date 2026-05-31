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
