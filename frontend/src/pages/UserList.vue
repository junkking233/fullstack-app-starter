<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { Delete, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus';
import { userApi } from '@/api/userApi';
import type { User, UserQuery } from '@/types/user';

const users = ref<User[]>([]);
const total = ref(0);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref('新增用户');
const formRef = ref<FormInstance>();

const query = reactive<UserQuery>({
  page: 1,
  pageSize: 10,
});

const form = reactive<Partial<User>>({
  username: '',
  password: '',
  email: '',
  phone: '',
  role: 'USER',
  status: 1,
});

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
};

onMounted(fetchUsers);

async function fetchUsers() {
  loading.value = true;
  try {
    const result = await userApi.list(query);
    users.value = result.records;
    total.value = result.total;
  } finally {
    loading.value = false;
  }
}

function search() {
  query.page = 1;
  fetchUsers();
}

function resetSearch() {
  query.username = '';
  query.email = '';
  query.status = undefined;
  search();
}

function openCreate() {
  dialogTitle.value = '新增用户';
  Object.assign(form, {
    id: undefined,
    username: '',
    password: '',
    email: '',
    phone: '',
    role: 'USER',
    status: 1,
  });
  dialogVisible.value = true;
}

function openEdit(row: User) {
  dialogTitle.value = '编辑用户';
  Object.assign(form, { ...row, password: '' });
  dialogVisible.value = true;
}

async function submitForm() {
  await formRef.value?.validate();
  if (form.id) {
    await userApi.update(form.id, form);
    ElMessage.success('更新成功');
  } else {
    await userApi.create(form);
    ElMessage.success('创建成功');
  }
  dialogVisible.value = false;
  fetchUsers();
}

async function removeUser(row: User) {
  await ElMessageBox.confirm(`确认删除用户 ${row.username}？`, '删除确认', {
    type: 'warning',
  });
  await userApi.delete(row.id);
  ElMessage.success('删除成功');
  fetchUsers();
}
</script>

<template>
  <section class="page-section">
    <div class="page-toolbar">
      <el-form :model="query" inline>
        <el-form-item label="用户名">
          <el-input v-model="query.username" clearable placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="query.email" clearable placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable placeholder="请选择" style="width: 132px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="search">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增</el-button>
    </div>

    <el-table v-loading="loading" :data="users" stripe border height="calc(100vh - 230px)">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" min-width="140" />
      <el-table-column prop="email" label="邮箱" min-width="200" />
      <el-table-column prop="phone" label="手机号" min-width="140" />
      <el-table-column prop="role" label="角色" width="110" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" :icon="Edit" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" :icon="Delete" link @click="removeUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-bar">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-sizes="[10, 20, 50]"
        @change="fetchUsers"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="88px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 180px">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="普通用户" value="USER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>
