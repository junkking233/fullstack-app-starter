<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { SwitchButton, User } from '@element-plus/icons-vue';
import { clearAuthState, useCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = useCurrentUser();

const navItems = [
  { label: '首页', path: '/portal/home' },
  { label: '赛程', path: '/portal/matches' },
  { label: '球队', path: '/portal/teams' },
  { label: '城市场馆', path: '/portal/cities' },
  { label: '积分榜', path: '/portal/standings' },
  { label: '淘汰赛', path: '/portal/bracket' },
  { label: '我的收藏', path: '/portal/favorites' },
  { label: '个人中心', path: '/portal/profile' },
];

function logout() {
  clearAuthState();
  router.push('/portal/home');
}
</script>

<template>
  <div class="portal-layout">
    <header class="portal-header">
      <div class="header-inner">
        <div class="brand" @click="router.push('/portal/home')">
          <div class="brand-mark">26</div>
          <div>
            <div class="brand-title">世界杯赛事信息系统</div>
            <div class="brand-subtitle">FIFA World Cup 2026</div>
          </div>
        </div>
        <el-menu :default-active="route.path" mode="horizontal" class="portal-menu" router>
          <el-menu-item v-for="item in navItems" :key="item.path" :index="item.path">
            {{ item.label }}
          </el-menu-item>
        </el-menu>
        <div class="header-actions">
          <el-button v-if="!currentUser" type="primary" :icon="User" @click="router.push('/login')">登录</el-button>
          <template v-else>
            <span class="user-name">{{ currentUser.username }}</span>
            <el-button :icon="SwitchButton" @click="logout">退出</el-button>
          </template>
        </div>
      </div>
    </header>
    <main class="portal-main">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.portal-layout {
  min-height: 100vh;
  background: var(--c-bg);
}

.portal-header {
  position: sticky;
  top: 0;
  z-index: 20;
  background: rgba(255, 255, 255, 0.92);
  border-bottom: 1px solid var(--c-line);
  box-shadow: var(--shadow-sm);
  backdrop-filter: blur(18px) saturate(1.4);
}

.header-inner {
  display: flex;
  align-items: center;
  gap: 18px;
  max-width: 1280px;
  min-height: 70px;
  margin: 0 auto;
  padding: 0 24px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 220px;
  cursor: pointer;
}

.brand-mark {
  display: grid;
  width: 42px;
  height: 42px;
  place-items: center;
  color: #fff;
  background: linear-gradient(135deg, #facc15, #0d9488 55%, #2563eb);
  border-radius: 8px;
  font-weight: 800;
  box-shadow: 0 10px 24px rgb(13 148 136 / 30%);
}

.brand-title {
  color: var(--c-ink);
  font-size: 17px;
  font-weight: 700;
}

.brand-subtitle {
  color: var(--c-primary);
  font-size: 11px;
}

.portal-menu {
  flex: 1;
  min-width: 0;
  border-bottom: 0;
  background: transparent;
}

.portal-menu :deep(.el-menu-item) {
  color: var(--c-body);
  border-bottom-color: transparent;
}

.portal-menu :deep(.el-menu-item:hover),
.portal-menu :deep(.el-menu-item.is-active) {
  color: var(--c-primary);
  background: var(--c-primary-bg);
  border-bottom-color: var(--c-primary);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  color: var(--c-ink);
  font-size: 14px;
}

.portal-main {
  max-width: 1280px;
  margin: 0 auto;
  padding: 28px 24px 40px;
}
</style>
