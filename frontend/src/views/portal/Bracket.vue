<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { Calendar } from '@element-plus/icons-vue';
import { bracketApi, teamApi } from '@/api/worldcupApi';
import type { Match, Team } from '@/types/worldcup';

const matches = ref<Match[]>([]);
const teams = ref<Team[]>([]);

const stageOrder = ['ROUND_OF_32', 'ROUND_OF_16', 'QUARTER_FINAL', 'SEMI_FINAL', 'THIRD_PLACE', 'FINAL'];

const orderedStages = computed(() => {
  const orderMap = new Map(stageOrder.map((s, i) => [s, i]));
  const stages = [...new Set(matches.value.map((item) => item.stage))];
  return stages.sort((a, b) => {
    const ia = orderMap.get(a) ?? 999;
    const ib = orderMap.get(b) ?? 999;
    return ia - ib;
  });
});

function getNextStage(stage: string) {
  const idx = stageOrder.indexOf(stage);
  return stageOrder[idx + 1];
}

function shouldShowBridge(stage: string) {
  const currCount = matches.value.filter((m) => m.stage === stage).length;
  const nextStage = getNextStage(stage);
  if (!nextStage) return false;
  const nextCount = matches.value.filter((m) => m.stage === nextStage).length;
  return currCount === nextCount * 2;
}

function isLastStage(stage: string) {
  return stage === orderedStages.value[orderedStages.value.length - 1];
}

function stageMatches(stage: string) {
  return matches.value
    .filter((m) => m.stage === stage)
    .sort((a, b) => a.matchNo - b.matchNo);
}

onMounted(async () => {
  [matches.value, teams.value] = await Promise.all([
    bracketApi.list(),
    teamApi.list({ page: 1, pageSize: 100 }).then((page) => page.records),
  ]);
});

function teamName(id?: number) {
  return teams.value.find((team) => team.id === id)?.nameCn || '待定';
}

function statusType(status: string) {
  if (status.includes('结束') || status === 'FINISHED') return 'success';
  if (status.includes('进行') || status === 'ONGOING') return 'warning';
  return 'info';
}

function stageLabel(stage: string) {
  const labels: Record<string, string> = {
    ROUND_OF_32: '1/16 决赛（32 强）',
    ROUND_OF_16: '1/8 决赛（16 强）',
    QUARTER_FINAL: '1/4 决赛（8 强）',
    SEMI_FINAL: '半决赛',
    THIRD_PLACE: '季军赛',
    FINAL: '决赛',
  };
  return labels[stage] || stage;
}
</script>

<template>
  <el-card class="bracket-card">
    <template #header>
      <div class="flex-between">
        <span>淘汰赛对阵图</span>
        <el-tag type="info" effect="plain" size="small">横向滚动查看更多</el-tag>
      </div>
    </template>
    <div class="bracket-scroll">
      <div class="bracket">
        <section v-for="stage in orderedStages" :key="stage" class="stage">
          <h3 class="stage-title">{{ stageLabel(stage) }}</h3>
          <div class="stage-matches">
            <div
              v-for="match in stageMatches(stage)"
              :key="match.id"
              class="match-card"
              :class="{
                'has-bridge': shouldShowBridge(stage),
                'is-last': isLastStage(stage),
              }"
            >
              <div class="match-header">
                <span class="match-no">Match {{ match.matchNo }}</span>
                <el-tag size="small" :type="statusType(match.status)">{{ match.status }}</el-tag>
              </div>
              <div class="match-body">
                <div
                  class="team-row"
                  :class="{ winner: match.winnerTeamId && match.winnerTeamId === match.homeTeamId }"
                >
                  <div class="team-info">
                    <span class="team-badge home"></span>
                    <span class="team-name">{{ match.homeTeamId ? teamName(match.homeTeamId) : '待定' }}</span>
                  </div>
                  <span v-if="match.homeScore !== undefined" class="team-score">{{ match.homeScore }}</span>
                </div>
                <div class="team-divider"></div>
                <div
                  class="team-row"
                  :class="{ winner: match.winnerTeamId && match.winnerTeamId === match.awayTeamId }"
                >
                  <div class="team-info">
                    <span class="team-badge away"></span>
                    <span class="team-name">{{ match.awayTeamId ? teamName(match.awayTeamId) : '待定' }}</span>
                  </div>
                  <span v-if="match.awayScore !== undefined" class="team-score">{{ match.awayScore }}</span>
                </div>
              </div>
              <div v-if="match.matchTime" class="match-footer">
                <el-icon size="12"><Calendar /></el-icon>
                <span>{{ match.matchTime }}</span>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.bracket-card :deep(.el-card__header) {
  padding: 16px 20px;
}

