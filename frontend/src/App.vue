<script setup lang="ts">
import { computed, ref } from 'vue';
import ChatPage from '@/pages/ChatPage.vue';
import UserList from '@/pages/UserList.vue';

const activeMenu = ref('chat');

const currentView = computed(() => (activeMenu.value === 'users' ? UserList : ChatPage));
</script>

<template>
  <el-container class="app-shell">
    <el-header class="app-header">
      <div class="app-brand">SpringBoot Vue Starter</div>
      <el-menu
        :default-active="activeMenu"
        mode="horizontal"
        class="app-menu"
        @select="activeMenu = $event"
      >
        <el-menu-item index="chat">AI 对话</el-menu-item>
        <el-menu-item index="users">用户管理</el-menu-item>
      </el-menu>
    </el-header>
    <el-main class="app-main">
      <component :is="currentView" />
    </el-main>
  </el-container>
</template>
