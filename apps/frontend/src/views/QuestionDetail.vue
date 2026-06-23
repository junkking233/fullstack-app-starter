<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Bell, UserFilled, View, Star, Check, Delete, Edit,
  ArrowUp, ArrowDown, ChatDotRound, Promotion, Collection
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { clearAuthState, getCurrentUser } from '@/utils/auth'
import { questionApi, adminQuestionApi } from '@/api/questionApi'
import { answerApi } from '@/api/answerApi'
import { commentApi } from '@/api/commentApi'
import { voteApi } from '@/api/voteApi'
import { favoriteApi } from '@/api/favoriteApi'
import { reportApi } from '@/api/reportApi'
import { getActiveSensitiveWords, checkSensitiveWords } from '@/api/sensitiveWordPublic'

const route = useRoute()
const router = useRouter()
const currentUser = getCurrentUser()

const isLoggedIn = computed(() => !!currentUser)

function logout() {
  clearAuthState()
  router.push('/login')
}

// Question
const question = ref<any>(null)
const loading = ref(false)

async function loadQuestion() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    question.value = await questionApi.detail(id)
  } catch {
    ElMessage.error('加载问题失败')
  } finally {
    loading.value = false
  }
}

const isAuthor = computed(() =>
  currentUser && question.value ? currentUser.id === question.value.author?.id : false
)
const isAdmin = computed(() => currentUser?.role === 'ADMIN')

// Vote
const hasVoted = ref(false)
async function toggleVote() {
  if (!currentUser) { router.push('/login'); return }
  try {
    const res: any = await voteApi.toggle({ targetType: 'question', targetId: question.value.id })
    hasVoted.value = res
    question.value.voteCount = (question.value.voteCount || 0) + (res ? 1 : -1)
  } catch (err: any) { ElMessage.error(err?.message || '操作失败') }
}

// Favorite
const hasFavorited = ref(false)
async function toggleFavorite() {
  if (!currentUser) { router.push('/login'); return }
  try {
    const res: any = await favoriteApi.toggle(question.value.id)
    hasFavorited.value = res
    question.value.favoriteCount = (question.value.favoriteCount || 0) + (res ? 1 : -1)
    ElMessage.success(res ? '已收藏' : '已取消收藏')
  } catch (err: any) { ElMessage.error(err?.message || '操作失败') }
}

// Report
const reportDialogVisible = ref(false)
const reportForm = ref({ reason: '', detail: '' })
async function submitReport() {
  if (!reportForm.value.reason) { ElMessage.warning('请选择举报原因'); return }
  try {
    await reportApi.create({
      targetType: 'question', targetId: question.value.id,
      reason: reportForm.value.reason, detail: reportForm.value.detail
    })
    ElMessage.success('举报已提交，管理员会尽快处理')
    reportDialogVisible.value = false
    reportForm.value = { reason: '', detail: '' }
  } catch (err: any) { ElMessage.error(err?.message || '举报失败') }
}

// Related questions
const relatedQuestions = ref<any[]>([])
async function loadRelated() {
  try {
    const data: any = await questionApi.related(question.value.id)
    relatedQuestions.value = Array.isArray(data) ? data : []
  } catch { /* ignore */ }
}

// Sensitive word check for answer
const sensitiveWords = ref<string[]>([])
const answerSensitiveWarning = ref('')
function checkAnswerContent() {
  const hits = checkSensitiveWords(answerContent.value, sensitiveWords.value)
  answerSensitiveWarning.value = hits.length > 0 ? `包含敏感词：${hits.slice(0, 3).join('、')}` : ''
}

