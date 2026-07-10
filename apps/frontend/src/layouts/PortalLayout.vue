<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { HomeFilled, User } from '@element-plus/icons-vue';
import { getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const currentUser = getCurrentUser();

function handleAccountEntry() {
  if (!currentUser) {
    router.push('/login');
    return;
  }
  if (currentUser.role === 'ADMIN') {
    router.push('/admin/users');
  }
}
</script>

<template>
  <div class="portal-layout">
    <header class="portal-header">
      <div class="header-inner">
        <div class="brand" @click="router.push('/portal/home')">
          <div class="brand-mark">A</div>
          <div>
            <div class="brand-title">业务项目脚手架</div>
            <div class="brand-subtitle">AppScaffold</div>
          </div>
        </div>

        <el-menu :default-active="route.path" mode="horizontal" class="portal-menu" router>
          <el-menu-item index="/portal/home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
        </el-menu>

        <el-button
          type="primary"
          :icon="User"
          :disabled="Boolean(currentUser && currentUser.role !== 'ADMIN')"
          @click="handleAccountEntry"
        >
          {{ !currentUser ? '登录' : currentUser.role === 'ADMIN' ? '进入后台' : '已登录' }}
        </el-button>
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
  z-index: 10;
  top: 0;
  background: rgb(255 255 255 / 88%);
  border-bottom: 1px solid var(--c-line);
  backdrop-filter: blur(16px);
}

.header-inner {
  display: flex;
  align-items: center;
  gap: 28px;
  max-width: 1120px;
  min-height: 64px;
  margin: 0 auto;
  padding: 0 24px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.brand-mark {
  display: grid;
  width: 36px;
  height: 36px;
  place-items: center;
  border-radius: 10px;
  color: #fff;
  font-weight: 800;
  background: linear-gradient(135deg, var(--c-primary), var(--c-primary-dark));
}

.brand-title {
  color: var(--c-ink);
  font-size: 16px;
  font-weight: 800;
}

.brand-subtitle {
  margin-top: 1px;
  color: var(--c-muted);
  font-size: 11px;
}

.portal-menu {
  flex: 1;
  min-width: 0;
  border-bottom: 0;
  background: transparent;
}

.portal-main {
  max-width: 1120px;
  margin: 0 auto;
  padding: 32px 24px 48px;
}
</style>
