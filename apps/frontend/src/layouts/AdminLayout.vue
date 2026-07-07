<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { SwitchButton, UserFilled } from '@element-plus/icons-vue';
import { clearAuthState, getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = getCurrentUser();

function logout() {
  clearAuthState();
  router.push('/login');
}
</script>

<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand" @click="router.push('/admin/users')">
        <h2>管理后台</h2>
        <span>Minimal Admin</span>
      </div>

      <el-menu :default-active="route.path" router class="admin-menu">
        <el-menu-item index="/admin/users">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-bottom">
        <div class="admin-name">{{ currentUser?.nickname || currentUser?.username || '管理员' }}</div>
        <el-button :icon="SwitchButton" text @click="logout">退出</el-button>
      </div>
    </aside>

    <main class="admin-main">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.admin-layout {
  display: grid;
  grid-template-columns: 224px minmax(0, 1fr);
  min-height: 100vh;
  background: var(--c-bg);
}

.admin-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px 12px;
  background: var(--c-surface);
  border-right: 1px solid var(--c-line);
}

.sidebar-brand {
  padding: 8px 10px 18px;
  border-bottom: 1px solid var(--c-line);
  cursor: pointer;
}

.sidebar-brand h2 {
  margin: 0;
  color: var(--c-ink);
  font-size: 18px;
}

.sidebar-brand span {
  display: block;
  margin-top: 4px;
  color: var(--c-muted);
  font-size: 12px;
}

.admin-menu {
  flex: 1;
  border-right: 0;
}

.admin-menu :deep(.el-menu-item) {
  border-radius: var(--radius-md);
}

.sidebar-bottom {
  display: grid;
  gap: 8px;
  padding: 12px 10px 0;
  border-top: 1px solid var(--c-line);
}

.admin-name {
  color: var(--c-muted);
  font-size: 13px;
}

.admin-main {
  min-width: 0;
  padding: 28px;
}

@media (max-width: 760px) {
  .admin-layout {
    grid-template-columns: 1fr;
  }

  .admin-sidebar {
    position: sticky;
    z-index: 10;
    top: 0;
  }
}
</style>
