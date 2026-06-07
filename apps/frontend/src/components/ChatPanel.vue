<script setup lang="ts">
import { nextTick, ref, watch } from 'vue';
import { CircleClose, Promotion } from '@element-plus/icons-vue';
import type { ChatMessage } from '@/types/chat';

const props = defineProps<{
  messages: ChatMessage[];
  streaming: boolean;
}>();

const emit = defineEmits<{
  send: [content: string];
  stop: [];
}>();

const input = ref('');
const bottomRef = ref<HTMLElement>();

watch(
  () => props.messages.map((message) => message.content).join('|'),
  async () => {
    await nextTick();
    bottomRef.value?.scrollIntoView({ behavior: 'smooth' });
  },
);

function submit() {
  const content = input.value.trim();
  if (!content || props.streaming) {
    return;
  }
  input.value = '';
  emit('send', content);
}
</script>

<template>
  <section class="chat-panel">
    <div class="chat-messages">
      <div
        v-for="message in messages"
        :key="message.id"
        class="message"
        :class="message.role"
      >
        <div class="message-bubble">
          <div class="message-text">{{ message.content || (message.role === 'assistant' ? '...' : '') }}</div>
        </div>
      </div>

      <div ref="bottomRef" />
    </div>

    <div class="chat-input-area">
      <el-input
        v-model="input"
        type="textarea"
        :rows="3"
        resize="none"
        :disabled="streaming"
        placeholder="输入消息，DeepSeek 会流式回复"
        @keydown.enter.exact.prevent="submit"
      />
      <div class="chat-actions">
        <el-button v-if="streaming" type="danger" :icon="CircleClose" @click="emit('stop')">
          停止
        </el-button>
        <el-button v-else type="primary" :icon="Promotion" :disabled="!input.trim()" @click="submit">
          发送
        </el-button>
      </div>
    </div>
  </section>
</template>