// Delete question
async function handleDeleteQuestion() {
  try {
    await ElMessageBox.confirm('确定要删除该问题吗？此操作不可恢复。', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await questionApi.delete(question.value.id)
    ElMessage.success('问题已删除')
    router.push('/')
  } catch {
    // cancelled
  }
}

// Toggle pin/feature
async function togglePin() {
  try {
    await adminQuestionApi.pin(question.value.id)
    question.value.isPinned = !question.value.isPinned
    ElMessage.success(question.value.isPinned ? '已置顶' : '已取消置顶')
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

async function toggleFeature() {
  try {
    await adminQuestionApi.feature(question.value.id)
    question.value.isFeatured = !question.value.isFeatured
    ElMessage.success(question.value.isFeatured ? '已设为精华' : '已取消精华')
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Answers
const answers = ref<any[]>([])
const answersLoading = ref(false)
const answerPage = ref(1)
const answerTotal = ref(0)

async function loadAnswers() {
  answersLoading.value = true
  try {
    const res: any = await answerApi.list(question.value.id, {
      page: answerPage.value,
      pageSize: 10,
    })
    const list = res?.records || res?.list || []
    answerTotal.value = res?.total || 0

    // Sort: accepted first
    list.sort((a: any, b: any) => {
      if (a.isAccepted && !b.isAccepted) return -1
      if (!a.isAccepted && b.isAccepted) return 1
      return (b.voteCount || 0) - (a.voteCount || 0)
    })

    answers.value = list
  } catch {
    ElMessage.error('加载回答失败')
  } finally {
    answersLoading.value = false
  }
}

// Add answer
const answerContent = ref('')
const answerSubmitting = ref(false)

async function handleAddAnswer() {
  if (!answerContent.value.trim()) {
    ElMessage.warning('请输入回答内容')
    return
  }
  answerSubmitting.value = true
  try {
    await answerApi.create(question.value.id, { content: answerContent.value })
    ElMessage.success('回答发布成功')
    answerContent.value = ''
    await loadAnswers()
  } catch (err: any) {
    const msg = err?.message || '发布失败'
    if (msg.includes('敏感') || msg.includes('不当')) {
      ElMessage.error('内容包含不当用语，请修改')
    } else {
      ElMessage.error(msg)
    }
  } finally {
    answerSubmitting.value = false
  }
}

// Accept answer
async function handleAccept(answerId: number) {
  try {
    await answerApi.accept(answerId)
    ElMessage.success('已采纳该回答')
    await loadAnswers()
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Delete answer
async function handleDeleteAnswer(answerId: number) {
  try {
    await ElMessageBox.confirm('确定要删除该回答吗？', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await answerApi.delete(answerId)
    ElMessage.success('回答已删除')
    await loadAnswers()
  } catch {
    // cancelled
  }
}

// Comments
const commentVisibleMap = ref<Record<number, boolean>>({})
const commentMap = ref<Record<number, any[]>>({})
const commentInputMap = ref<Record<number, string>>({})
const replyTargetMap = ref<Record<number, { id: number; nickname: string } | null>>({})
const commentLoadingMap = ref<Record<number, boolean>>({})
const commentSubmittingMap = ref<Record<number, boolean>>({})

function toggleComments(answerId: number) {
  commentVisibleMap.value = { ...commentVisibleMap.value, [answerId]: !commentVisibleMap.value[answerId] }
  if (commentVisibleMap.value[answerId] && !commentMap.value[answerId]) {
    loadComments(answerId)
  }
}

async function loadComments(answerId: number) {
  commentLoadingMap.value[answerId] = true
  try {
    const data = await commentApi.list(answerId)
    commentMap.value[answerId] = Array.isArray(data) ? data : []
  } catch {
    ElMessage.error('加载评论失败')
  } finally {
    commentLoadingMap.value[answerId] = false
  }
}

function startReply(answerId: number, comment: { id: number; user?: any; author?: any }) {
  const user = comment.user || comment.author
  replyTargetMap.value[answerId] = { id: comment.id, nickname: user?.nickname || '用户' }
}

function cancelReply(answerId: number) {
  replyTargetMap.value[answerId] = null
}

async function handleAddComment(answerId: number) {
  const content = commentInputMap.value[answerId]
  if (!content?.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  commentSubmittingMap.value[answerId] = true
  try {
    const parentId = replyTargetMap.value[answerId]?.id
    await commentApi.create(answerId, { content: content.trim(), parentId })
    ElMessage.success('评论发布成功')
    commentInputMap.value[answerId] = ''
    replyTargetMap.value[answerId] = null
    await loadComments(answerId)
  } catch (err: any) {
    ElMessage.error(err?.message || '评论失败')
  } finally {
    commentSubmittingMap.value[answerId] = false
  }
}

async function handleDeleteComment(commentId: number, answerId: number) {
  try {
    await ElMessageBox.confirm('确定要删除该评论吗？', '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await commentApi.delete(commentId)
    ElMessage.success('评论已删除')
    await loadComments(answerId)
  } catch {
    // cancelled
  }
}

function getNestedComments(answerId: number) {
  const all = commentMap.value[answerId] || []
  const topLevel = all.filter((c: any) => !c.parentId)
  return topLevel.map((parent: any) => ({
    ...parent,
    replies: all.filter((c: any) => c.parentId === parent.id),
  }))
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

function canDeleteComment(comment: any) {
  if (!currentUser) return false
  if (currentUser.role === 'ADMIN') return true
  const user = comment.user || comment.author
  return user?.id === currentUser.id
}

function canDeleteAnswer(answer: any) {
  if (!currentUser) return false
  if (currentUser.role === 'ADMIN') return true
  return answer.author?.id === currentUser.id
}

onMounted(async () => {
  await loadQuestion()
  if (question.value) {
    await loadAnswers()
    loadRelated()
    if (currentUser) {
      try {
        hasVoted.value = await voteApi.check({ targetType: 'question', targetId: question.value.id }) as any
        hasFavorited.value = await favoriteApi.check(question.value.id) as any
      } catch { /* ignore */ }
      getActiveSensitiveWords().then(w => { sensitiveWords.value = w })
    }
  }
})
</script>

<template>
  <div class="detail-page" v-loading="loading">
    <!-- Navigation Bar -->
    <header class="detail-header">
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

    <main class="detail-main" v-if="question">
      <div class="detail-layout">
        <!-- Left: Content -->
        <div class="detail-content">
          <!-- Question Card -->
          <div class="question-card">
            <div class="question-header">
              <h1 class="question-title">
                <el-tag v-if="question.isPinned" type="danger" size="small" class="badge">置顶</el-tag>
                <el-tag v-if="question.isFeatured" type="warning" size="small" class="badge">精华</el-tag>
                {{ question.title }}
              </h1>
              <div class="question-meta">
                <span class="meta-item">
                  <el-avatar :size="24" class="mini-avatar">
                    <el-icon><UserFilled /></el-icon>
                  </el-avatar>
                  {{ question.author?.nickname || '匿名' }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ question.viewCount || 0 }} 浏览
                </span>
                <span class="meta-item">
                  {{ formatDate(question.createdAt) }}
                </span>
                <el-tag :type="getStatusType(question.status)" size="small">
                  {{ getStatusText(question.status) }}
                </el-tag>
              </div>
            </div>

            <div class="question-tags" v-if="question.tags?.length">
              <el-tag v-for="tag in question.tags" :key="tag.id" size="small" type="info">
                {{ tag.name }}
              </el-tag>
              <el-tag v-if="question.category" size="small" type="primary">
                {{ question.category.name }}
              </el-tag>
            </div>

            <div class="question-content" v-html="question.content?.replace(/\n/g, '<br>')"></div>

            <!-- Rejected reason -->
            <el-alert
              v-if="question.status === 'rejected' && question.rejectReason"
              :title="'拒绝原因：' + question.rejectReason"
              type="error"
              :closable="false"
              show-icon
              style="margin-top: 12px"
            />

            <div class="question-actions">
              <div class="action-left">
                <el-button :icon="ArrowUp" size="small" :type="hasVoted ? 'primary' : 'default'" @click="toggleVote">{{ question.voteCount || 0 }}</el-button>
                <el-button :icon="Star" size="small" :type="hasFavorited ? 'warning' : 'default'" @click="toggleFavorite">{{ hasFavorited ? '已收藏' : '收藏' }}</el-button>
                <el-button size="small" text type="info" @click="reportDialogVisible = true">举报</el-button>
              </div>
              <div class="action-right">
                <template v-if="isAuthor">
                  <el-button size="small" :icon="Edit">编辑</el-button>
                  <el-button size="small" type="danger" :icon="Delete" @click="handleDeleteQuestion">删除</el-button>
                </template>
                <template v-if="isAdmin">
                  <el-button
                    size="small"
                    :type="question.isPinned ? 'warning' : 'default'"
                    :icon="Promotion"
                    @click="togglePin"
                  >
                    {{ question.isPinned ? '取消置顶' : '置顶' }}
                  </el-button>
                  <el-button
                    size="small"
                    :type="question.isFeatured ? 'warning' : 'default'"
                    :icon="Collection"
                    @click="toggleFeature"
                  >
                    {{ question.isFeatured ? '取消精华' : '精华' }}
                  </el-button>
                  <el-button v-if="!isAuthor" size="small" type="danger" :icon="Delete" @click="handleDeleteQuestion">删除</el-button>
                </template>
              </div>
            </div>
          </div>

          <!-- Answers Section -->
          <div class="answers-section">
            <div class="answers-header">
              <h3>{{ answerTotal }} 个回答</h3>
            </div>

            <div v-loading="answersLoading">
              <div v-if="answers.length === 0" class="answers-empty">
                <el-empty description="暂无回答，来写下第一个回答吧" />
              </div>

              <div
                v-for="answer in answers"
                :key="answer.id"
                class="answer-card"
                :class="{ accepted: answer.isAccepted }"
              >
                <div class="answer-meta">
                  <el-avatar :size="36" class="answer-avatar">
                    <el-icon><UserFilled /></el-icon>
                  </el-avatar>
                  <div class="answer-author">
                    <span class="author-name">{{ answer.author?.nickname || '匿名' }}</span>
                    <span class="answer-time">{{ formatDate(answer.createdAt) }}</span>
                  </div>
                  <el-tag v-if="answer.isAccepted" type="success" size="small">已采纳</el-tag>
                </div>

                <div class="answer-body" v-html="answer.content?.replace(/\n/g, '<br>')"></div>

                <div class="answer-actions">
                  <div class="answer-action-left">
                    <el-button :icon="ArrowUp" size="small" text>{{ answer.voteCount || 0 }}</el-button>
                    <el-button :icon="ArrowDown" size="small" text />
                    <el-button
                      :icon="ChatDotRound"
                      size="small"
                      text
                      @click="toggleComments(answer.id)"
                    >
                      评论 ({{ answer.commentCount || 0 }})
                    </el-button>
                  </div>
                  <div class="answer-action-right">
                    <el-button
                      v-if="isAuthor && !answer.isAccepted"
                      type="success"
                      size="small"
                      :icon="Check"
                      @click="handleAccept(answer.id)"
                    >
                      采纳
                    </el-button>
                    <el-button
                      v-if="canDeleteAnswer(answer)"
                      type="danger"
                      size="small"
                      :icon="Delete"
                      text
                      @click="handleDeleteAnswer(answer.id)"
                    >
                      删除
                    </el-button>
                  </div>
                </div>

                <!-- Comments -->
                <div v-if="commentVisibleMap[answer.id]" class="answer-comments">
                  <div v-loading="commentLoadingMap.value[answer.id]">
                    <div v-if="getNestedComments(answer.id).length === 0" class="no-comments">
                      暂无评论
                    </div>

                    <div v-for="nested in getNestedComments(answer.id)" :key="nested.id" class="comment-thread">
                      <div class="comment-item">
                        <span class="comment-user">{{ nested.user?.nickname || nested.author?.nickname || '匿名' }}</span>
                        <span class="comment-colon">：</span>
                        <span class="comment-text">{{ nested.content }}</span>
                        <span class="comment-time">{{ formatDate(nested.createdAt) }}</span>
                        <el-button
                          v-if="isLoggedIn"
                          link
                          type="primary"
                          size="small"
                          @click="startReply(answer.id, nested)"
                        >
                          回复
                        </el-button>
                        <el-button
                          v-if="canDeleteComment(nested)"
                          link
                          type="danger"
                          size="small"
                          @click="handleDeleteComment(nested.id, answer.id)"
                        >
                          删除
                        </el-button>
                      </div>
                      <!-- Replies -->
                      <div v-for="reply in nested.replies" :key="reply.id" class="comment-item reply-item">
                        <span class="comment-user">{{ reply.user?.nickname || reply.author?.nickname || '匿名' }}</span>
                        <span class="comment-colon">：</span>
                        <span class="comment-text">{{ reply.content }}</span>
                        <span class="comment-time">{{ formatDate(reply.createdAt) }}</span>
                        <el-button
                          v-if="isLoggedIn"
                          link
                          type="primary"
                          size="small"
                          @click="startReply(answer.id, nested)"
                        >
                          回复
                        </el-button>
                        <el-button
                          v-if="canDeleteComment(reply)"
                          link
                          type="danger"
                          size="small"
                          @click="handleDeleteComment(reply.id, answer.id)"
                        >
                          删除
                        </el-button>
                      </div>
                    </div>

                    <!-- Add Comment -->
                    <div v-if="isLoggedIn" class="add-comment">
                      <span v-if="replyTargetMap.value[answer.id]" class="reply-hint">
                        回复 @{{ replyTargetMap.value[answer.id]?.nickname }}
                        <el-button link type="primary" size="small" @click="cancelReply(answer.id)">取消</el-button>
                      </span>
                      <el-input
                        v-model="commentInputMap.value[answer.id]"
                        type="textarea"
                        :rows="2"
                        placeholder="写下你的评论..."
                      />
                      <el-button
                        type="primary"
                        size="small"
                        :loading="commentSubmittingMap.value[answer.id]"
                        class="comment-btn"
                        @click="handleAddComment(answer.id)"
                      >
                        发表评论
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Add Answer -->
            <div class="add-answer" v-if="isLoggedIn">
              <h4>撰写回答</h4>
              <el-input
                v-model="answerContent"
                type="textarea"
                :rows="5"
                placeholder="写下你的回答..."
                resize="vertical"
                @input="checkAnswerContent"
              />
              <el-alert
                v-if="answerSensitiveWarning"
                :title="answerSensitiveWarning"
                type="warning"
                :closable="false"
                show-icon
                style="margin-top: 8px"
              />
              <div class="add-answer-actions">
                <el-button
                  type="primary"
                  :loading="answerSubmitting"
                  :disabled="!!answerSensitiveWarning"
                  @click="handleAddAnswer"
                >
                  发布回答
                </el-button>
              </div>
            </div>

            <div v-else class="login-prompt">
              <p>登录后才能回答问题</p>
              <el-button type="primary" @click="router.push('/login')">去登录</el-button>
            </div>
          </div>
        </div>

        <!-- Right: Sidebar -->
        <aside class="detail-sidebar">
          <el-card class="sidebar-card">
            <h4>问题统计</h4>
            <div class="stat-list">
              <div class="stat-item">
                <span class="stat-label">浏览</span>
                <span class="stat-value">{{ question.viewCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">回答</span>
                <span class="stat-value">{{ answerTotal }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">投票</span>
                <span class="stat-value">{{ question.voteCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">收藏</span>
                <span class="stat-value">{{ question.favoriteCount || 0 }}</span>
              </div>
            </div>
          </el-card>

          <el-card v-if="relatedQuestions.length" class="sidebar-card">
            <h4>相关问题</h4>
            <div class="related-list">
              <div
                v-for="rq in relatedQuestions"
                :key="rq.id"
                class="related-item"
                @click="router.push(`/question/${rq.id}`)"
              >
                {{ rq.title }}
              </div>
            </div>
          </el-card>
        </aside>
      </div>
    </main>

    <!-- Report Dialog -->
    <el-dialog v-model="reportDialogVisible" title="举报" width="420px">
      <el-form label-width="80px">
        <el-form-item label="举报原因">
          <el-select v-model="reportForm.reason" placeholder="请选择" style="width: 100%">
            <el-option label="广告" value="ad" />
            <el-option label="色情" value="porn" />
            <el-option label="辱骂" value="abuse" />
            <el-option label="侵权" value="copyright" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="补充说明">
          <el-input v-model="reportForm.detail" type="textarea" :rows="3" placeholder="可选" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReport">提交举报</el-button>
      </template>
    </el-dialog>

    <footer class="detail-footer">
      <p>&copy; 2026 知问社区 - 技术问答与知识分享平台</p>
    </footer>
  </div>
</template>

<style scoped>
.detail-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: var(--c-bg);
}

/* Header */
.detail-header {
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

/* Main Layout */
.detail-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
}

.detail-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 260px;
  gap: 20px;
}

/* Question Card */
.question-card {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 28px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 20px;
}

.question-header {
  margin-bottom: 16px;
}

.question-title {
  margin: 0 0 12px;
  font-size: 22px;
  font-weight: 700;
  color: var(--c-ink);
  line-height: 1.4;
}

.question-title .badge {
  margin-right: 6px;
  vertical-align: middle;
}

.question-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  color: var(--c-muted);
  font-size: 13px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.mini-avatar {
  background: var(--c-primary-bg);
  color: var(--c-primary);
  font-size: 12px;
}

.question-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.question-content {
  font-size: 15px;
  line-height: 1.8;
  color: var(--c-body);
  word-break: break-word;
}

.question-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--c-line);
}

.action-left,
.action-right {
  display: flex;
  gap: 8px;
}

/* Answers */
.answers-section {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 24px;
  box-shadow: var(--shadow-sm);
}

.answers-header {
  padding-bottom: 16px;
  margin-bottom: 16px;
  border-bottom: 1px solid var(--c-line);
}

.answers-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--c-ink);
}

.answers-empty {
  padding: 24px 0;
}

.answer-card {
  padding: 20px 0;
  border-bottom: 1px solid var(--c-line-light);
}

.answer-card:last-child {
  border-bottom: none;
}

.answer-card.accepted {
  background: var(--c-green-bg);
  margin: 0 -24px;
  padding: 20px 24px;
  border-radius: var(--radius-md);
}

.answer-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.answer-avatar {
  background: var(--c-primary-bg);
  color: var(--c-primary);
}

.answer-author {
  flex: 1;
}

.author-name {
  font-weight: 600;
  font-size: 14px;
  color: var(--c-ink-light);
}

.answer-time {
  display: block;
  font-size: 12px;
  color: var(--c-muted-light);
}

.answer-body {
  font-size: 15px;
  line-height: 1.8;
  color: var(--c-body);
  word-break: break-word;
}

.answer-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--c-line-light);
}

.answer-action-left,
.answer-action-right {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* Comments */
.answer-comments {
  margin-top: 12px;
  padding: 12px 16px;
  background: var(--c-bg);
  border-radius: var(--radius-md);
}

.comment-thread {
  margin-bottom: 8px;
}

.comment-item {
  padding: 6px 0;
  font-size: 13px;
  line-height: 1.6;
}

.comment-item.reply-item {
  padding-left: 24px;
  border-left: 2px solid var(--c-line);
  margin: 4px 0 4px 12px;
}

.comment-user {
  font-weight: 600;
  color: var(--c-primary);
}

.comment-colon {
  color: var(--c-muted-light);
}

.comment-text {
  color: var(--c-body);
}

.comment-time {
  margin-left: 8px;
  font-size: 12px;
  color: var(--c-muted-light);
}

.no-comments {
  color: var(--c-muted-light);
  font-size: 13px;
  padding: 12px 0;
}

.add-comment {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--c-line);
}

.reply-hint {
  display: block;
  margin-bottom: 6px;
  font-size: 13px;
  color: var(--c-muted);
}

.comment-btn {
  margin-top: 8px;
}

/* Add Answer */
.add-answer {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 2px solid var(--c-line);
}

.add-answer h4 {
  margin: 0 0 12px;
  font-size: 16px;
  font-weight: 600;
  color: var(--c-ink);
}

.add-answer-actions {
  margin-top: 12px;
}

.login-prompt {
  margin-top: 24px;
  text-align: center;
  padding: 32px 0;
}

.login-prompt p {
  margin: 0 0 12px;
  color: var(--c-muted);
  font-size: 14px;
}

/* Sidebar */
.sidebar-card {
  margin-bottom: 16px;
}

.sidebar-card h4 {
  margin: 0 0 12px;
  font-size: 15px;
  font-weight: 600;
  color: var(--c-ink);
}

.stat-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 13px;
  color: var(--c-muted);
}

.stat-value {
  font-size: 16px;
  font-weight: 700;
  color: var(--c-primary);
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.related-item {
  font-size: 13px;
  color: var(--c-body);
  cursor: pointer;
  padding: 6px 0;
  border-bottom: 1px solid var(--c-line-light);
  line-height: 1.5;
  transition: color var(--transition-fast);
}

.related-item:last-child {
  border-bottom: none;
}

.related-item:hover {
  color: var(--c-primary);
}

/* Footer */
.detail-footer {
  text-align: center;
  padding: 24px;
  color: var(--c-muted);
  font-size: 13px;
  border-top: 1px solid var(--c-line);
}

.detail-footer p {
  margin: 0;
}

@media (max-width: 860px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }

  .detail-sidebar {
    display: none;
  }
}
</style>
