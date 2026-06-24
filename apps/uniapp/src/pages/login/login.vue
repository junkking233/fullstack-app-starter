<template>
  <view class="page">
    <view class="form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input
          v-model="form.username"
          class="input"
          placeholder="请输入用户名"
          type="text"
        />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input
          v-model="form.password"
          class="input"
          placeholder="请输入密码"
          type="password"
        />
      </view>
      <button class="btn-login" :loading="loading" @tap="handleLogin">
        登录
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { login } from '../../api/auth'
import { setAuthState } from '../../utils/auth'

const form = ref({
  username: '',
  password: '',
})
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await login(form.value)
    setAuthState(res.data.token, res.data.user)
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch {
    // 错误已在 http.ts 中统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.page {
  padding: 40rpx;
  background: #f5f7fb;
  min-height: 100vh;
}

.form {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
  margin-top: 80rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-item {
  margin-bottom: 32rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 12rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e5e5e5;
  border-radius: 8rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.btn-login {
  width: 100%;
  height: 88rpx;
  background: #13b45d;
  color: #fff;
  font-size: 32rpx;
  border-radius: 8rpx;
  margin-top: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.btn-login::after {
  border: none;
}
</style>
