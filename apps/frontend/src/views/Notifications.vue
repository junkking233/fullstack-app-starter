<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Bell, UserFilled, Checked, ChatLineSquare, Star } from '@element-plus/icons-vue'
import { notificationApi } from '@/api/notificationApi'
import { getCurrentUser } from '@/utils/auth'

const router = useRouter()
const currentUser = getCurrentUser()
const notifications = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 20
const total = ref(0)
const unreadCount = ref(0)

const typeIcons: Record<string, any> = {
  new_answer: ChatLineSquare, comment: ChatLineSquare, follow: UserFilled,
  accepted: Star, audit_pass: Checked, audit_reject: Bell,
  invite: Bell, report_result: Bell, achievement: Star, system: Bell
}
const typeLabels: Record<string, string> = {
  new_answer: '新回答', comment: '评论', follow: '关注',
  accepted: '被采纳', audit_pass: '审核通过', audit_reject: '审核驳回',
  invite: '邀请回答', report_result: '举报结果', achievement: '成就解锁', system: '系统通知'
}

async function load() {
  loading.value = true
  try {
    const res: any = await notificationApi.list({ page: page.value, pageSize })
    notifications.value = res?.records || []
    total.value = res?.total || 0
    unreadCount.value = await notificationApi.unreadCount() as any || 0
  } catch { notifications.value = [] }
  finally { loading.value = false }
}

async function markRead(id: number) {
  await notificationApi.markRead(id)
  load()
}

async function markAllRead() {
  await notificationApi.markAllRead()
  load()
}

function formatDate(d: string) {
  if (!d) return ''
  const diff = Date.now() - new Date(d).getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 60) return `${mins}分钟前`
  const hrs = Math.floor(mins / 60)
  if (hrs < 24) return `${hrs}小时前`
  return `${Math.floor(hrs / 24)}天前`
}

onMounted(load)
</script>

<template>
  <div class="notif-page">
    <header class="page-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <span class="page-title">消息通知</span>
        <div class="header-right">
          <el-button v-if="unreadCount > 0" type="primary" text @click="markAllRead">全部已读</el-button>
          <router-link to="/" class="nav-link">首页</router-link>
        </div>
      </div>
    </header>
    <main class="notif-main">
      <div v-loading="loading">
        <div v-if="notifications.length === 0 && !loading" class="empty"><el-empty description="暂无消息" /></div>
        <div v-for="n in notifications" :key="n.id" class="notif-item" :class="{ unread: !n.isRead }" @click="markRead(n.id)">
          <el-icon class="notif-icon" :size="20"><component :is="typeIcons[n.type] || Bell" /></el-icon>
          <div class="notif-content">
            <div class="notif-text">{{ n.content || typeLabels[n.type] || '通知' }}</div>
            <div class="notif-time">{{ formatDate(n.createdAt) }}</div>
          </div>
          <el-tag v-if="!n.isRead" type="danger" size="small">未读</el-tag>
        </div>
      </div>
      <div v-if="total > pageSize" class="pagination"><el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="p => { page = p; load() }" /></div>
    </main>
  </div>
</template>

<style scoped>
.notif-page { min-height: 100vh; background: #f5f7fa; }
.page-header { background: #fff; border-bottom: 1px solid #e4e7ed; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 800px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; gap: 16px; }
.logo { font-size: 20px; font-weight: 800; color: #409eff; text-decoration: none; }
.page-title { font-size: 16px; font-weight: 600; flex: 1; }
.header-right { display: flex; gap: 12px; align-items: center; }
.nav-link { color: #606266; text-decoration: none; font-size: 14px; }
.notif-main { max-width: 800px; margin: 0 auto; padding: 20px 24px; }
.notif-item { display: flex; align-items: center; gap: 12px; padding: 16px 20px; background: #fff; border-radius: 8px; margin-bottom: 8px; cursor: pointer; border: 1px solid #e4e7ed; transition: all .2s; }
.notif-item:hover { border-color: #409eff; }
.notif-item.unread { background: #ecf5ff; border-color: #b3d8ff; }
.notif-icon { color: #409eff; flex-shrink: 0; }
.notif-content { flex: 1; }
.notif-text { font-size: 14px; color: #303133; }
.notif-time { font-size: 12px; color: #909399; margin-top: 4px; }
.pagination { text-align: center; margin-top: 20px; }
.empty { padding: 60px 0; }
</style>
