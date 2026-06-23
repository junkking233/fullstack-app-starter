<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reportApi } from '@/api/reportApi'

const reports = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 20
const total = ref(0)
const statusFilter = ref('pending')

const reasonLabels: Record<string, string> = {
  ad: '广告', porn: '色情', abuse: '辱骂', copyright: '侵权', other: '其他'
}
const targetLabels: Record<string, string> = {
  question: '问题', answer: '回答', comment: '评论'
}

async function load() {
  loading.value = true
  try {
    const res: any = await reportApi.adminList({ status: statusFilter.value, page: page.value, pageSize })
    reports.value = res?.records || []
    total.value = res?.total || 0
  } catch { reports.value = [] }
  finally { loading.value = false }
}

async function handleReport(id: number, action: string) {
  const actionLabels: Record<string, string> = { ignore: '忽略', delete_content: '删除内容', ban_user: '封禁用户' }
  try {
    const { value: note } = await ElMessageBox.prompt('处理备注（可选）', `处理举报 - ${actionLabels[action]}`, {
      inputType: 'textarea', confirmButtonText: '确认', cancelButtonText: '取消'
    })
    await reportApi.adminHandle(id, { action, resultNote: note || '' })
    ElMessage.success('处理成功')
    load()
  } catch { /* cancelled */ }
}

function formatDate(d: string) {
  if (!d) return ''
  return new Date(d).toLocaleString('zh-CN')
}

onMounted(load)
</script>

<template>
  <div class="reports-page">
    <div class="page-title">举报处理</div>
    <div class="filter-bar">
      <el-radio-group v-model="statusFilter" @change="load">
        <el-radio-button value="pending">待处理</el-radio-button>
        <el-radio-button value="ignored">已忽略</el-radio-button>
        <el-radio-button value="deleted_content">已删内容</el-radio-button>
        <el-radio-button value="banned_user">已封禁</el-radio-button>
      </el-radio-group>
    </div>
    <el-table :data="reports" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="举报目标" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ targetLabels[row.targetType] || row.targetType }}</el-tag>
          <span style="margin-left:4px">#{{ row.targetId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="举报原因" width="100">
        <template #default="{ row }">{{ reasonLabels[row.reason] || row.reason }}</template>
      </el-table-column>
      <el-table-column prop="detail" label="补充说明" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="举报时间" width="160">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 'pending'">
            <el-button size="small" @click="handleReport(row.id, 'ignore')">忽略</el-button>
            <el-button size="small" type="warning" @click="handleReport(row.id, 'delete_content')">删除内容</el-button>
            <el-button size="small" type="danger" @click="handleReport(row.id, 'ban_user')">封禁</el-button>
          </template>
          <el-tag v-else size="small">已处理</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="total > pageSize" class="pagination"><el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="p => { page = p; load() }" /></div>
  </div>
</template>

<style scoped>
.reports-page { padding: 20px; }
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; }
.filter-bar { margin-bottom: 16px; }
.pagination { text-align: center; margin-top: 16px; }
</style>
