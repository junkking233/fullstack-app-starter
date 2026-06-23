<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock, UserFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';

const router = useRouter();

const formRef = ref();
const form = ref({ username: '', password: '', nickname: '' });
const loading = ref(false);

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度为3-50个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 64, message: '密码长度为6-64个字符', trigger: 'blur' },
  ],
};

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  loading.value = true;
  try {
    await authApi.register({
      username: form.value.username,
      password: form.value.password,
      nickname: form.value.nickname || undefined,
    });
    ElMessage.success('注册成功，请登录');
    await router.push('/login');
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '注册失败');
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-brand">
        <h1>知问社区</h1>
        <p>创建您的账号</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" class="register-form" @submit.prevent="handleRegister">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            :prefix-icon="User"
            placeholder="用户名（3-50个字符）"
            size="large"
            maxlength="50"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            :prefix-icon="Lock"
            type="password"
            show-password
            placeholder="密码（6-64个字符）"
            size="large"
            maxlength="64"
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
            v-model="form.nickname"
            :prefix-icon="UserFilled"
            placeholder="昵称（选填）"
            size="large"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="submit-btn"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>

      <div class="register-footer">
        已有账号？<router-link to="/login">去登录</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
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

.register-card {
  width: 100%;
  max-width: 420px;
  padding: 44px 36px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

.register-brand {
  text-align: center;
  margin-bottom: 32px;
}

.register-brand h1 {
  margin: 0 0 6px;
  font-size: 26px;
  font-weight: 800;
  color: var(--c-primary);
  letter-spacing: -0.5px;
}

.register-brand p {
  margin: 0;
  color: var(--c-muted);
  font-size: 14px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: var(--radius-md);
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: var(--radius-md);
}

.register-footer {
  text-align: center;
  margin-top: 16px;
  padding-top: 18px;
  color: var(--c-muted);
  font-size: 14px;
  border-top: 1px dashed var(--c-line);
}

.register-footer a {
  color: var(--c-primary);
  text-decoration: none;
  font-weight: 500;
}

.register-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .register-card {
    padding: 28px 20px;
  }
}
</style>
