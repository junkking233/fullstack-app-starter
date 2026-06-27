<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  DataAnalysis,
  TrendCharts,
  Monitor,
  Management,
  Setting,
  List,
  UserFilled,
  SwitchButton,
} from '@element-plus/icons-vue';
import { clearAuthState, getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = getCurrentUser();

const menuItems = [
  { index: '/admin/dashboard', title: '运营总览', icon: DataAnalysis },
  { index: '/admin/analytics', title: '数据分析', icon: TrendCharts },
  { index: '/admin/datascreen', title: '数据大屏', icon: Monitor },
  { index: '/admin/management', title: '客户管理', icon: Management },
  { index: '/admin/visuallist', title: '可视化列表', icon: List },
  { index: '/admin/settings', title: '系统设置', icon: Setting },
];

const pageTitle = computed(() => {
  const item = menuItems.find((m) => m.index === route.path);
  return item?.title ?? '管理控制台';
});

function logout() {
  clearAuthState();
  router.push('/login');
}
</script>

<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="sidebar-brand" @click="router.push('/admin/dashboard')">
        <h2 class="brand-title">管理控制台</h2>
        <span class="brand-sub">Admin Console</span>
      </div>

      <div class="sidebar-user">
        <el-avatar :size="36" class="user-avatar">
          <el-icon><UserFilled /></el-icon>
        </el-avatar>
        <div class="user-info">
          <strong>{{ currentUser?.nickname || '管理员' }}</strong>
        </div>
      </div>

      <el-menu :default-active="route.path" class="admin-menu" router>
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>

      <div class="sidebar-bottom">
        <el-button :icon="SwitchButton" text class="logout-btn" @click="logout">
          退出登录
        </el-button>
      </div>
    </aside>

    <section class="admin-main">
      <header class="admin-header">
        <h2 class="header-title">{{ pageTitle }}</h2>
        <div class="header-right">
          <span class="admin-name">{{ currentUser?.nickname }}</span>
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
  grid-template-columns: 240px minmax(0, 1fr);
  min-height: 100vh;
  background: var(--c-bg);
}

.admin-sidebar {
  position: sticky;
  top: 0;
  display: flex;
  height: 100vh;
  padding: 20px 12px;
  flex-direction: column;
  background: var(--c-ink);
  border-right: 1px solid rgb(255 255 255 / 6%);
}

.sidebar-brand {
  padding: 4px 10px 16px;
  border-bottom: 1px solid rgb(255 255 255 / 8%);
  cursor: pointer;
}

.brand-title {
  margin: 0;
  font-size: 18px;
  font-weight: 800;
  color: #f8fafc;
  letter-spacing: -0.3px;
}

.brand-sub {
  display: block;
  margin-top: 2px;
  color: #94a3b8;
  font-size: 11px;
  font-weight: 500;
  letter-spacing: 0.5px;
}

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 10px;
  border-bottom: 1px solid rgb(255 255 255 / 6%);
}

.user-avatar {
  background: linear-gradient(135deg, var(--c-primary), var(--c-primary-dark));
  color: #fff;
}

.user-info strong {
  color: #f1f5f9;
  font-size: 14px;
  font-weight: 600;
}

.admin-menu {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  background: transparent;
  border-right: 0;
  padding-top: 8px;
}

.admin-menu :deep(.el-menu-item) {
  height: 44px;
  margin-bottom: 2px;
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
  background: linear-gradient(135deg, rgb(47 107 255 / 25%), rgb(47 107 255 / 8%));
  box-shadow: inset 3px 0 0 var(--c-primary);
  font-weight: 600;
}

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
}

.logout-btn:hover {
  background: rgb(239 68 68 / 10%);
}

.admin-main {
  min-width: 0;
  padding: 24px 28px;
}

.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 22px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--c-line);
}

.header-title {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: var(--c-ink);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.admin-name {
  color: var(--c-muted);
  font-size: 14px;
}

.admin-content {
  min-width: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.2s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateX(-6px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(6px);
}

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
    gap: 6px;
  }

  .sidebar-brand,
  .sidebar-user {
    display: none;
  }

  .admin-menu {
    display: flex;
    flex: 1 1 auto;
    min-width: 0;
    gap: 4px;
    padding-top: 0;
  }

  .admin-menu :deep(.el-menu-item) {
    flex: 1;
    justify-content: center;
    padding: 0 10px !important;
    margin-bottom: 0;
  }

  .sidebar-bottom {
    display: none;
  }
}
</style>
