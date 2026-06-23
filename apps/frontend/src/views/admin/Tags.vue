<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue';
import { tagApi } from '@/api/tagApi';

interface Tag {
  id: number;
  name: string;
  usageCount: number;
  createTime: string;
}

const tags = ref<Tag[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref('新增标签');
const formRef = ref();
const form = ref({ name: '' });
const editingId = ref<number | null>(null);
const searchKeyword = ref('');

const rules = {
  name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
};

async function loadTags() {
  loading.value = true;
  try {
    const data = await tagApi.list() as unknown as Tag[];
    tags.value = data;
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载标签失败');
  } finally {
    loading.value = false;
  }
}

const filteredTags = ref<Tag[]>([]);

function updateFiltered() {
  if (!searchKeyword.value) {
    filteredTags.value = tags.value;
  } else {
    const keyword = searchKeyword.value.toLowerCase();
    filteredTags.value = tags.value.filter((t) => t.name.toLowerCase().includes(keyword));
  }
}

function openCreate() {
  editingId.value = null;
  dialogTitle.value = '新增标签';
  form.value = { name: '' };
  dialogVisible.value = true;
}

function openEdit(tag: Tag) {
  editingId.value = tag.id;
  dialogTitle.value = '编辑标签';
  form.value = { name: tag.name };
  dialogVisible.value = true;
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  try {
    if (editingId.value) {
      await tagApi.update(editingId.value, form.value.name);
      ElMessage.success('标签已更新');
    } else {
      await tagApi.create(form.value.name);
      ElMessage.success('标签已创建');
    }
    dialogVisible.value = false;
    await loadTags();
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '操作失败');
  }
}

async function handleDelete(tag: Tag) {
  try {
    await ElMessageBox.confirm(`确定要删除标签「${tag.name}」吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    await tagApi.delete(tag.id);
    ElMessage.success('标签已删除');
    await loadTags();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error instanceof Error ? error.message : '删除失败');
    }
  }
}

onMounted(() => {
  loadTags().then(() => updateFiltered());
});

// Watch tags update
import { watch } from 'vue';
watch(tags, () => updateFiltered(), { deep: true });
</script>

<template>
  <div class="tags-page">
    <div class="page-toolbar">
      <h2>标签管理</h2>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增标签</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        :prefix-icon="Search"
        placeholder="搜索标签名称"
        clearable
        style="width: 280px"
        @input="updateFiltered"
      />
    </div>

    <el-table v-loading="loading" :data="filteredTags" border>
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="标签名称" min-width="200">
        <template #default="{ row }">
          <el-tag>{{ row.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="usageCount" label="使用次数" width="120" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button text type="primary" :icon="Edit" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button text type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="420px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入标签名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.tags-page {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 22px;
}

.search-bar {
  margin-bottom: 16px;
}
</style>
