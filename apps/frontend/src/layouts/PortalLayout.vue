<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { MessageBox, Search, User } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();

const navItems = [
  { label: '首页', path: '/portal/home' },
  { label: '服务大厅', path: '/portal/services' },
  { label: '我的业务', path: '/portal/my-business' },
  { label: '个人中心', path: '/portal/profile' },
];
</script>

<template>
  <div class="portal-layout">
    <!-- 顶部导航 -->
    <header class="portal-header">
      <div class="header-inner">
        <div class="brand" @click="router.push('/portal/home')">
          <div class="brand-mark">
            <svg viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect width="40" height="40" rx="10" fill="url(#portalGrad)" />
              <path d="M12 28V14l8 7 8-7v14" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
              <defs>
                <linearGradient id="portalGrad" x1="0" y1="0" x2="40" y2="40">
                  <stop stop-color="#0d9488" />
                  <stop offset="1" stop-color="#3b82f6" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div class="brand-text">
            <div class="brand-title">智享门户</div>
            <div class="brand-subtitle">Smart Portal</div>
          </div>
        </div>

        <el-menu :default-active="route.path" mode="horizontal" class="portal-menu" router>
          <el-menu-item v-for="item in navItems" :key="item.path" :index="item.path">
            {{ item.label }}
          </el-menu-item>
        </el-menu>

        <div class="header-actions">
          <el-tooltip content="搜索" placement="bottom">
            <el-button :icon="Search" circle class="action-btn" />
          </el-tooltip>
          <el-tooltip content="消息" placement="bottom">
            <el-button :icon="MessageBox" circle class="action-btn" />
          </el-tooltip>
          <el-button type="primary" :icon="User" class="login-btn" @click="router.push('/login')">
            登录
          </el-button>
        </div>
      </div>
    </header>

    <!-- 页面内容 -->
    <main class="portal-main">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 页脚 -->
    <footer class="portal-footer">
      <div class="footer-inner">
        <div class="footer-left">
          <span class="footer-brand">智享门户</span>
          <span class="footer-divider">|</span>
          <span>一站式服务平台</span>
        </div>
        <div class="footer-right">
          <span>技术支持：Fullstack App Starter</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.portal-layout {
  display: flex;
  min-height: 100vh;
  flex-direction: column;
  background:
    radial-gradient(ellipse 80% 60% at 10% 0%, rgb(13 148 136 / 6%), transparent),
    radial-gradient(ellipse 60% 50% at 90% 100%, rgb(59 130 246 / 5%), transparent),
    var(--c-bg);
}

/* Header */
.portal-header {
  position: sticky;
  z-index: 50;
  top: 0;
  background: rgb(255 255 255 / 80%);
  border-bottom: 1px solid var(--c-line);
  backdrop-filter: blur(20px) saturate(1.8);
  -webkit-backdrop-filter: blur(20px) saturate(1.8);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 28px;
  min-height: 68px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: opacity var(--transition-fast);
}

.brand:hover {
  opacity: 0.85;
}

.brand-mark {
  width: 40px;
  height: 40px;
}

.brand-mark svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 2px 4px rgb(13 148 136 / 25%));
}

.brand-title {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: -0.3px;
  color: var(--c-ink);
}

.brand-subtitle {
  margin-top: 1px;
  color: var(--c-muted-light);
  font-size: 11px;
  font-weight: 500;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.portal-menu {
  flex: 1 1 auto;
  min-width: 0;
  max-width: 480px;
  margin: 0 20px;
  border-bottom: 0;
  justify-content: center;
  background: transparent;
}

.portal-menu :deep(.el-menu-item) {
  height: 48px;
  font-size: 15px;
  font-weight: 500;
  color: var(--c-body);
  border-bottom-width: 2.5px;
  transition: all var(--transition-fast);
}

.portal-menu :deep(.el-menu-item:hover) {
  color: var(--c-primary);
  background: transparent;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.action-btn {
  color: var(--c-muted);
  background: var(--c-line-light);
  border: 1px solid var(--c-line);
  transition: all var(--transition-fast);
}

.action-btn:hover {
  color: var(--c-primary);
  background: var(--c-primary-bg);
  border-color: rgb(13 148 136 / 20%);
  transform: translateY(-1px);
}

.login-btn {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 20px;
}

/* Main */
.portal-main {
  flex: 1;
  min-width: 0;
}

/* Footer */
.portal-footer {
  padding: 20px 28px;
  background: var(--c-surface);
  border-top: 1px solid var(--c-line);
}

.footer-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1280px;
  margin: 0 auto;
  color: var(--c-muted);
  font-size: 13px;
}

.footer-brand {
  font-weight: 700;
  color: var(--c-ink-light);
}

.footer-divider {
  margin: 0 8px;
  color: var(--c-line);
}

/* Page transition */
.page-enter-active,
.page-leave-active {
  transition: all 0.25s ease;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(12px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* Responsive */
@media (max-width: 860px) {
  .header-inner {
    flex-wrap: wrap;
    padding: 10px 16px;
    gap: 6px;
    min-height: auto;
  }

  .portal-menu {
    order: 3;
    width: 100%;
    max-width: none;
    margin: 0;
    overflow-x: auto;
  }

  .portal-menu :deep(.el-menu--horizontal) {
    justify-content: center;
  }

  .footer-inner {
    flex-direction: column;
    gap: 6px;
    text-align: center;
  }
}
</style>
