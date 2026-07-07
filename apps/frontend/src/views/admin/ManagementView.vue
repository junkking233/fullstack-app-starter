<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Refresh, Search } from '@element-plus/icons-vue';
import { userApi } from '@/api/userApi';
import type { PageResult, User, UserQuery } from '@/types/user';

const loading = ref(false);
const users = ref<User[]>([]);
const total = ref(0);

const query = reactive<UserQuery>({
  username: '',
  nickname: '',
  status: '',
  page: 1,
  pageSize: 10,
});

function roleLabel(role: string) {
  if (role === 'ADMIN') return '管理员';
  return '普通用户';
}

function statusLabel(status: string) {
  return status === 'banned' ? '禁用' : '正常';
}

function statusType(status: string) {
  return status === 'banned' ? 'danger' : 'success';
}

async function loadUsers() {
  loading.value = true;
  try {
    const result = await userApi.list({ ...query }) as PageResult<User>;
    users.value = result.records || [];
    total.value = result.total || 0;
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '用户列表加载失败');
  } finally {
    loading.value = false;
  }
}

function resetQuery() {
  query.username = '';
  query.nickname = '';
  query.status = '';
  query.page = 1;
  loadUsers();
}

async function toggleStatus(row: User) {
  try {
    if (row.status === 'banned') {
      await userApi.unban(row.id);
      ElMessage.success('账号已启用');
    } else {
      await userApi.ban(row.id);
      ElMessage.success('账号已禁用');
    }
    await loadUsers();
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '状态更新失败');
  }
}

function handlePageChange(page: number) {
  query.page = page;
  loadUsers();
}

onMounted(loadUsers);
</script>

<template>
  <div class="user-management">
    <div class="page-heading">
      <div>
        <p class="eyebrow">Admin Example</p>
        <h1>用户管理</h1>
      </div>
      <el-button :icon="Refresh" @click="loadUsers">刷新</el-button>
    </div>

    <el-card shadow="never" class="filter-card">
      <el-form :model="query" label-width="72px" class="filter-form" @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="query.username" clearable placeholder="输入用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="query.nickname" clearable placeholder="输入昵称" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="全部状态">
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="banned" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="loadUsers">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table v-loading="loading" :data="users" row-key="id">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="nickname" label="昵称" min-width="140">
          <template #default="{ row }">{{ row.nickname || '-' }}</template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">{{ roleLabel(row.role) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="light">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="toggleStatus(row)">
              {{ row.status === 'banned' ? '启用' : '禁用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :page-size="query.pageSize"
          :current-page="query.page"
          :total="total"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.user-management {
  display: grid;
  gap: 18px;
}

.page-heading {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.eyebrow {
  margin: 0 0 4px;
  color: var(--c-primary);
  font-size: 12px;
  font-weight: 700;
}

h1 {
  margin: 0;
  color: var(--c-ink);
  font-size: 26px;
}

.filter-card {
  border-color: var(--c-line);
}

.filter-form {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  align-items: end;
}

.filter-form :deep(.el-form-item) {
  margin-bottom: 0;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
}

@media (max-width: 1080px) {
  .filter-form {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .page-heading {
    align-items: flex-start;
    flex-direction: column;
  }

  .filter-form {
    grid-template-columns: 1fr;
  }
}
</style>