.bracket-scroll {
  overflow-x: auto;
  padding-bottom: 8px;
}

.bracket {
  display: flex;
  gap: 28px;
  min-width: max-content;
  padding: 4px 2px;
}

.stage {
  width: 220px;
  flex-shrink: 0;
}

.stage-title {
  margin: 0 0 14px;
  padding-bottom: 10px;
  color: var(--c-ink);
  font-size: 15px;
  font-weight: 700;
  text-align: center;
  border-bottom: 2px solid var(--c-primary);
}

.stage-matches {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.match-card {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 12px;
  background: #fff;
  border: 1px solid var(--c-line);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);
}

.match-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
  border-color: var(--c-primary-light);
}

/* 右侧水平出线 */
.match-card:not(.is-last)::after {
  content: '';
  position: absolute;
  right: -15px;
  top: 50%;
  width: 15px;
  height: 2px;
  background: var(--c-line);
  transform: translateY(-50%);
}

/* 箭头 */
.match-card:not(.is-last)::before {
  content: '';
  position: absolute;
  right: -19px;
  top: 50%;
  transform: translateY(-50%);
  border: 4px solid transparent;
  border-left: 6px solid var(--c-line);
}

/* 桥接垂直线 - 奇数（上半段，向下） */
.match-card.has-bridge:nth-child(odd):not(:last-child)::after {
  width: 15px;
  height: calc(50% + 10px);
  background: transparent;
  border-right: 2px solid var(--c-line);
  border-top: 2px solid var(--c-line);
  border-radius: 0 4px 0 0;
  transform: none;
  top: 50%;
}

/* 桥接垂直线 - 偶数（下半段，向上） */
.match-card.has-bridge:nth-child(even)::after {
  width: 15px;
  height: calc(50% + 10px);
  background: transparent;
  border-right: 2px solid var(--c-line);
  border-bottom: 2px solid var(--c-line);
  border-radius: 0 0 4px 0;
  transform: none;
  top: auto;
  bottom: 50%;
}

.match-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.match-no {
  color: var(--c-muted);
  font-size: 11px;
  font-weight: 600;
}

.match-body {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.team-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  border-radius: var(--radius-sm);
  transition: background var(--transition-fast);
}

.team-row.winner {
  background: var(--c-primary-bg);
  border-left: 3px solid var(--c-primary);
  font-weight: 700;
}

.team-info {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.team-badge {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.team-badge.home {
  background: var(--c-primary);
}

.team-badge.away {
  background: var(--c-blue);
}

.team-name {
  color: var(--c-ink);
  font-size: 14px;
}

.team-row.winner .team-name {
  color: var(--c-primary-dark);
}

.team-score {
  color: var(--c-ink);
  font-size: 15px;
  font-weight: 700;
  min-width: 24px;
  text-align: right;
}

.team-divider {
  height: 1px;
  background: var(--c-line-light);
  margin: 2px 0;
}

.match-footer {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 2px;
  padding-top: 8px;
  color: var(--c-muted);
  font-size: 12px;
  border-top: 1px dashed var(--c-line);
}
</style>
