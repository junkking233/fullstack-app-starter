<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  Bell, UserFilled, View, ChatLineSquare, Plus, Search
} from '@element-plus/icons-vue'
import { clearAuthState, getCurrentUser } from '@/utils/auth'
import { questionApi } from '@/api/questionApi'
import { categoryApi } from '@/api/categoryApi'

const router = useRouter()
const currentUser = getCurrentUser()

const isLoggedIn = computed(() => !!currentUser)
const isAdmin = computed(() => currentUser?.role === 'ADMIN')

function logout() {
  clearAuthState()
  router.push('/login')
}

// Questions
const questions = ref<any[]>([])
const loading = ref(false)
const tab = ref('recommend')
const page = ref(1)
const pageSize = 10
const total = ref(0)
const categoryFilter = ref<number | null>(null)
const keyword = ref('')

const tabs = [
  { label: '推荐', value: 'recommend' },
  { label: '热门', value: 'hot' },
  { label: '最新', value: 'latest' },
]

async function loadQuestions() {
  loading.value = true
  try {
    const params: any = {
      page: page.value,
      pageSize,
      sort: tab.value,
    }
    if (categoryFilter.value) {
      params.categoryId = categoryFilter.value
    }
    if (keyword.value.trim()) {
      params.keyword = keyword.value.trim()
    }
    const res: any = await questionApi.list(params)
    const list = res?.records || res?.list || []
    questions.value = list
    total.value = res?.total || 0
  } catch {
    questions.value = []
  } finally {
    loading.value = false
  }
}

function handleTabChange(val: string) {
  tab.value = val
  page.value = 1
  loadQuestions()
}

function handlePageChange(p: number) {
  page.value = p
  loadQuestions()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function handleSearch() {
  page.value = 1
  loadQuestions()
}

function goToDetail(id: number) {
  router.push(`/question/${id}`)
}

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  const minutes = Math.floor(diff / 60000)
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  const hours = Math.floor(minutes / 60)
  if (hours < 24) return `${hours}小时前`
  const days = Math.floor(hours / 24)
  if (days < 30) return `${days}天前`
  return d.toLocaleDateString('zh-CN')
}

