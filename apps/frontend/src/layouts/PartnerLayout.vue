<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { Bell, OfficeBuilding } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();

const navItems = [
  { label: '总览', path: '/partner/dashboard' },
  { label: '工单', path: '/partner/tasks' },
  { label: '资源', path: '/partner/resources' },
  { label: '报表', path: '/partner/reports' },
];
</script>

<template>
  <div class="partner-layout">
    <header class="partner-header">
      <div class="header-inner">
        <div class="brand" @click="router.push('/partner/dashboard')">
          <div class="brand-mark">
            <svg viewBox="0 0 40 40" fill="none">
              <rect width="40" height="40" rx="10" fill="url(#partnerGrad)" />
              <path d="M10 30V18l10-6 10 6v12" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
              <defs>
                <linearGradient id="partnerGrad" x1="0" y1="0" x2="40" y2="40">
                  <stop stop-color="#f59e0b" />
                  <stop offset="1" stop-color="#f97316" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div class="brand-text">
            <div class="brand-title">服务方平台</div>
            <div class="brand-subtitle">Partner Hub</div>
          </div>
        </div>

        <el-menu :default-active="route.path" mode="horizontal" class="partner-menu" router>
          <el-menu-item v-for="item in navItems" :key="item.path" :index="item.path">
            {{ item.label }}
          </el-menu-item>
        </el-menu>

        <div class="header-actions">
          <el-badge :value="3" class="notice-badge">
            <el-button :icon="Bell" circle class="action-btn" />
          </el-badge>
          <el-button type="primary" :icon="OfficeBuilding" class="login-btn" @click="router.push('/login')">
            机构入口
          </el-button>
        </div>
      </div>
    </header>

    <main class="partner-main">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <footer class="partner-footer">
      <div class="footer-inner">
        <div class="footer-left">
          <span class="footer-brand">服务方平台</span>
          <span class="footer-divider">|</span>
          <span>合作伙伴工作台</span>
        </div>
        <div class="footer-right">
          <span>技术支持：Fullstack App Starter</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.partner-layout {
  display: flex;
  min-height: 100vh;
  flex-direction: column;
  background:
    radial-gradient(ellipse 70% 50% at 5% 0%, rgb(245 158 11 / 6%), transparent),
    radial-gradient(ellipse 50% 40% at 95% 100%, rgb(249 115 22 / 4%), transparent),
    var(--c-bg);
}

.partner-header {
  position: sticky;
  z-index: 50;
  top: 0;
  background: rgb(255 255 255 / 82%);
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
  filter: drop-shadow(0 2px 4px rgb(245 158 11 / 30%));
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

.partner-menu {
  flex: 1 1 auto;
  min-width: 0;
  max-width: 480px;
  margin: 0 20px;
  border-bottom: 0;
  justify-content: center;
  background: transparent;
}

.partner-menu :deep(.el-menu-item) {
  height: 48px;
  font-size: 15px;
  font-weight: 500;
  color: var(--c-body);
  border-bottom-width: 2.5px;
}

.partner-menu :deep(.el-menu-item:hover) {
  color: var(--c-amber);
  background: transparent;
}

.partner-menu :deep(.el-menu-item.is-active) {
  color: var(--c-amber);
  border-bottom-color: var(--c-amber);
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
  color: var(--c-amber);
  background: var(--c-amber-bg);
  border-color: rgb(245 158 11 / 20%);
  transform: translateY(-1px);
}

.notice-badge :deep(.el-badge__content) {
  top: 4px;
  right: 4px;
  box-shadow: 0 0 0 2px #fff;
}

.login-btn {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 20px;
  background: linear-gradient(135deg, #f59e0b, #f97316);
  box-shadow: 0 2px 6px rgb(245 158 11 / 30%);
}

.login-btn:hover {
  background: linear-gradient(135deg, #fbbf24, #f59e0b);
  box-shadow: 0 4px 12px rgb(245 158 11 / 35%);
}

.partner-main {
  flex: 1;
  min-width: 0;
}

.partner-footer {
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

@media (max-width: 860px) {
  .header-inner {
    flex-wrap: wrap;
    padding: 10px 16px;
    gap: 6px;
    min-height: auto;
  }

  .partner-menu {
    order: 3;
    width: 100%;
    max-width: none;
    margin: 0;
    overflow-x: auto;
  }

  .footer-inner {
    flex-direction: column;
    gap: 6px;
    text-align: center;
  }
}
</style>
