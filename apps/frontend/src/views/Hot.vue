<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Trophy, View, ChatLineSquare, Star } from '@element-plus/icons-vue'
import { hotlistApi } from '@/api/hotlistApi'

const router = useRouter()
const period = ref('daily')
const questions = ref<any[]>([])
const loading = ref(false)

const periods = [
  { label: '日榜', value: 'daily' },
  { label: '周榜', value: 'weekly' },
  { label: '月榜', value: 'monthly' },
]

async function load() {
  loading.value = true
  try {
    const res: any = await hotlistApi.list({ period: period.value })
    questions.value = res?.records || (Array.isArray(res) ? res : [])
  } catch { questions.value = [] }
  finally { loading.value = false }
}

function goToDetail(id: number) { router.push(`/question/${id}`) }
function getRankClass(i: number) { return i < 3 ? `rank-${i + 1}` : '' }

onMounted(load)
</script>

<template>
  <div class="hot-page">
    <header class="page-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <nav class="header-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/hot" class="nav-link active">热榜</router-link>
          <router-link to="/featured" class="nav-link">精华区</router-link>
          <router-link to="/leaderboard" class="nav-link">排行榜</router-link>
        </nav>
      </div>
    </header>
    <main class="hot-main">
      <div class="section-title"><el-icon><Trophy /></el-icon> 热门问题</div>
      <el-tabs v-model="period" @tab-change="load">
        <el-tab-pane v-for="p in periods" :key="p.value" :label="p.label" :name="p.value" />
      </el-tabs>
      <div v-loading="loading">
        <div v-if="questions.length === 0 && !loading" class="empty"><el-empty description="暂无热榜数据" /></div>
        <div v-for="(q, idx) in questions" :key="q.id" class="hot-item" @click="goToDetail(q.id)">
          <span class="rank" :class="getRankClass(idx)">{{ idx + 1 }}</span>
          <div class="hot-content">
            <h3>{{ q.title }}</h3>
            <div class="meta">
              <span><el-icon><ChatLineSquare /></el-icon> {{ q.answerCount || 0 }} 回答</span>
              <span><el-icon><View /></el-icon> {{ q.viewCount || 0 }} 浏览</span>
              <span><el-icon><Star /></el-icon> {{ q.voteCount || 0 }} 赞</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.hot-page { min-height: 100vh; background: #f5f7fa; }
.page-header { background: #fff; border-bottom: 1px solid #e4e7ed; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 800px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; gap: 24px; }
.logo { font-size: 20px; font-weight: 800; color: #409eff; text-decoration: none; }
.header-nav { display: flex; gap: 8px; }
.nav-link { padding: 6px 14px; color: #606266; text-decoration: none; font-size: 14px; border-radius: 4px; }
.nav-link:hover, .nav-link.active { color: #409eff; background: #ecf5ff; }
.hot-main { max-width: 800px; margin: 0 auto; padding: 20px 24px; }
.section-title { font-size: 18px; font-weight: 600; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; color: #e6a23c; }
.hot-item { display: flex; align-items: center; gap: 16px; padding: 16px 20px; background: #fff; border: 1px solid #e4e7ed; border-radius: 8px; margin-bottom: 8px; cursor: pointer; transition: all .2s; }
.hot-item:hover { border-color: #409eff; }
.rank { font-size: 20px; font-weight: 800; color: #c0c4cc; min-width: 32px; text-align: center; }
.rank-1 { color: #f56c6c; }
.rank-2 { color: #e6a23c; }
.rank-3 { color: #409eff; }
.hot-content { flex: 1; }
.hot-content h3 { margin: 0 0 6px; font-size: 15px; color: #303133; }
.meta { display: flex; gap: 16px; color: #909399; font-size: 13px; }
.empty { padding: 60px 0; }
</style>