function getExcerpt(content: string, maxLen = 120) {
  if (!content) return ''
  const text = content.replace(/<[^>]*>/g, '').replace(/[#*`>\n\r]/g, ' ').trim()
  return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

// Categories sidebar
const categories = ref<any[]>([])
const categoriesLoading = ref(false)

async function loadCategories() {
  categoriesLoading.value = true
  try {
    const data = await categoryApi.list()
    const buildTree = (items: any[], parentId: number | null = null): any[] => {
      return items
        .filter((item: any) => item.parentId === parentId)
        .map((item: any) => ({
          ...item,
          children: buildTree(items, item.id),
        }))
    }
    categories.value = buildTree(Array.isArray(data) ? data : [])
  } catch {
    // ignore
  } finally {
    categoriesLoading.value = false
  }
}

function selectCategory(catId: number | null) {
  categoryFilter.value = categoryFilter.value === catId ? null : catId
  page.value = 1
  loadQuestions()
}

function isCategoryActive(catId: number) {
  return categoryFilter.value === catId
}

onMounted(() => {
  loadQuestions()
  loadCategories()
})
</script>

<template>
  <div class="home-page">
    <!-- Navigation Bar -->
    <header class="home-header">
      <div class="header-inner">
        <div class="header-left">
          <router-link to="/" class="logo">知问社区</router-link>
          <nav class="header-nav">
            <router-link to="/" class="nav-link active">首页</router-link>
            <router-link to="/hot" class="nav-link">热榜</router-link>
            <router-link to="/featured" class="nav-link">精华区</router-link>
          </nav>
        </div>
        <div class="header-right">
          <template v-if="isLoggedIn">
            <router-link to="/notifications">
              <el-badge is-dot>
                <el-button :icon="Bell" circle />
              </el-badge>
            </router-link>
            <router-link to="/profile" class="user-link">
              <el-avatar :size="32" class="user-avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="username">{{ currentUser?.nickname }}</span>
            </router-link>
            <el-button text type="danger" size="small" @click="logout">退出</el-button>
          </template>
          <template v-else>
            <el-button text size="default" @click="router.push('/login')">登录</el-button>
            <el-button type="primary" size="small" @click="router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="home-main">
      <div class="home-layout">
        <!-- Left: Question Feed -->
        <div class="home-feed">
          <!-- Toolbar -->
          <div class="feed-toolbar">
            <div class="toolbar-left">
              <el-tabs v-model="tab" @tab-change="handleTabChange">
                <el-tab-pane
                  v-for="t in tabs"
                  :key="t.value"
                  :label="t.label"
                  :name="t.value"
                />
              </el-tabs>
            </div>
            <div class="toolbar-right">
              <el-input
                v-model="keyword"
                placeholder="搜索问题..."
                :prefix-icon="Search"
                size="default"
                clearable
                style="width: 220px"
                @keyup.enter="handleSearch"
                @clear="handleSearch"
              />
              <el-button
                type="primary"
                :icon="Plus"
                @click="router.push('/question/create')"
              >
                提问
              </el-button>
            </div>
          </div>

          <!-- Question List -->
          <div v-loading="loading" class="question-list">
            <div v-if="questions.length === 0 && !loading" class="empty-list">
              <el-empty description="暂无问题" />
            </div>

            <div
              v-for="q in questions"
              :key="q.id"
              class="question-item"
              @click="goToDetail(q.id)"
            >
              <div class="question-item-main">
                <h3 class="question-item-title">
                  <el-tag v-if="q.isPinned" type="danger" size="small" class="title-badge">置顶</el-tag>
                  <el-tag v-if="q.isFeatured" type="warning" size="small" class="title-badge">精华</el-tag>
                  {{ q.title }}
                </h3>
                <p class="question-item-excerpt">{{ getExcerpt(q.content) }}</p>
                <div class="question-item-meta">
                  <span class="meta-author">
                    <el-avatar :size="20" class="tiny-avatar">
                      <el-icon><UserFilled /></el-icon>
                    </el-avatar>
                    {{ q.author?.nickname || '匿名' }}
                  </span>
                  <span v-if="q.category" class="meta-category">
                    <el-tag size="small" type="primary">{{ q.category.name }}</el-tag>
                  </span>
                  <span class="meta-stat">
                    <el-icon><ChatLineSquare /></el-icon>
                    {{ q.answerCount || 0 }} 回答
                  </span>
                  <span class="meta-stat">
                    <el-icon><View /></el-icon>
                    {{ q.viewCount || 0 }} 浏览
                  </span>
                  <span class="meta-time">{{ formatDate(q.createdAt) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <div v-if="total > pageSize" class="pagination-bar">
            <el-pagination
              v-model:current-page="page"
              :page-size="pageSize"
              :total="total"
              layout="prev, pager, next"
              @current-change="handlePageChange"
            />
          </div>
        </div>

        <!-- Right: Sidebar -->
        <aside class="home-sidebar">
          <el-card class="sidebar-card" v-loading="categoriesLoading">
            <template #header>
              <span class="sidebar-card-title">问题分类</span>
            </template>
            <div class="category-list">
              <div
                class="category-item"
                :class="{ active: categoryFilter === null && !categoryFilter }"
                @click="selectCategory(null)"
              >
                全部
              </div>
              <template v-for="cat in categories" :key="cat.id">
                <div
                  class="category-item parent"
                  :class="{ active: isCategoryActive(cat.id) }"
                  @click="selectCategory(cat.id)"
                >
                  {{ cat.name }}
                </div>
                <div
                  v-for="child in cat.children"
                  :key="child.id"
                  class="category-item child"
                  :class="{ active: isCategoryActive(child.id) }"
                  @click="selectCategory(child.id)"
                >
                  {{ child.name }}
                </div>
              </template>
            </div>
          </el-card>

          <el-card class="sidebar-card quick-links">
            <template #header>
              <span class="sidebar-card-title">快捷入口</span>
            </template>
            <div class="quick-link" @click="router.push('/question/create')">
              <el-icon><Plus /></el-icon>
              <span>发布问题</span>
            </div>
            <div v-if="isAdmin" class="quick-link" @click="router.push('/admin/dashboard')">
              <el-icon><UserFilled /></el-icon>
              <span>管理后台</span>
            </div>
          </el-card>
        </aside>
      </div>
    </main>

    <!-- Footer -->
    <footer class="home-footer">
      <p>&copy; 2026 知问社区 - 技术问答与知识分享平台</p>
    </footer>
  </div>
</template>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--c-bg);
}

/* Header */
.home-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--c-surface);
  border-bottom: 1px solid var(--c-line);
  box-shadow: var(--shadow-xs);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 32px;
}

.logo {
  font-size: 20px;
  font-weight: 800;
  color: var(--c-primary);
  text-decoration: none;
  letter-spacing: -0.3px;
}

.header-nav {
  display: flex;
  gap: 8px;
}

.nav-link {
  padding: 6px 14px;
  color: var(--c-body);
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.nav-link:hover,
.nav-link.active {
  color: var(--c-primary);
  background: var(--c-primary-bg);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--c-body);
  text-decoration: none;
  font-size: 14px;
}

.user-link:hover {
  color: var(--c-primary);
}

.user-avatar {
  background: var(--c-primary-bg);
  color: var(--c-primary);
}

.username {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* Main Layout */
.home-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
}

.home-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 260px;
  gap: 20px;
}

