<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Lock, User } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { hasRoutePermission, setAuthState } from '@/utils/auth';

const route = useRoute();
const router = useRouter();

const activeRole = ref<'portal' | 'partner' | 'admin'>('portal');
const form = ref({ username: '', password: '' });
const loading = ref(false);
const remember = ref(false);

const roleOptions = [
  {
    label: '普通用户',
    value: 'portal' as const,
    color: '#0d9488',
    gradient: 'linear-gradient(135deg, #0d9488, #3b82f6)',
    path: '/portal/home',
    desc: '门户服务平台',
    roles: ['ADMIN', 'PARTNER', 'USER'],
  },
  {
    label: '服务方',
    value: 'partner' as const,
    color: '#f59e0b',
    gradient: 'linear-gradient(135deg, #f59e0b, #f97316)',
    path: '/partner/dashboard',
    desc: '合作伙伴工作台',
    roles: ['ADMIN', 'PARTNER'],
  },
  {
    label: '管理员',
    value: 'admin' as const,
    color: '#3b82f6',
    gradient: 'linear-gradient(135deg, #3b82f6, #1d4ed8)',
    path: '/admin/dashboard',
    desc: '系统管理平台',
    roles: ['ADMIN'],
  },
];

const currentRole = computed(() => roleOptions.find((r) => r.value === activeRole.value)!);

async function handleLogin() {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码');
    return;
  }
  loading.value = true;
  try {
    const result = await authApi.login({
      username: form.value.username,
      password: form.value.password,
    });

    if (!currentRole.value.roles.includes(result.user.role)) {
      ElMessage.error(`当前账号无权进入${currentRole.value.label}入口`);
      return;
    }

    setAuthState(result.token, result.user, result.expiresAt);
    loading.value = false;
    ElMessage.success('登录成功');
    const redirect = typeof route.query.redirect === 'string' && hasRoutePermission(route.query.redirect, result.user.role)
      ? route.query.redirect
      : currentRole.value.path;
    await router.push(redirect);
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="login-bg">
      <div class="bg-orb orb-1" />
      <div class="bg-orb orb-2" />
      <div class="bg-orb orb-3" />
    </div>

    <div class="login-card">
      <!-- Logo -->
      <div class="login-brand">
        <div class="brand-icon">
          <svg viewBox="0 0 48 48" fill="none">
            <rect width="48" height="48" rx="14" fill="url(#loginGrad)" />
            <path d="M14 34V20l10 8 10-8v14" stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" />
            <defs>
              <linearGradient id="loginGrad" x1="0" y1="0" x2="48" y2="48">
                <stop stop-color="#0d9488" />
                <stop offset="1" stop-color="#3b82f6" />
              </linearGradient>
            </defs>
          </svg>
        </div>
        <h2>Fullstack App Starter</h2>
        <p>门户 + 后台管理脚手架</p>
      </div>

      <!-- 角色切换 -->
      <div class="role-tabs">
        <button
          v-for="r in roleOptions"
          :key="r.value"
          class="role-tab"
          :class="{ active: activeRole === r.value }"
          :style="activeRole === r.value ? {
            background: r.gradient,
            color: '#fff',
            boxShadow: `0 4px 14px ${r.color}40`,
          } : {}"
          @click="activeRole = r.value"
        >
          {{ r.label }}
        </button>
      </div>

      <p class="role-desc">{{ currentRole.desc }}</p>

      <!-- 表单 -->
      <el-form :model="form" class="login-form">
        <el-form-item>
          <el-input v-model="form.username" :prefix-icon="User" placeholder="请输入用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" :prefix-icon="Lock" type="password" show-password placeholder="请输入密码" size="large" />
        </el-form-item>
        <div class="form-options">
          <el-checkbox v-model="remember">记住我</el-checkbox>
          <el-button type="primary" link size="small">忘记密码？</el-button>
        </div>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="submit-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 演示账号 -->
      <div class="demo-hint">
        <el-tag size="small" effect="light" round>演示账号</el-tag>
        <span>管理员 <strong>admin</strong> / <strong>admin123</strong></span>
        <span class="hint-divider">·</span>
        <span>服务方 <strong>partner</strong> / <strong>123456</strong></span>
        <span class="hint-divider">·</span>
        <span>用户 <strong>user</strong> / <strong>123456</strong></span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  overflow: hidden;
  background:
    radial-gradient(ellipse 80% 60% at 15% 10%, rgb(13 148 136 / 8%), transparent),
    radial-gradient(ellipse 60% 50% at 85% 90%, rgb(59 130 246 / 6%), transparent),
    var(--c-bg);
}

.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.4;
}

.orb-1 {
  width: 500px;
  height: 500px;
  top: -200px;
  left: -100px;
  background: rgb(13 148 136 / 12%);
}

.orb-2 {
  width: 400px;
  height: 400px;
  bottom: -150px;
  right: -80px;
  background: rgb(59 130 246 / 10%);
}

.orb-3 {
  width: 300px;
  height: 300px;
  top: 40%;
  left: 50%;
  background: rgb(245 158 11 / 6%);
}

.login-card {
  position: relative;
  width: 100%;
  max-width: 440px;
  padding: 40px 36px;
  background: rgb(255 255 255 / 92%);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

/* Brand */
.login-brand {
  text-align: center;
  margin-bottom: 28px;
}

.brand-icon {
  width: 52px;
  height: 52px;
  margin: 0 auto 14px;
}

.brand-icon svg {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4px 10px rgb(13 148 136 / 30%));
}

.login-brand h2 {
  margin: 0 0 6px;
  font-size: 22px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.login-brand p {
  margin: 0;
  color: var(--c-muted);
  font-size: 14px;
}

/* Role Tabs */
.role-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.role-tab {
  flex: 1;
  height: 42px;
  color: var(--c-muted);
  font-size: 14px;
  font-weight: 600;
  background: var(--c-bg);
  border: 1.5px solid var(--c-line);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.role-tab:hover:not(.active) {
  border-color: var(--c-primary);
  color: var(--c-primary);
}

.role-desc {
  text-align: center;
  margin: 0 0 20px;
  color: var(--c-muted-light);
  font-size: 12px;
}

/* Form */
.login-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-md);
  padding: 4px 12px;
}

.login-form :deep(.el-input__inner) {
  height: 42px;
}

.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: -4px 0 16px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 700;
  border-radius: var(--radius-md);
  letter-spacing: 2px;
}

/* Demo Hint */
.demo-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 24px;
  padding-top: 20px;
  color: var(--c-muted);
  font-size: 12px;
  border-top: 1px dashed var(--c-line);
}

.demo-hint strong {
  color: var(--c-ink-light);
  font-weight: 600;
}

.hint-divider {
  color: var(--c-line);
}

@media (max-width: 560px) {
  .login-card {
    padding: 28px 20px;
  }
}
</style>
