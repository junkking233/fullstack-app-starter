<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search as SearchIcon, View, ChatLineSquare, UserFilled } from '@element-plus/icons-vue'
import { questionApi } from '@/api/questionApi'
import { categoryApi } from '@/api/categoryApi'

const router = useRouter()
const route = useRoute()

const keyword = ref((route.query.q as string) || '')
const categoryId = ref<number | null>(null)
const tagId = ref<number | null>(null)
const sort = ref('recommend')
const questions = ref<any[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = 20
const total = ref(0)
const categories = ref<any[]>([])

async function loadCategories() {
  try {
    const data = await categoryApi.list()
    categories.value = Array.isArray(data) ? data : []
  } catch { /* ignore */ }
}

async function doSearch() {
  loading.value = true
  try {
    const params: any = { page: page.value, pageSize, sort: sort.value }
    if (keyword.value.trim()) params.keyword = keyword.value.trim()
    if (categoryId.value) params.categoryId = categoryId.value
    if (tagId.value) params.tagId = tagId.value
    const res: any = await questionApi.search(params)
    questions.value = res?.records || []
    total.value = res?.total || 0
  } catch { questions.value = [] }
  finally { loading.value = false }
}

function handlePageChange(p: number) { page.value = p; doSearch() }
function goToDetail(id: number) { router.push(`/question/${id}`) }
function formatDate(d: string) {
  if (!d) return ''
  const diff = Date.now() - new Date(d).getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 60) return `${mins}分钟前`
  const hrs = Math.floor(mins / 60)
  if (hrs < 24) return `${hrs}小时前`
  return `${Math.floor(hrs / 24)}天前`
}
function getExcerpt(c: string, max = 120) {
  if (!c) return ''
  const t = c.replace(/<[^>]*>/g, '').trim()
  return t.length > max ? t.slice(0, max) + '...' : t
}

onMounted(() => { loadCategories(); doSearch() })
</script>

<template>
  <div class="search-page">
    <header class="page-header">
      <div class="header-inner">
        <router-link to="/" class="logo">知问社区</router-link>
        <div class="search-bar">
          <el-input v-model="keyword" placeholder="搜索问题..." :prefix-icon="SearchIcon" size="large" clearable @keyup.enter="doSearch" @clear="doSearch" style="width: 500px" />
          <el-button type="primary" size="large" @click="doSearch">搜索</el-button>
        </div>
        <div class="header-right">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/hot" class="nav-link">热榜</router-link>
          <router-link to="/featured" class="nav-link">精华区</router-link>
        </div>
      </div>
    </header>
    <main class="search-main">
      <div class="filter-bar">
        <el-select v-model="categoryId" placeholder="所有分类" clearable style="width: 160px" @change="doSearch">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-radio-group v-model="sort" @change="doSearch">
          <el-radio-button value="recommend">相关度</el-radio-button>
          <el-radio-button value="latest">最新</el-radio-button>
          <el-radio-button value="hot">最热</el-radio-button>
        </el-radio-group>
      </div>
      <div v-loading="loading" class="result-list">
        <div v-if="questions.length === 0 && !loading" class="empty"><el-empty description="未找到相关问题" /></div>
        <div v-for="q in questions" :key="q.id" class="result-item" @click="goToDetail(q.id)">
          <h3>{{ q.title }}</h3>
          <p class="excerpt">{{ getExcerpt(q.content) }}</p>
          <div class="meta">
            <span><el-icon><UserFilled /></el-icon> {{ q.author?.nickname || '匿名' }}</span>
            <span><el-icon><ChatLineSquare /></el-icon> {{ q.answerCount || 0 }} 回答</span>
            <span><el-icon><View /></el-icon> {{ q.viewCount || 0 }} 浏览</span>
            <span>{{ formatDate(q.createdAt) }}</span>
          </div>
        </div>
      </div>
      <div v-if="total > pageSize" class="pagination"><el-pagination v-model:current-page="page" :page-size="pageSize" :total="total" layout="prev, pager, next" @current-change="handlePageChange" /></div>
    </main>
  </div>
</template>

<style scoped>
.search-page { min-height: 100vh; background: #f5f7fa; }
.page-header { background: #fff; border-bottom: 1px solid #e4e7ed; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 1000px; margin: 0 auto; padding: 0 24px; height: 60px; display: flex; align-items: center; gap: 24px; }
.logo { font-size: 20px; font-weight: 800; color: #409eff; text-decoration: none; }
.search-bar { display: flex; gap: 8px; flex: 1; justify-content: center; }
.header-right { display: flex; gap: 12px; }
.nav-link { color: #606266; text-decoration: none; font-size: 14px; }
.nav-link:hover { color: #409eff; }
.search-main { max-width: 1000px; margin: 0 auto; padding: 20px 24px; }
.filter-bar { display: flex; gap: 16px; align-items: center; margin-bottom: 16px; background: #fff; padding: 12px 20px; border-radius: 8px; }
.result-item { background: #fff; padding: 20px; border-radius: 8px; margin-bottom: 12px; cursor: pointer; border: 1px solid #e4e7ed; transition: all .2s; }
.result-item:hover { border-color: #409eff; box-shadow: 0 2px 8px rgba(64,158,255,.1); }
.result-item h3 { margin: 0 0 8px; font-size: 16px; color: #303133; }
.excerpt { color: #909399; font-size: 14px; margin: 0 0 8px; }
.meta { display: flex; gap: 16px; color: #909399; font-size: 13px; align-items: center; }
.meta .el-icon { margin-right: 2px; }
.pagination { text-align: center; margin-top: 20px; }
.empty { padding: 60px 0; }
</style>
