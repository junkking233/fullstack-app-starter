<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  Bell,
  ChatDotRound,
  DataAnalysis,
  Grid,
  Histogram,
  List,
  Monitor,
  SwitchButton,
  Setting,
  Tickets,
  UserFilled,
} from '@element-plus/icons-vue';
import { clearAuthState, getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = getCurrentUser();

const menuItems = [
  { index: '/admin/screen', title: '数据大屏', icon: Monitor },
  { index: '/admin/dashboard', title: '运营概览', icon: DataAnalysis },
  { index: '/admin/management', title: '数据管理', icon: Tickets },
  { index: '/admin/visual-list', title: '可视化列表', icon: List },
  { index: '/admin/analytics', title: '数据可视化', icon: Histogram },
  { index: '/admin/users', title: '用户管理', icon: UserFilled },
  { index: '/admin/chat', title: 'AI 助手', icon: ChatDotRound },
  { index: '/admin/settings', title: '系统配置', icon: Setting },
];

const pageTitle = computed(() => {
  const item = menuItems.find((m) => m.index === route.path);
  return item?.title ?? '管理后台';
});

function logout() {
  clearAuthState();
  router.push('/login');
}
</script>

<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <!-- Logo -->
      <div class="sidebar-brand" @click="router.push('/admin/dashboard')">
        <div class="brand-icon">
          <svg viewBox="0 0 36 36" fill="none">
            <rect width="36" height="36" rx="9" fill="url(#adminGrad)" />
            <path d="M10 26V14l8 5.5L26 14v12" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
            <defs>
              <linearGradient id="adminGrad" x1="0" y1="0" x2="36" y2="36">
                <stop stop-color="#10BFA3" />
                <stop offset="1" stop-color="#31D18B" />
              </linearGradient>
            </defs>
          </svg>
        </div>
        <div class="brand-text">
          <div class="brand-title">明析管理平台</div>
          <div class="brand-sub">Admin Console</div>
        </div>
      </div>

      <!-- User -->
      <div class="sidebar-user">
        <el-avatar :size="40" class="user-avatar">
          <el-icon><UserFilled /></el-icon>
        </el-avatar>
        <div class="user-info">
          <strong>{{ currentUser?.username || '管理员' }}</strong>
          <span>{{ currentUser?.email || 'admin@example.com' }}</span>
        </div>
      </div>

      <!-- Menu -->
      <el-menu :default-active="route.path" class="admin-menu" router>
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>

      <!-- Logout -->
      <div class="sidebar-bottom">
        <el-button :icon="SwitchButton" text type="danger" class="logout-btn" @click="logout">
          退出登录
        </el-button>
      </div>
    </aside>

    <section class="admin-main">
      <header class="admin-header">
        <div class="header-title">
          <span class="breadcrumb">Admin Console</span>
          <h2>{{ pageTitle }}</h2>
        </div>
        <div class="header-actions">
          <el-button :icon="Bell" circle class="header-icon-btn" />
          <el-button type="primary" :icon="Grid" class="create-btn">快捷操作</el-button>
        </div>
      </header>

      <div class="admin-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
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
  position: sticky;
  top: 0;
  display: flex;
  height: 100vh;
  padding: 20px 14px;
  flex-direction: column;
  background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  border-right: 1px solid rgb(255 255 255 / 6%);
}

/* Brand */
.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 10px 18px;
  border-bottom: 1px solid rgb(255 255 255 / 8%);
  cursor: pointer;
  transition: opacity var(--transition-fast);
}

.sidebar-brand:hover {
  opacity: 0.85;
}

.brand-icon {
  width: 36px;
  height: 36px;
  flex-shrink: 0;
}

.brand-icon svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 2px 8px rgb(16 191 163 / 38%));
}

.brand-title {
  color: #f8fafc;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: -0.2px;
}

.brand-sub {
  color: #94a3b8;
  font-size: 10px;
  font-weight: 500;
  letter-spacing: 0.8px;
  text-transform: uppercase;
  margin-top: 1px;
}