/* Feed */
.feed-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 8px 20px;
  box-shadow: var(--shadow-sm);
}

.toolbar-left :deep(.el-tabs__header) {
  margin-bottom: 0;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* Question List */
.question-list {
  min-height: 300px;
}

.question-item {
  display: flex;
  gap: 16px;
  padding: 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-xs);
}

.question-item:hover {
  border-color: var(--c-primary);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.question-item-main {
  flex: 1;
  min-width: 0;
}

.question-item-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--c-ink-light);
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.title-badge {
  margin-right: 4px;
  vertical-align: middle;
}

.question-item-excerpt {
  margin: 0 0 12px;
  font-size: 14px;
  color: var(--c-muted);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.question-item-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  font-size: 13px;
  color: var(--c-muted-light);
}

.meta-author {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  color: var(--c-body);
}

.tiny-avatar {
  background: var(--c-primary-bg);
  color: var(--c-primary);
}

.meta-stat {
  display: flex;
  align-items: center;
  gap: 3px;
}

.meta-time {
  margin-left: auto;
}

.empty-list {
  display: flex;
  justify-content: center;
  padding: 48px 0;
}

/* Sidebar */
.home-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sidebar-card {
  box-shadow: var(--shadow-sm);
}

.sidebar-card-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--c-ink);
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.category-item {
  padding: 8px 12px;
  font-size: 14px;
  color: var(--c-body);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.category-item:hover {
  background: var(--c-primary-bg);
  color: var(--c-primary);
}

.category-item.active {
  background: var(--c-primary-bg);
  color: var(--c-primary);
  font-weight: 600;
}

.category-item.parent {
  font-weight: 600;
  margin-top: 4px;
}

.category-item.child {
  padding-left: 28px;
  font-size: 13px;
}

.quick-links .quick-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  font-size: 14px;
  color: var(--c-primary);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.quick-links .quick-link:hover {
  background: var(--c-primary-bg);
}

/* Footer */
.home-footer {
  text-align: center;
  padding: 24px;
  color: var(--c-muted);
  font-size: 13px;
  border-top: 1px solid var(--c-line);
}

.home-footer p {
  margin: 0;
}

@media (max-width: 860px) {
  .home-layout {
    grid-template-columns: 1fr;
  }

  .home-sidebar {
    display: none;
  }

  .feed-toolbar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .toolbar-right {
    flex-wrap: wrap;
  }
}
</style>
