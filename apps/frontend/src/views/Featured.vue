<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Star, View, ChatLineSquare, UserFilled, Search as SearchIcon } from '@element-plus/icons-vue'
import { questionApi } from '@/api/questionApi'

const router = useRouter()
const questions = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 20
const total = ref(0)
const keyword = ref('')

async function load() {
  loading.value = true
  try {
    const params: any = { page: page.value, pageSize, sort: 'recommend', isFeatured: 1 }
    if (keyword.value.trim()) params.keyword = keyword.value.trim()
    const res: any = await questionApi.search(params)
    questions.value = res?.records || []
    total.value = res?.total || 0
  } catch { questions.value = [] }
  finally { loading.value = false }
}

function handleSearch() { page.value = 1; load() }
function goToDetail(id: number) { router.push(`/question/${id}`) }
function formatDate(d: string) {
  if (!d) return ''
  const diff = Date.now() - new Date(d).getTime()
  const days = Math.floor(diff / 86400000)
  if (days < 1) return '今天'
  if (days < 30) return `${days}天前`
  return new Date(d).toLocaleDateString('zh-CN')
}
function getExcerpt(c: string, max = 120) {
  if (!c) return ''
  const t = c.replace(/<[^>]*>/g, '').trim()
  return t.length > max ? t.slice(0, max) + '...' : t
}

onMounted(load)
</script>

<template>
  <div class="featured-page">
    <header class="page-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <nav class="header-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/hot" class="nav-link">热榜</router-link>
          <router-link to="/featured" class="nav-link active">精华区</router-link>
          <router-link to="/leaderboard" class="nav-link">排行榜</router-link>
        </nav>
        <div class="search-area">
          <el-input v-model="keyword" placeholder="搜索精华内容..." :prefix-icon="SearchIcon" clearable style="width: 240px" @keyup.enter="handleSearch" @clear="handleSearch" />
        </div>
      </div>
    </header>
    <main class="featured-main">
      <div class="section-title"><el-icon><Star /></el-icon> 精华问答</div>
      <div v-loading="loading">
        <div v-if="questions.length === 0 && !loading" class="empty"><el-empty description="暂无精华内容" /></div>
        <div v-for="q in questions" :key="q.id" class="q-item" @click="goToDetail(q.id)">
          <div class="q-main">
            <h3><el-tag type="warning" size="small">精华</el-tag> {{ q.title }}</h3>
            <p class="excerpt">{{ getExcerpt(q.content) }}</p>
            <div class="meta">
              <span><el-icon><UserFilled /></el-icon> {{ q.author?.nickname || '匿名' }}</span>
              <span><el-icon><ChatLineSquare /></el-icon> {{ q.answerCount || 0 }}</span>
              <span><el-icon><View /></el-icon> {{ q.viewCount || 0 }}</span>
              <span>{{ formatDate(q.createdAt) }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-if="total > pageSize" class="pagination"><el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="p => { page = p; load() }" /></div>
    </main>
  </div>
</template>

<style scoped>
.featured-page { min-height: 100vh; background: #f5f7fa; }
.page-header { background: #fff; border-bottom: 1px solid #e4e7ed; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 1000px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; gap: 24px; }
.logo { font-size: 20px; font-weight: 800; color: #409eff; text-decoration: none; }
.header-nav { display: flex; gap: 8px; }
.nav-link { padding: 6px 14px; color: #606266; text-decoration: none; font-size: 14px; border-radius: 4px; }
.nav-link:hover, .nav-link.active { color: #409eff; background: #ecf5ff; }
.search-area { margin-left: auto; }
.featured-main { max-width: 1000px; margin: 0 auto; padding: 20px 24px; }
.section-title { font-size: 18px; font-weight: 600; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; color: #e6a23c; }
.q-item { display: flex; gap: 16px; padding: 20px; background: #fff; border: 1px solid #e4e7ed; border-radius: 8px; margin-bottom: 12px; cursor: pointer; transition: all .2s; }
.q-item:hover { border-color: #409eff; box-shadow: 0 2px 8px rgba(64,158,255,.1); }
.q-main { flex: 1; }
.q-main h3 { margin: 0 0 8px; font-size: 16px; color: #303133; }
.excerpt { color: #909399; font-size: 14px; margin: 0 0 8px; }
.meta { display: flex; gap: 16px; color: #909399; font-size: 13px; }
.pagination { text-align: center; margin-top: 20px; }
.empty { padding: 60px 0; }
</style>
