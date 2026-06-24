<template>
  <view class="page">
    <view class="header">
      <text class="title">欢迎使用</text>
      <text class="subtitle">全栈项目 uni-app 端</text>
    </view>

    <view class="card-list">
      <view class="card" @tap="goToLogin">
        <text class="card-title">登录</text>
        <text class="card-desc">登录后体验完整功能</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { isLoggedIn } from '../../utils/auth'

const goToLogin = () => {
  if (isLoggedIn()) {
    uni.showToast({ title: '已登录', icon: 'success' })
  } else {
    uni.navigateTo({ url: '/pages/login/login' })
  }
}
</script>

<style scoped>
.page {
  padding: 40rpx;
}

.header {
  padding: 60rpx 0 40rpx;
}

.title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
}

.subtitle {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-top: 12rpx;
}

.card-list {
  margin-top: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.card-desc {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}
</style>
