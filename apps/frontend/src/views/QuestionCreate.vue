<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Bell, UserFilled, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { clearAuthState, getCurrentUser } from '@/utils/auth'
import { categoryApi } from '@/api/categoryApi'
import { tagApi } from '@/api/tagApi'
import { questionApi } from '@/api/questionApi'
import { getActiveSensitiveWords, checkSensitiveWords } from '@/api/sensitiveWordPublic'

const router = useRouter()
const currentUser = getCurrentUser()

const isLoggedIn = computed(() => !!currentUser)

function logout() {
  clearAuthState()
  router.push('/login')
}

// Form data
const formRef = ref()
const form = ref({
  title: '',
  content: '',
  categoryId: null as number | null,
  tagIds: [] as number[],
})

// Form rules
const rules = {
  title: [
    { required: true, message: '请输入问题标题', trigger: 'blur' },
    { max: 200, message: '标题不能超过200字', trigger: 'blur' },
  ],
  content: [
    { required: true, message: '请输入问题内容', trigger: 'blur' },
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' },
  ],
}

// Categories
const categories = ref<any[]>([])
const categoryLoading = ref(false)

async function loadCategories() {
  categoryLoading.value = true
  try {
    const data = await categoryApi.list()
    // Build tree structure
    const buildTree = (items: any[], parentId: number | null = null): any[] => {
      return items
        .filter((item: any) => item.parentId === parentId)
        .map((item: any) => ({
          value: item.id,
          label: item.name,
          children: buildTree(items, item.id),
        }))
        .map((item: any) => {
          if (item.children.length === 0) delete item.children
          return item
        })
    }
    categories.value = buildTree(Array.isArray(data) ? data : [])
  } catch {
    ElMessage.error('加载分类失败')
  } finally {
    categoryLoading.value = false
  }
}

// Tags
const allTags = ref<any[]>([])
const recommendedTags = ref<any[]>([])

async function loadAllTags() {
  try {
    const data = await tagApi.list()
    allTags.value = Array.isArray(data) ? data : []
  } catch {
    // ignore
  }
}

let debounceTimer: ReturnType<typeof setTimeout> | null = null

watch(
  () => form.value.title,
  (val) => {
    if (debounceTimer) clearTimeout(debounceTimer)
    if (!val || val.trim().length < 2) {
      recommendedTags.value = []
      return
    }
    debounceTimer = setTimeout(async () => {
      try {
        const data = await tagApi.recommend(val.trim())
        recommendedTags.value = Array.isArray(data) ? data : []
      } catch {
        // ignore
      }
    }, 500)
  },
)

// Invite - placeholder
const inviteSearch = ref('')

// Sensitive word check
const sensitiveWords = ref<string[]>([])
const sensitiveWarning = ref('')

async function loadSensitiveWords() {
  sensitiveWords.value = await getActiveSensitiveWords()
}

function checkContent() {
  const allText = (form.value.title || '') + ' ' + (form.value.content || '')
  const hits = checkSensitiveWords(allText, sensitiveWords.value)
  if (hits.length > 0) {
    sensitiveWarning.value = `内容包含敏感词：${hits.slice(0, 3).join('、')}，请修改后再提交`
  } else {
    sensitiveWarning.value = ''
  }
}

watch(() => form.value.title, () => checkContent())
watch(() => form.value.content, () => checkContent())

// Submit
const submitting = ref(false)

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    // Frontend sensitive word check before submit
    if (sensitiveWarning.value) {
      ElMessage.error('内容包含不当用语，请修改')
      submitting.value = false
      return
    }
    const res: any = await questionApi.create({
      title: form.value.title,
      content: form.value.content,
      categoryId: form.value.categoryId,
      tagIds: form.value.tagIds,
    })
    ElMessage.success('问题发布成功')
    router.push(`/question/${res.id}`)
  } catch (err: any) {
    const msg = err?.message || '发布失败'
    if (msg.includes('敏感') || msg.includes('不当')) {
      ElMessage.error('内容包含不当用语，请修改')
    } else {
      ElMessage.error(msg)
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadCategories()
  loadAllTags()
  loadSensitiveWords()
})
</script>

