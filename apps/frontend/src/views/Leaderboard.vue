<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Trophy, UserFilled } from '@element-plus/icons-vue'
import { achievementApi } from '@/api/achievementApi'
import { getCurrentUser } from '@/utils/auth'

const currentUser = getCurrentUser()
const users = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 50
const total = ref(0)

const levelNames: Record<number, string> = {
  1: '初学者', 2: '入门', 3: '进阶', 4: '熟练', 5: '精通',
  6: '专家', 7: '大师', 8: '宗师', 9: '传说', 10: '至尊'
}

async function load() {
  loading.value = true
  try {
    const res: any = await achievementApi.leaderboard({ page: page.value, pageSize })
    users.value = res?.records || (Array.isArray(res) ? res : [])
    total.value = res?.total || users.value.length
  } catch { users.value = [] }
  finally { loading.value = false }
}

function getRankClass(i: number) { return i < 3 ? `rank-${i + 1}` : '' }
function getLevelTag(level: number) {
  if (level >= 8) return 'danger'
  if (level >= 5) return 'warning'
  if (level >= 3) return 'success'
  return 'info'
}

onMounted(load)
</script>

<template>
  <div class="leaderboard-page">
    <header class="page-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <nav class="header-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/hot" class="nav-link">热榜</router-link>
          <router-link to="/featured" class="nav-link">精华区</router-link>
          <router-link to="/leaderboard" class="nav-link active">排行榜</router-link>
        </nav>
      </div>
    </header>
    <main class="lb-main">
      <div class="section-title"><el-icon><Trophy /></el-icon> 经验值排行榜</div>
      <div v-loading="loading">
        <div v-if="users.length === 0 && !loading" class="empty"><el-empty description="暂无数据" /></div>
        <div v-for="(u, idx) in users" :key="u.id" class="lb-item" :class="{ 'is-me': currentUser?.id === u.id }">
          <span class="rank" :class="getRankClass(idx)">{{ idx + 1 }}</span>
          <el-avatar :size="40" class="avatar"><el-icon><UserFilled /></el-icon></el-avatar>
          <div class="user-info">
            <div class="name">{{ u.nickname || u.username }}</div>
            <div class="level">
              <el-tag :type="getLevelTag(u.level)" size="small">Lv.{{ u.level }} {{ levelNames[u.level] || '' }}</el-tag>
            </div>
          </div>
          <div class="exp">{{ u.exp || 0 }} 经验</div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.leaderboard-page { min-height: 100vh; background: #f5f7fa; }
.page-header { background: #fff; border-bottom: 1px solid #e4e7ed; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 800px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; gap: 24px; }
.logo { font-size: 20px; font-weight: 800; color: #409eff; text-decoration: none; }
.header-nav { display: flex; gap: 8px; }
.nav-link { padding: 6px 14px; color: #606266; text-decoration: none; font-size: 14px; border-radius: 4px; }
.nav-link:hover, .nav-link.active { color: #409eff; background: #ecf5ff; }
.lb-main { max-width: 800px; margin: 0 auto; padding: 20px 24px; }
.section-title { font-size: 18px; font-weight: 600; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; color: #e6a23c; }
.lb-item { display: flex; align-items: center; gap: 16px; padding: 14px 20px; background: #fff; border: 1px solid #e4e7ed; border-radius: 8px; margin-bottom: 8px; transition: all .2s; }
.lb-item.is-me { border-color: #409eff; background: #ecf5ff; }
.rank { font-size: 20px; font-weight: 800; color: #c0c4cc; min-width: 32px; text-align: center; }
.rank-1 { color: #f56c6c; }
.rank-2 { color: #e6a23c; }
.rank-3 { color: #409eff; }
.avatar { background: #ecf5ff; color: #409eff; }
.user-info { flex: 1; }
.name { font-size: 15px; font-weight: 600; color: #303133; }
.level { margin-top: 4px; }
.exp { font-size: 16px; font-weight: 700; color: #e6a23c; }
.empty { padding: 60px 0; }
</style>
