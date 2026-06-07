<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { UserFilled, Flag, List, Trophy, ChatLineRound, Star } from '@element-plus/icons-vue';
import { chartApi } from '@/api/chartApi';

const stats = ref({ totalUsers: 0, totalTeams: 0, totalMatches: 0, totalCities: 0, pendingComments: 0, favorites: 0 });
onMounted(async () => {
  stats.value = await chartApi.dashboardStats();
});

const statCards = [
  { key: 'totalUsers' as const, label: '用户数', icon: UserFilled, color: '#3b82f6', bg: '#eff6ff' },
  { key: 'totalTeams' as const, label: '球队数', icon: Flag, color: '#0d9488', bg: '#f0fdfa' },
  { key: 'totalMatches' as const, label: '比赛数', icon: List, color: '#f59e0b', bg: '#fffbeb' },
  { key: 'totalCities' as const, label: '主办城市', icon: Trophy, color: '#2563eb', bg: '#eff6ff' },
  { key: 'pendingComments' as const, label: '待审核评论', icon: ChatLineRound, color: '#ef4444', bg: '#fef2f2' },
  { key: 'favorites' as const, label: '收藏数', icon: Star, color: '#f97316', bg: '#fff7ed' },
];
</script>

<template>
  <div class="dashboard-grid">
    <el-card v-for="card in statCards" :key="card.key" class="stat-card" :body-style="{ padding: '20px' }">
      <div class="stat-icon" :style="{ background: card.bg, color: card.color }">
        <el-icon :size="24"><component :is="card.icon" /></el-icon>
      </div>
      <div class="stat-info">
        <span>{{ card.label }}</span>
        <strong>{{ stats[card.key] }}</strong>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  display: flex;
  width: 52px;
  height: 52px;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  flex-shrink: 0;
  transition: transform 0.2s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.08);
}

.stat-info {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 6px;
}

.stat-info span {
  color: var(--c-muted);
  font-size: 14px;
}

.stat-info strong {
  color: var(--c-ink);
  font-size: 28px;
  font-weight: 700;
  line-height: 1;
}

@media (max-width: 860px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}
</style>