<template>
  <div class="create-page">
    <!-- Navigation Bar -->
    <header class="create-header">
      <div class="header-inner">
        <div class="header-left">
          <router-link to="/" class="logo">知问社区</router-link>
          <nav class="header-nav">
            <router-link to="/" class="nav-link">首页</router-link>
          </nav>
        </div>
        <div class="header-right">
          <template v-if="isLoggedIn">
            <el-badge is-dot>
              <el-button :icon="Bell" circle />
            </el-badge>
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

    <!-- Main -->
    <main class="create-main">
      <div class="create-container">
        <h2 class="create-title">发布问题</h2>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          label-position="top"
          class="create-form"
        >
          <el-form-item label="问题标题" prop="title">
            <el-input
              v-model="form.title"
              placeholder="请输入问题标题，简洁明了地总结你的问题"
              maxlength="200"
              show-word-limit
              size="large"
            />
            <el-alert
              v-if="sensitiveWarning"
              :title="sensitiveWarning"
              type="warning"
              :closable="false"
              show-icon
              style="margin-top: 8px"
            />
          </el-form-item>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="选择分类" prop="categoryId">
                <el-tree-select
                  v-model="form.categoryId"
                  :data="categories"
                  :loading="categoryLoading"
                  placeholder="请选择分类"
                  check-strictly
                  clearable
                  filterable
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="添加标签">
                <el-select
                  v-model="form.tagIds"
                  multiple
                  filterable
                  placeholder="选择或搜索标签"
                  style="width: 100%"
                >
                  <el-option-group v-if="recommendedTags.length" label="推荐标签">
                    <el-option
                      v-for="tag in recommendedTags"
                      :key="tag.id"
                      :label="tag.name"
                      :value="tag.id"
                    />
                  </el-option-group>
                  <el-option-group label="全部标签">
                    <el-option
                      v-for="tag in allTags"
                      :key="tag.id"
                      :label="tag.name"
                      :value="tag.id"
                    />
                  </el-option-group>
                </el-select>
                <div v-if="recommendedTags.length" class="tag-hint">
                  根据标题推荐：
                  <el-tag
                    v-for="tag in recommendedTags.slice(0, 5)"
                    :key="tag.id"
                    size="small"
                    type="primary"
                    class="rec-tag"
                    @click="form.tagIds.includes(tag.id) ? null : form.tagIds.push(tag.id)"
                  >
                    {{ tag.name }}
                  </el-tag>
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="问题内容" prop="content">
            <el-input
              v-model="form.content"
              type="textarea"
              :rows="12"
              placeholder="请详细描述你的问题，包括背景、遇到的问题、尝试过的方法等..."
              resize="vertical"
            />
          </el-form-item>

          <el-form-item label="邀请回答">
            <el-input
              v-model="inviteSearch"
              placeholder="搜索想要邀请的用户（即将开放）"
              disabled
            />
            <p class="form-hint">邀请功能将在后续版本中开放</p>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="submitting"
              :icon="Plus"
              @click="handleSubmit"
            >
              发布问题
            </el-button>
            <el-button size="large" @click="router.push('/')">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </main>

    <!-- Footer -->
    <footer class="create-footer">
      <p>&copy; 2026 知问社区 - 技术问答与知识分享平台</p>
    </footer>
  </div>
</template>

<style scoped>
.create-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--c-bg);
}

/* Header */
.create-header {
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

.nav-link:hover {
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

/* Main */
.create-main {
  flex: 1;
  max-width: 860px;
  width: 100%;
  margin: 0 auto;
  padding: 32px 24px;
}

.create-container {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 32px;
  box-shadow: var(--shadow-sm);
}

.create-title {
  margin: 0 0 28px;
  font-size: 22px;
  font-weight: 700;
  color: var(--c-ink);
}

.create-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--c-ink-light);
}

.tag-hint {
  margin-top: 8px;
  font-size: 13px;
  color: var(--c-muted);
  line-height: 2;
}

.rec-tag {
  margin-left: 4px;
  cursor: pointer;
}

.form-hint {
  margin: 4px 0 0;
  font-size: 12px;
  color: var(--c-muted-light);
}

/* Footer */
.create-footer {
  text-align: center;
  padding: 24px;
  color: var(--c-muted);
  font-size: 13px;
  border-top: 1px solid var(--c-line);
}

.create-footer p {
  margin: 0;
}
</style>
