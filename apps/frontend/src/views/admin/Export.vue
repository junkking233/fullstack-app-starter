<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import { getAuthToken } from '@/utils/auth'

const exporting = ref<string | null>(null)

const exports = [
  { key: 'users', label: '用户列表', desc: '导出所有用户数据', icon: '👤' },
  { key: 'questions', label: '问题列表', desc: '导出所有问题数据', icon: '❓' },
  { key: 'answers', label: '回答列表', desc: '导出所有回答数据', icon: '💬' },
]

async function doExport(key: string) {
  exporting.value = key
  try {
    const token = getAuthToken()
    const res = await fetch(`/api/admin/export/${key}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
    if (!res.ok) throw new Error('导出失败')
    const blob = await res.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `${key}.json`
    a.click()
    URL.revokeObjectURL(url)
    ElMessage.success(`${key} 导出成功`)
  } catch (e: any) {
    ElMessage.error(e.message || '导出失败')
  } finally {
    exporting.value = null
  }
}
</script>

<template>
  <div class="export-page">
    <div class="page-title">数据导出</div>
    <div class="export-grid">
      <el-card v-for="item in exports" :key="item.key" class="export-card" shadow="hover">
        <div class="card-content">
          <div class="card-icon">{{ item.icon }}</div>
          <div class="card-info">
            <h3>{{ item.label }}</h3>
            <p>{{ item.desc }}</p>
          </div>
          <el-button type="primary" :icon="Download" :loading="exporting === item.key" @click="doExport(item.key)">
            导出
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.export-page { padding: 20px; }
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 24px; }
.export-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(360px, 1fr)); gap: 16px; }
.export-card { cursor: default; }
.card-content { display: flex; align-items: center; gap: 16px; }
.card-icon { font-size: 36px; }
.card-info { flex: 1; }
.card-info h3 { margin: 0 0 4px; font-size: 16px; }
.card-info p { margin: 0; color: #909399; font-size: 13px; }
</style>