/* User */
.sidebar-user {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px 10px;
  border-bottom: 1px solid rgb(255 255 255 / 6%);
}

.user-avatar {
  background: linear-gradient(135deg, #10bfa3, #31d18b);
  color: #fff;
  box-shadow: 0 2px 8px rgb(16 191 163 / 28%);
}

.user-info {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 2px;
}

.user-info strong {
  color: #f1f5f9;
  font-size: 14px;
  font-weight: 600;
}

.user-info span {
  color: #94a3b8;
  font-size: 12px;
}

/* Menu */
.admin-menu {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  background: transparent;
  border-right: 0;
  padding-top: 8px;
}

.admin-menu :deep(.el-menu-item) {
  height: 46px;
  margin-bottom: 4px;
  padding-left: 14px !important;
  color: #cbd5e1;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.admin-menu :deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 10px;
}

.admin-menu :deep(.el-menu-item:hover) {
  color: #fff;
  background: rgb(255 255 255 / 6%);
}

.admin-menu :deep(.el-menu-item.is-active) {
  color: #fff;
  background: linear-gradient(135deg, rgb(16 191 163 / 22%), rgb(49 209 139 / 10%));
  box-shadow: 0 0 0 1px rgb(16 191 163 / 20%), inset 3px 0 0 #10bfa3;
  font-weight: 600;
}

/* Bottom */
.sidebar-bottom {
  padding-top: 12px;
  border-top: 1px solid rgb(255 255 255 / 6%);
}

.logout-btn {
  width: 100%;
  justify-content: center;
  color: #f87171;
  font-weight: 500;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.logout-btn:hover {
  background: rgb(239 68 68 / 10%);
}

/* Main */
.admin-main {
  min-width: 0;
  padding: 24px 28px;
}

.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 22px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--c-line);
}

.breadcrumb {
  display: block;
  color: var(--c-muted);
  font-size: 12px;
  font-weight: 500;
  letter-spacing: 0.3px;
  text-transform: uppercase;
  margin-bottom: 4px;
}

.admin-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.5px;
  color: var(--c-ink);
}

.header-actions {
  display: flex;
  gap: 10px;
}

.header-icon-btn {
  color: var(--c-muted);
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  transition: all var(--transition-fast);
}

.header-icon-btn:hover {
  color: var(--primary);
  background: var(--primary-soft);
  border-color: rgb(16 191 163 / 20%);
  transform: translateY(-1px);
}

.create-btn {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 18px;
  background: linear-gradient(135deg, #10bfa3, #0a9a82);
  box-shadow: 0 2px 6px rgb(16 191 163 / 25%);
}

.create-btn:hover {
  background: linear-gradient(135deg, #31d18b, #10bfa3);
  box-shadow: 0 4px 12px rgb(16 191 163 / 35%);
}

.admin-content {
  min-width: 0;
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.2s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateX(-8px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(8px);
}

/* Responsive */
@media (max-width: 860px) {
  .admin-layout {
    grid-template-columns: 1fr;
  }

  .admin-sidebar {
    position: relative;
    height: auto;
    padding: 12px;
    flex-direction: row;
    flex-wrap: wrap;
    gap: 8px;
    background: linear-gradient(180deg, #0f172a 0%, #1e293b 100%);
  }

  .sidebar-brand,
  .sidebar-user {
    display: none;
  }

  .admin-menu {
    display: flex;
    flex: 1 1 auto;
    min-width: 0;
    gap: 6px;
    padding-top: 0;
  }

  .admin-menu :deep(.el-menu-item) {
    display: flex;
    justify-content: center;
    margin-bottom: 0;
    padding: 0 14px !important;
    flex: 1;
  }

  .admin-menu :deep(.el-menu-item.is-active) {
    box-shadow: 0 0 0 1px rgb(16 191 163 / 20%);
    border-left: none;
    inset: none;
  }

  .sidebar-bottom {
    display: none;
  }

  .admin-main {
    padding: 14px 16px;
  }

  .admin-header {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }
}
</style>
