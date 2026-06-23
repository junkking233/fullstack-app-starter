<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { reviewApi } from '@/api/questionApi'

type ReviewTab = 'questions' | 'answers'

const activeTab = ref<ReviewTab>('questions')

// Questions table
const questionList = ref<any[]>([])
const questionLoading = ref(false)
const questionPage = ref(1)
const questionPageSize = 10
const questionTotal = ref(0)

async function loadQuestions() {
  questionLoading.value = true
  try {
    const res: any = await reviewApi.questionList({
      page: questionPage.value,
      pageSize: questionPageSize,
    })
    questionList.value = res?.records || res?.list || []
    questionTotal.value = res?.total || 0
  } catch {
    ElMessage.error('加载待审核问题失败')
  } finally {
    questionLoading.value = false
  }
}

async function approveQuestion(id: number) {
  try {
    await reviewApi.approveQuestion(id)
    ElMessage.success('已通过审核')
    await loadQuestions()
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Reject dialog
const rejectDialogVisible = ref(false)
const rejectTargetId = ref<number | null>(null)
const rejectType = ref<'question' | 'answer'>('question')
const rejectReason = ref('')

function openRejectQuestion(id: number) {
  rejectTargetId.value = id
  rejectType.value = 'question'
  rejectReason.value = ''
  rejectDialogVisible.value = true
}

async function confirmReject() {
  if (!rejectReason.value.trim()) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  try {
    if (rejectType.value === 'question') {
      await reviewApi.rejectQuestion(rejectTargetId.value!, rejectReason.value.trim())
    } else {
      await reviewApi.rejectAnswer(rejectTargetId.value!, rejectReason.value.trim())
    }
    ElMessage.success('已拒绝')
    rejectDialogVisible.value = false
    if (rejectType.value === 'question') {
      await loadQuestions()
    } else {
      await loadAnswers()
    }
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

// Answers table
const answerList = ref<any[]>([])
const answerLoading = ref(false)
const answerPage = ref(1)
const answerPageSize = 10
const answerTotal = ref(0)

async function loadAnswers() {
  answerLoading.value = true
  try {
    const res: any = await reviewApi.answerList({
      page: answerPage.value,
      pageSize: answerPageSize,
    })
    answerList.value = res?.records || res?.list || []
    answerTotal.value = res?.total || 0
  } catch {
    ElMessage.error('加载待审核回答失败')
  } finally {
    answerLoading.value = false
  }
}

async function approveAnswer(id: number) {
  try {
    await reviewApi.approveAnswer(id)
    ElMessage.success('已通过审核')
    await loadAnswers()
  } catch (err: any) {
    ElMessage.error(err?.message || '操作失败')
  }
}

function openRejectAnswer(id: number) {
  rejectTargetId.value = id
  rejectType.value = 'answer'
  rejectReason.value = ''
  rejectDialogVisible.value = true
}

function handleTabChange(tab: string) {
  if (tab === 'questions') {
    loadQuestions()
  } else {
    loadAnswers()
  }
}

function getContentPreview(content: string, maxLen = 80) {
  if (!content) return ''
  const text = content.replace(/<[^>]*>/g, '').replace(/[#*`>\n\r]/g, ' ').trim()
  return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleString('zh-CN')
}

onMounted(() => {
  loadQuestions()
})
</script>

<template>
  <div class="review-page">
    <div class="page-toolbar">
      <h2>内容审核</h2>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <!-- Questions Tab -->
      <el-tab-pane label="问题审核" name="questions">
        <el-table
          v-loading="questionLoading"
          :data="questionList"
          stripe
          style="width: 100%"
          border
        >
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column label="内容预览" min-width="250">
            <template #default="{ row }">
              <span class="content-preview">{{ getContentPreview(row.content, 60) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="作者" width="120">
            <template #default="{ row }">
              {{ row.author?.nickname || '匿名' }}
            </template>
          </el-table-column>
          <el-table-column label="提交时间" width="170">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="approveQuestion(row.id)">
                通过
              </el-button>
              <el-button type="danger" size="small" @click="openRejectQuestion(row.id)">
                拒绝
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="questionTotal > questionPageSize" class="pagination-bar">
          <el-pagination
            v-model:current-page="questionPage"
            :page-size="questionPageSize"
            :total="questionTotal"
            layout="total, prev, pager, next"
            @current-change="loadQuestions"
          />
        </div>

        <el-empty v-if="questionList.length === 0 && !questionLoading" description="暂无待审核问题" />
      </el-tab-pane>

      <!-- Answers Tab -->
      <el-tab-pane label="回答审核" name="answers">
        <el-table
          v-loading="answerLoading"
          :data="answerList"
          stripe
          style="width: 100%"
          border
        >
          <el-table-column prop="id" label="ID" width="70" />
          <el-table-column label="内容预览" min-width="350">
            <template #default="{ row }">
              <span class="content-preview">{{ getContentPreview(row.content, 80) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="作者" width="120">
            <template #default="{ row }">
              {{ row.author?.nickname || '匿名' }}
            </template>
          </el-table-column>
          <el-table-column label="提交时间" width="170">
            <template #default="{ row }">
              {{ formatDate(row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="approveAnswer(row.id)">
                通过
              </el-button>
              <el-button type="danger" size="small" @click="openRejectAnswer(row.id)">
                拒绝
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="answerTotal > answerPageSize" class="pagination-bar">
          <el-pagination
            v-model:current-page="answerPage"
            :page-size="answerPageSize"
            :total="answerTotal"
            layout="total, prev, pager, next"
            @current-change="loadAnswers"
          />
        </div>

        <el-empty v-if="answerList.length === 0 && !answerLoading" description="暂无待审核回答" />
      </el-tab-pane>
    </el-tabs>

    <!-- Reject Dialog -->
    <el-dialog
      v-model="rejectDialogVisible"
      :title="rejectType === 'question' ? '拒绝问题' : '拒绝回答'"
      width="500px"
    >
      <el-form>
        <el-form-item label="拒绝原因" required>
          <el-input
            v-model="rejectReason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.review-page {
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  padding: 22px;
}

.content-preview {
  color: var(--c-muted);
  font-size: 13px;
}
</style>
