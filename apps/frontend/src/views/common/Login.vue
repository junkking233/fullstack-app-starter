<script setup lang="ts">
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { User, Lock } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { setAuthState } from '@/utils/auth';

const route = useRoute();
const router = useRouter();

const formRef = ref();
const form = ref({ username: '', password: '' });
const loading = ref(false);

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  loading.value = true;
  try {
    const result = await authApi.login({
      username: form.value.username,
      password: form.value.password,
    });

    setAuthState(result.token, result.user, result.expiresAt);

    if (result.user.role === 'ADMIN') {
      ElMessage.success('登录成功，欢迎管理员');
      await router.push('/admin/dashboard');
    } else if (result.user.role === 'PARTNER') {
      ElMessage.success('登录成功，欢迎服务方');
      await router.push('/partner/dashboard');
    } else {
      ElMessage.success('登录成功');
      const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/portal/home';
      await router.push(redirect);
    }
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '登录失败');
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-brand">
        <h1>智享门户</h1>
        <p>一站式服务平台</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" class="login-form" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            :prefix-icon="User"
            placeholder="请输入用户名"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            :prefix-icon="Lock"
            type="password"
            show-password
            placeholder="请输入密码"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="submit-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <router-link to="/register" class="register-link">还没有账号？立即注册</router-link>
      </div>

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
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  background:
    radial-gradient(ellipse 60% 50% at 30% 20%, rgb(47 107 255 / 6%), transparent),
    radial-gradient(ellipse 50% 40% at 70% 80%, rgb(47 107 255 / 4%), transparent),
    var(--c-bg);
}

.login-card {
  width: 100%;
  max-width: 420px;
  padding: 44px 36px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

.login-brand {
  text-align: center;
  margin-bottom: 32px;
}

.login-brand h1 {
  margin: 0 0 6px;
  font-size: 26px;
  font-weight: 800;
  color: var(--c-primary);
  letter-spacing: -0.5px;
}

.login-brand p {
  margin: 0;
  color: var(--c-muted);
  font-size: 14px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-md);
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: var(--radius-md);
}

.login-footer {
  text-align: center;
  margin-bottom: 20px;
}

.register-link {
  color: var(--c-primary);
  font-size: 14px;
  text-decoration: none;
}

.register-link:hover {
  text-decoration: underline;
}

.demo-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 6px;
  padding-top: 18px;
  color: var(--c-muted);
  font-size: 12px;
  border-top: 1px dashed var(--c-line);
}

.demo-hint strong {
  color: var(--c-ink-light);
}

.hint-divider {
  color: var(--c-line);
}

@media (max-width: 480px) {
  .login-card {
    padding: 28px 20px;
  }
}
</style>
