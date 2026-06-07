<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { authApi } from '@/api/authApi';
import { commentApi } from '@/api/worldcupApi';
import type { MatchComment } from '@/types/worldcup';
import { useCurrentUser } from '@/utils/auth';

const user = useCurrentUser();
const comments = ref<MatchComment[]>([]);
const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' });

onMounted(async () => {
  comments.value = await commentApi.my();
});

function statusLabel(status: string) {
  const map: Record<string, string> = {
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已驳回',
  };
  return map[status] || status;
}

function statusTagType(status: string) {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info'> = {
    PENDING: 'info',
    APPROVED: 'success',
    REJECTED: 'danger',
  };
  return map[status] || 'info';
}

async function changePassword() {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.warning('请填写原密码和新密码');
    return;
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.warning('两次输入的新密码不一致');
    return;
  }
  await authApi.changePassword({
    oldPassword: passwordForm.value.oldPassword,
    newPassword: passwordForm.value.newPassword,
  });
  passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' };
  ElMessage.success('密码修改成功');
}
</script>

<template>
  <div class="profile-grid">
    <el-card>
      <template #header>个人中心</template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户名">{{ user?.username }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ user?.email }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ user?.role }}</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-form :model="passwordForm" label-width="84px">
        <el-form-item label="原密码"><el-input v-model="passwordForm.oldPassword" type="password" show-password /></el-form-item>
        <el-form-item label="新密码"><el-input v-model="passwordForm.newPassword" type="password" show-password /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="passwordForm.confirmPassword" type="password" show-password /></el-form-item>
        <el-form-item><el-button type="primary" @click="changePassword">修改密码</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <template #header>我的评论</template>
      <el-table :data="comments" border>
        <el-table-column prop="matchId" label="比赛ID" width="90" />
        <el-table-column prop="content" label="评论内容" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.auditStatus)" size="small">{{ statusLabel(row.auditStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.profile-grid {
  display: grid;
  grid-template-columns: 360px 1fr;
  gap: 16px;
}
</style>
