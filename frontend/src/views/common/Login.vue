<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Lock, User } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { hasRoutePermission, setAuthState } from '@/utils/auth';

const route = useRoute();
const router = useRouter();

const activeRole = ref<'portal' | 'admin'>('portal');
const form = ref({ username: '', password: '' });
const registerForm = ref({ username: '', password: '', email: '', phone: '' });
const loading = ref(false);
const remember = ref(false);
const registerVisible = ref(false);

const roleOptions = [
  {
    label: '普通用户',
    value: 'portal' as const,
    color: '#0d9488',
    gradient: 'linear-gradient(135deg, #0d9488, #3b82f6)',
    path: '/portal/home',
    desc: '门户服务平台',
    roles: ['ADMIN', 'USER'],
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

    setAuthState(result.token, result.user, result.expiresAt, remember.value);
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

async function handleRegister() {
  if (!registerForm.value.username || !registerForm.value.password || !registerForm.value.email) {
    ElMessage.warning('请填写用户名、密码和邮箱');
    return;
  }
  await authApi.register(registerForm.value);
  ElMessage.success('注册成功，请登录');
  form.value.username = registerForm.value.username;
  registerVisible.value = false;
}
</script>

<template>
  <div class="login-page">
    <section class="login-intro">
      <span>WORLD CUP 2026</span>
      <h1>赛事数据、赛程维护与球迷互动的一体化系统</h1>
      <p>面向游客、注册用户和管理员，覆盖球队、赛程、积分榜、淘汰赛、收藏和评论审核。</p>
    </section>
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
        <h2>世界杯赛事信息系统</h2>
        <p>FIFA World Cup 2026</p>
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
          <el-button type="primary" link size="small" @click="registerVisible = true">注册账号</el-button>
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
        <span>用户 <strong>user</strong> / <strong>123456</strong></span>
      </div>
    </div>

    <el-dialog v-model="registerVisible" title="注册普通用户" width="460px">
      <el-form :model="registerForm" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="registerForm.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="registerForm.password" type="password" show-password /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="registerForm.email" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="registerForm.phone" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="registerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegister">注册</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  display: grid;
  grid-template-columns: minmax(360px, 620px) 440px;
  gap: 44px;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 42px;
  overflow: hidden;
  background: linear-gradient(135deg, #f0fdfa 0%, #f8fafc 40%, #e0f2fe 100%);
}

.login-intro {
  position: relative;
  z-index: 1;
  color: var(--c-ink);
}

.login-intro span {
  color: var(--c-primary);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.14em;
}

.login-intro h1 {
  max-width: 620px;
  margin: 16px 0 18px;
  font-size: 48px;
  line-height: 1.12;
}

.login-intro p {
  max-width: 560px;
  margin: 0;
  color: var(--c-body);
  font-size: 16px;
  line-height: 1.8;
}

.login-card {
  position: relative;
  width: 100%;
  max-width: 440px;
  padding: 40px 36px;
  background: #fff;
  border: 1px solid var(--c-line);
  border-radius: 18px;
  box-shadow: var(--shadow-xl);
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
  .login-page {
    grid-template-columns: 1fr;
    padding: 24px;
  }
  .login-intro {
    display: none;
  }
  .login-card {
    padding: 28px 20px;
  }
}
</style>
