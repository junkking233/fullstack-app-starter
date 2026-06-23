<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Delete } from '@element-plus/icons-vue';
import { sensitiveWordApi } from '@/api/sensitiveWordApi';

interface SensitiveWord {
  id: number;
  word: string;
  status: string;
  createTime: string;
}

const words = ref<SensitiveWord[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const formRef = ref();
const form = ref({ word: '' });
const statusFilter = ref('');

const rules = {
  word: [{ required: true, message: '请输入敏感词', trigger: 'blur' }],
};

async function loadWords() {
  loading.value = true;
  try {
    const data = await sensitiveWordApi.list() as unknown as SensitiveWord[];
    words.value = data;
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载敏感词失败');
  } finally {
    loading.value = false;
  }
}

const filteredWords = ref<SensitiveWord[]>([]);

function updateFiltered() {
  if (!statusFilter.value) {
    filteredWords.value = words.value;
  } else {
    filteredWords.value = words.value.filter((w) => w.status === statusFilter.value);
  }
}

async function openCreate() {
  form.value = { word: '' };
  dialogVisible.value = true;
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  try {
    await sensitiveWordApi.create(form.value.word);
    ElMessage.success('敏感词已添加');
    dialogVisible.value = false;
    await loadWords();
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '添加失败');
  }
}

async function toggleStatus(word: SensitiveWord) {
  const newStatus = word.status === 'active' ? 'disabled' : 'active';
  try {
    await sensitiveWordApi.updateStatus(word.id, newStatus);
    word.status = newStatus;
    ElMessage.success(newStatus === 'active' ? '已启用' : '已禁用');
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '操作失败');
  }
}

async function handleDelete(word: SensitiveWord) {
  try {
    await ElMessageBox.confirm(`确定要删除敏感词「${word.word}」吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    await sensitiveWordApi.delete(word.id);
    ElMessage.success('敏感词已删除');
    await loadWords();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error instanceof Error ? error.message : '删除失败');
    }
  }
}

import { watch } from 'vue';
watch(words, () => updateFiltered(), { deep: true });

onMounted(() => {
  loadWords().then(() => updateFiltered());
});
</script>

<template>
  <div class="sensitive-words-page">
    <div class="page-toolbar">
      <h2>敏感词管理</h2>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增敏感词</el-button>
    </div>

    <div class="filter-bar">
      <el-select v-model="statusFilter" placeholder="按状态筛选" clearable style="width: 160px" @change="updateFiltered">
        <el-option label="全部" value="" />
        <el-option label="启用" value="active" />
        <el-option label="禁用" value="disabled" />
      </el-select>
    </div>

    <el-table v-loading="loading" :data="filteredWords" border>
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="word" label="敏感词" min-width="200" />
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="{ row }">
          <el-switch
            :model-value="row.status === 'ACTIVE'"
            active-text="启用"
            inactive-text="禁用"
            @change="toggleStatus(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="100" align="center" fixed="right">
        <template #default="{ row }">
          <el-button text type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新增敏感词" width="420px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="敏感词" prop="word">
          <el-input v-model="form.word" placeholder="请输入敏感词" />
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
.sensitive-words-page {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 22px;
}

.filter-bar {
  margin-bottom: 16px;
}
</style>
