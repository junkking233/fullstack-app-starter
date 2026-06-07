<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { DataAnalysis, DocumentChecked, Flag, ChatLineRound, Histogram, List, SwitchButton, Trophy, UserFilled } from '@element-plus/icons-vue';
import { clearAuthState, useCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = useCurrentUser();

const menuItems = [
  { index: '/admin/dashboard', title: '后台概览', icon: DataAnalysis },
  { index: '/admin/teams', title: '球队管理', icon: Flag },
  { index: '/admin/matches', title: '赛程管理', icon: List },
  { index: '/admin/standings', title: '积分榜管理', icon: Trophy },
  { index: '/admin/comments', title: '评论审核', icon: ChatLineRound },
  { index: '/admin/analytics', title: '图表统计', icon: Histogram },
  { index: '/admin/data-maintenance', title: '数据维护', icon: DocumentChecked },
  { index: '/admin/users', title: '用户管理', icon: UserFilled },
];

const pageTitle = computed(() => menuItems.find((m) => m.index === route.path)?.title ?? '管理后台');

function logout() {
  clearAuthState();
  router.push('/login');
}
</script>

<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand" @click="router.push('/admin/dashboard')">
        <div class="brand-icon">26</div>
        <div>
          <div class="brand-title">世界杯后台</div>
          <div class="brand-sub">Admin Console</div>
        </div>
      </div>
      <div class="sidebar-user">
        <el-avatar :size="38"><el-icon><UserFilled /></el-icon></el-avatar>
        <div>
          <strong>{{ currentUser?.username || '管理员' }}</strong>
          <span>{{ currentUser?.email || 'admin@example.com' }}</span>
        </div>
      </div>
      <el-menu :default-active="route.path" class="admin-menu" router>
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
      <el-button :icon="SwitchButton" text type="danger" class="logout-btn" @click="logout">退出登录</el-button>
    </aside>
    <section class="admin-main">
      <header class="admin-header">
        <div>
          <span>World Cup 2026</span>
          <h2>{{ pageTitle }}</h2>
        </div>
      </header>
      <div class="admin-content">
        <router-view />
      </div>
    </section>
  </div>
</template>

<style scoped>
.admin-layout {
  display: grid;
  grid-template-columns: 252px minmax(0, 1fr);
  min-height: 100vh;
  background: var(--c-bg);
}

.admin-sidebar {
  display: flex;
  flex-direction: column;
  gap: 14px;
  height: 100vh;
  padding: 20px 14px;
  background: #fff;
  border-right: 1px solid var(--c-line);
  box-shadow: 4px 0 24px rgb(15 23 42 / 6%);
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 10px 18px;
  color: var(--c-ink);
  border-bottom: 1px solid var(--c-line);
  cursor: pointer;
}

.brand-icon {
  display: grid;
  width: 38px;
  height: 38px;
  place-items: center;
  background: linear-gradient(135deg, #facc15, #0d9488 58%, #2563eb);
  border-radius: 8px;
  font-weight: 800;
}

.brand-title {
  font-weight: 700;
}

.brand-sub {
  color: var(--c-muted);
  font-size: 11px;
}

.sidebar-user {
  display: flex;
  gap: 10px;
  align-items: center;
  padding: 14px 12px;
  color: var(--c-body);
  background: var(--c-bg);
  border: 1px solid var(--c-line);
  border-radius: 10px;
}

.sidebar-user div {
  display: flex;
  min-width: 0;
  flex-direction: column;
}

.sidebar-user span {
  color: var(--c-muted);
  font-size: 12px;
}

.admin-menu {
  flex: 1;
  background: transparent;
  border-right: 0;
}

.admin-menu :deep(.el-menu-item) {
  height: 44px;
  margin-bottom: 4px;
  color: var(--c-body);
  border-radius: 8px;
}

.admin-menu :deep(.el-menu-item.is-active),
.admin-menu :deep(.el-menu-item:hover) {
  color: var(--c-primary);
  background: var(--c-primary-bg);
  box-shadow: none;
}

.logout-btn {
  align-self: stretch;
}

.admin-main {
  min-width: 0;
}

.admin-header {
  display: flex;
  align-items: center;
  height: 74px;
  padding: 0 30px;
  background: rgb(255 255 255 / 78%);
  border-bottom: 1px solid rgb(226 232 240 / 78%);
  backdrop-filter: blur(14px);
}

.admin-header span {
  color: #64748b;
  font-size: 12px;
}

.admin-header h2 {
  margin: 4px 0 0;
  color: #0f172a;
  font-size: 22px;
}

.admin-content {
  padding: 26px 30px 38px;
}
</style>
