<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { questionApi, adminQuestionApi } from '@/api/questionApi'

const list = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const total = ref(0)

// Filters
const keyword = ref('')
const statusFilter = ref('')

const statusOptions = [
  { label: '全部状态', value: '' },
  { label: '已发布', value: 'published' },
  { label: '审核中', value: 'pending' },
  { label: '已拒绝', value: 'rejected' },
]

async function loadList() {
  loading.value = true
  try {
    const params: any = {
      page: page.value,
      pageSize,
    }
    if (keyword.value.trim()) {
      params.keyword = keyword.value.trim()
    }
    if (statusFilter.value) {
      params.status = statusFilter.value
    }
    const res: any = await questionApi.search(params)
    list.value = res?.records || res?.list || []
    total.value = res?.total || 0
  } catch {
    ElMessage.error('加载问题列表失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  loadList()
}

function handlePageChange(p: number) {
  page.value = p
  loadList()
}

// Pin toggle
async function handlePin(row: any) {
  try {
    await adminQuestionApi.pin(row.id)
    row.isPinned = !row.isPinned
    ElMessage.success(row.isPinned ? '已置顶' : '已取消置顶')
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Feature toggle
async function handleFeature(row: any) {
  try {
    await adminQuestionApi.feature(row.id)
    row.isFeatured = !row.isFeatured
    ElMessage.success(row.isFeatured ? '已设为精华' : '已取消精华')
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Delete
async function handleDelete(row: any) {
  try {
    await ElMessageBox.confirm(
      `确定要删除问题「${row.title}」吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
      },
    )
    await questionApi.delete(row.id)
    ElMessage.success('问题已删除')
    await loadList()
  } catch {
    // cancelled
  }
}

function getStatusType(status: string) {
  const map: Record<string, string> = {
    published: 'success',
    pending: 'warning',
    rejected: 'danger',
  }
  return map[status] || 'info'
}

function getStatusText(status: string) {
  const map: Record<string, string> = {
    published: '已发布',
    pending: '审核中',
    rejected: '已拒绝',
  }
  return map[status] || status
}

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleString('zh-CN')
}

onMounted(() => {
  loadList()
})
</script>

<template>
  <div class="questions-page">
    <div class="page-toolbar">
      <h2>问题管理</h2>
      <div class="toolbar-actions">
        <el-input
          v-model="keyword"
          :prefix-icon="Search"
          placeholder="搜索问题..."
          clearable
          style="width: 240px"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        />
        <el-select
          v-model="statusFilter"
          placeholder="状态筛选"
          style="width: 130px"
          clearable
          @change="handleSearch"
        >
          <el-option
            v-for="opt in statusOptions"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>
        <el-button :icon="Refresh" @click="loadList">刷新</el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="list" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip>
        <template #default="{ row }">
          <span v-if="row.isPinned" class="badge-pin">[置顶]</span>
          <span v-if="row.isFeatured" class="badge-feature">[精华]</span>
          {{ row.title }}
        </template>
      </el-table-column>
      <el-table-column label="作者" width="120">
        <template #default="{ row }">
          {{ row.author?.nickname || '匿名' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分类" width="100">
        <template #default="{ row }">
          {{ row.category?.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
      <el-table-column prop="answerCount" label="回答" width="80" align="center" />
      <el-table-column prop="voteCount" label="投票" width="80" align="center" />
      <el-table-column label="发布时间" width="170">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button
            :type="row.isPinned ? 'warning' : 'default'"
            size="small"
            @click="handlePin(row)"
          >
            {{ row.isPinned ? '取消置顶' : '置顶' }}
          </el-button>
          <el-button
            :type="row.isFeatured ? 'warning' : 'default'"
            size="small"
            @click="handleFeature(row)"
          >
            {{ row.isFeatured ? '取消精华' : '精华' }}
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="total > pageSize" class="pagination-bar">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>

    <el-empty v-if="list.length === 0 && !loading" description="暂无问题数据" />
  </div>
</template>

<style scoped>
.questions-page {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 22px;
}

.toolbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.badge-pin {
  color: var(--c-red);
  font-weight: 600;
  font-size: 12px;
}

.badge-feature {
  color: var(--c-amber);
  font-weight: 600;
  font-size: 12px;
}
</style>
