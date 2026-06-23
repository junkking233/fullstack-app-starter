<script setup lang="ts">
import { reactive, ref } from 'vue';
import { Camera, Lock, Message, Phone, User } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { getCurrentUser } from '@/utils/auth';

const activeTab = ref('info');
const currentUser = getCurrentUser();

const profileForm = reactive({
  nickname: currentUser?.nickname || '',
  realName: currentUser?.username || '',
  email: '',
  phone: '',
  idCard: '11010119900101****',
  address: '北京市朝阳区',
});

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
});

function saveProfile() {
  ElMessage.success('个人信息保存成功');
}

async function savePassword() {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致');
    return;
  }
  try {
    await authApi.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    });
    passwordForm.oldPassword = '';
    passwordForm.newPassword = '';
    passwordForm.confirmPassword = '';
    ElMessage.success('密码修改成功');
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '密码修改失败');
  }
}
</script>

<template>
  <div class="profile-page">
    <div class="page-header">
      <h1>个人中心</h1>
      <p>管理您的个人信息和账号安全</p>
    </div>

    <div class="profile-layout">
      <!-- 左侧卡片 -->
      <aside class="profile-sidebar">
        <div class="avatar-section">
          <div class="avatar-wrap">
            <el-avatar :size="90" class="main-avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <div class="avatar-overlay">
              <el-icon><Camera /></el-icon>
            </div>
          </div>
          <h3>{{ profileForm.nickname }}</h3>
          <p class="role-badge">普通用户</p>
        </div>

        <div class="profile-stats">
          <div class="stat-item">
            <strong>12</strong>
            <span>业务</span>
          </div>
          <div class="stat-divider" />
          <div class="stat-item">
            <strong>3</strong>
            <span>消息</span>
          </div>
          <div class="stat-divider" />
          <div class="stat-item">
            <strong>0</strong>
            <span>待办</span>
          </div>
        </div>

        <div class="profile-meta">
          <div class="meta-row">
            <el-icon><Message /></el-icon>
            <span>{{ profileForm.email }}</span>
          </div>
          <div class="meta-row">
            <el-icon><Phone /></el-icon>
            <span>{{ profileForm.phone }}</span>
          </div>
        </div>
      </aside>

      <!-- 右侧表单 -->
      <section class="profile-content">
        <el-tabs v-model="activeTab" class="profile-tabs">
          <el-tab-pane label="基本信息" name="info">
            <el-form :model="profileForm" label-width="90px" class="profile-form">
              <el-form-item label="昵称">
                <el-input v-model="profileForm.nickname" :prefix-icon="User" />
              </el-form-item>
              <el-form-item label="真实姓名">
                <el-input v-model="profileForm.realName" disabled />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="profileForm.email" :prefix-icon="Message" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="profileForm.phone" :prefix-icon="Phone" />
              </el-form-item>
              <el-form-item label="身份证号">
                <el-input v-model="profileForm.idCard" disabled />
              </el-form-item>
              <el-form-item label="地址">
                <el-input v-model="profileForm.address" type="textarea" :rows="2" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveProfile">保存修改</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="账号安全" name="security">
            <el-form :model="passwordForm" label-width="110px" class="profile-form">
              <el-form-item label="原密码">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password :prefix-icon="Lock" />
              </el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="passwordForm.newPassword" type="password" show-password :prefix-icon="Lock" />
              </el-form-item>
              <el-form-item label="确认新密码">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password :prefix-icon="Lock" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="savePassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </section>
    </div>
  </div>
</template>

<style scoped>
.profile-page {
  max-width: 1180px;
  margin: 0 auto;
  padding: 32px 28px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 6px;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.page-header p {
  margin: 0;
  color: var(--c-muted);
  font-size: 15px;
}

.profile-layout {
  display: grid;
  gap: 20px;
  grid-template-columns: 320px minmax(0, 1fr);
}

/* Sidebar */
.profile-sidebar {
  padding: 32px 24px;
  text-align: center;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  height: fit-content;
}

.avatar-section {
  margin-bottom: 24px;
}

.avatar-wrap {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;
}

.main-avatar {
  background: linear-gradient(135deg, var(--c-primary), var(--c-blue));
  color: #fff;
  font-size: 36px;
  box-shadow: 0 4px 16px rgb(13 148 136 / 25%);
}

.avatar-overlay {
  position: absolute;
  right: -2px;
  bottom: -2px;
  display: grid;
  width: 32px;
  height: 32px;
  place-items: center;
  color: #fff;
  font-size: 14px;
  background: linear-gradient(135deg, var(--c-primary), var(--c-blue));
  border: 2px solid #fff;
  border-radius: 50%;
  cursor: pointer;
  transition: transform var(--transition-fast);
}

.avatar-overlay:hover {
  transform: scale(1.1);
}

.avatar-section h3 {
  margin: 0 0 8px;
  font-size: 20px;
  font-weight: 700;
}

.role-badge {
  display: inline-block;
  margin: 0;
  padding: 4px 14px;
  color: var(--c-primary);
  font-size: 12px;
  font-weight: 600;
  background: var(--c-primary-bg);
  border: 1px solid rgb(13 148 136 / 12%);
  border-radius: 100px;
}

.profile-stats {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 20px 0;
  border-top: 1px solid var(--c-line);
  border-bottom: 1px solid var(--c-line);
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-item strong {
  font-size: 22px;
  font-weight: 800;
  color: var(--c-ink);
}

.stat-item span {
  color: var(--c-muted);
  font-size: 12px;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: var(--c-line);
}

.profile-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.meta-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--c-muted);
  font-size: 13px;
}

.meta-row .el-icon {
  font-size: 15px;
  color: var(--c-muted-light);
}

/* Content */
.profile-content {
  padding: 24px 28px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.profile-form {
  max-width: 480px;
  padding-top: 8px;
}

@media (max-width: 860px) {
  .profile-page {
    padding: 20px 16px;
  }

  .profile-layout {
    grid-template-columns: 1fr;
  }

  .profile-form {
    max-width: none;
  }
}
</style>
