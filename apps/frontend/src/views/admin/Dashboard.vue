<script setup lang="ts">
import { DataAnalysis, Document, UserFilled, QuestionFilled } from '@element-plus/icons-vue';

const stats = [
  { label: '用户总数', value: '--', icon: UserFilled, color: '#2f6bff' },
  { label: '问题总数', value: '--', icon: QuestionFilled, color: '#22c55e' },
  { label: '回答总数', value: '--', icon: Document, color: '#f59e0b' },
  { label: '待审核', value: '--', icon: DataAnalysis, color: '#ef4444' },
];
</script>

<template>
  <div class="dashboard-page">
    <div class="stats-grid">
      <el-card v-for="s in stats" :key="s.label" class="stat-card">
        <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }">
          <el-icon :size="24"><component :is="s.icon" /></el-icon>
        </div>
        <div class="stat-body">
          <div class="stat-value">{{ s.value }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </el-card>
    </div>

    <el-card class="placeholder-card">
      <p>仪表盘数据将在后续阶段接入统计接口后展示。</p>
    </el-card>
  </div>
</template>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px;
}

.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  flex-shrink: 0;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--c-ink);
}

.stat-label {
  margin-top: 2px;
  color: var(--c-muted);
  font-size: 13px;
}

.placeholder-card {
  padding: 12px;
  text-align: center;
  color: var(--c-muted);
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
