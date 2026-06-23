<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete } from '@element-plus/icons-vue';
import { categoryApi } from '@/api/categoryApi';

interface Category {
  id: number;
  name: string;
  parentId: number | null;
  sortOrder: number;
  questionCount: number;
  children: Category[];
  createTime: string;
}

const categories = ref<Category[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref('新增分类');
const formRef = ref();
const form = ref({ name: '', parentId: undefined as number | undefined, sortOrder: 0 });
const editingId = ref<number | null>(null);
const parentOptions = ref<{ value: number; label: string }[]>([]);

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
};

async function loadCategories() {
  loading.value = true;
  try {
    const data = await categoryApi.list() as unknown as Category[];
    categories.value = data;
    parentOptions.value = [{ value: 0, label: '顶级分类' }, ...flattenOptions(data)];
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '加载分类失败');
  } finally {
    loading.value = false;
  }
}

function flattenOptions(items: Category[]): { value: number; label: string }[] {
  const result: { value: number; label: string }[] = [];
  for (const item of items) {
    result.push({ value: item.id, label: item.name });
    if (item.children && item.children.length > 0) {
      result.push(...flattenOptions(item.children));
    }
  }
  return result;
}

function openCreate() {
  editingId.value = null;
  dialogTitle.value = '新增分类';
  form.value = { name: '', parentId: undefined, sortOrder: 0 };
  dialogVisible.value = true;
}

function openEdit(cat: Category) {
  editingId.value = cat.id;
  dialogTitle.value = '编辑分类';
  form.value = { name: cat.name, parentId: cat.parentId ?? undefined, sortOrder: cat.sortOrder };
  dialogVisible.value = true;
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  try {
    const params = {
      name: form.value.name,
      parentId: form.value.parentId || undefined,
      sortOrder: form.value.sortOrder,
    };

    if (editingId.value) {
      await categoryApi.update(editingId.value, { name: form.value.name, sortOrder: form.value.sortOrder });
      ElMessage.success('分类已更新');
    } else {
      await categoryApi.create(params);
      ElMessage.success('分类已创建');
    }
    dialogVisible.value = false;
    await loadCategories();
  } catch (error) {
    ElMessage.error(error instanceof Error ? error.message : '操作失败');
  }
}

async function handleDelete(cat: Category) {
  try {
    await ElMessageBox.confirm(`确定要删除分类「${cat.name}」吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    await categoryApi.delete(cat.id);
    ElMessage.success('分类已删除');
    await loadCategories();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error instanceof Error ? error.message : '删除失败');
    }
  }
}

onMounted(() => {
  loadCategories();
});
</script>

<template>
  <div class="categories-page">
    <div class="page-toolbar">
      <h2>分类管理</h2>
      <el-button type="primary" :icon="Plus" @click="openCreate">新增分类</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="categories"
      row-key="id"
      border
      default-expand-all
    >
      <el-table-column prop="name" label="分类名称" min-width="200" />
      <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
      <el-table-column prop="questionCount" label="问题数" width="100" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button text type="primary" :icon="Edit" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button text type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父级分类" prop="parentId">
          <el-select v-model="form.parentId" placeholder="请选择父级分类（可选）" clearable style="width: 100%">
            <el-option
              v-for="opt in parentOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value === 0 ? undefined : opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" />
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
.categories-page {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 22px;
}
</style>
