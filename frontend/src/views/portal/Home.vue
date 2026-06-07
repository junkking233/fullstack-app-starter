<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Calendar, Flag, Star, Trophy } from '@element-plus/icons-vue';
import { matchApi, publicStatsApi, teamApi } from '@/api/worldcupApi';
import type { Match, PublicSummary, Team } from '@/types/worldcup';

const router = useRouter();
const teams = ref<Team[]>([]);
const matches = ref<Match[]>([]);
const summary = ref<PublicSummary>({
  totalTeams: 0,
  totalMatches: 0,
  totalCities: 0,
  finishedMatches: 0,
  upcomingMatches: 0,
  topFavoriteTeams: [],
  topFavoriteMatches: [],
});

onMounted(async () => {
  const [teamPage, matchPage, summaryData] = await Promise.all([
    teamApi.list({ page: 1, pageSize: 8 }),
    matchApi.list({ page: 1, pageSize: 8 }),
    publicStatsApi.summary(),
  ]);
  teams.value = teamPage.records;
  matches.value = matchPage.records;
  summary.value = summaryData;
});
</script>

<template>
  <section class="home-page">
    <el-carousel height="300px" indicator-position="outside" class="hero-carousel">
      <el-carousel-item>
        <div class="hero hero-main">
          <span class="hero-kicker">FIFA WORLD CUP 2026</span>
          <h1>2026 世界杯赛事信息系统</h1>
          <p>48 支球队、12 个小组、104 场比赛，赛程、积分榜和淘汰赛路径集中查看。</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="router.push('/portal/matches')">查看赛程</el-button>
            <el-button size="large" @click="router.push('/portal/bracket')">淘汰赛路径</el-button>
          </div>
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div class="hero hero-alt">
          <span class="hero-kicker">HOST CITIES</span>
          <h1>加拿大 · 墨西哥 · 美国</h1>
          <p>浏览 16 个主办城市与场馆，了解每座城市承办的比赛。</p>
          <el-button type="primary" size="large" @click="router.push('/portal/cities')">城市场馆</el-button>
        </div>
      </el-carousel-item>
    </el-carousel>

    <div class="metric-grid">
      <el-card><el-icon><Flag /></el-icon><strong>{{ summary.totalTeams }}</strong><span>参赛球队</span></el-card>
      <el-card><el-icon><Calendar /></el-icon><strong>{{ summary.totalMatches }}</strong><span>全部比赛</span></el-card>
      <el-card><el-icon><Trophy /></el-icon><strong>{{ summary.totalCities }}</strong><span>主办城市</span></el-card>
      <el-card><el-icon><Star /></el-icon><strong>{{ summary.upcomingMatches }}</strong><span>未开始比赛</span></el-card>
    </div>

    <div class="content-grid">
      <el-card header="近期赛程">
        <el-table :data="matches" size="large">
          <el-table-column prop="matchNo" label="编号" width="80" />
          <el-table-column prop="stage" label="阶段" width="120" />
          <el-table-column prop="groupName" label="小组" width="80" />
          <el-table-column prop="matchTime" label="时间" min-width="180" />
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button type="primary" link @click="router.push(`/portal/matches/${row.id}`)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card header="热门球队">
        <div v-if="summary.topFavoriteTeams.length" class="ranking-list">
          <div v-for="item in summary.topFavoriteTeams" :key="item.name" class="rank-row">
            <strong>{{ item.name }}</strong>
            <span>{{ item.value }} 次收藏</span>
          </div>
        </div>
        <div v-else class="team-grid">
          <div v-for="team in teams" :key="team.id" class="team-card" @click="router.push(`/portal/teams/${team.id}`)">
            <strong>{{ team.nameCn }}</strong>
            <span>{{ team.nameEn }}</span>
            <el-tag size="small">Group {{ team.groupName }}</el-tag>
          </div>
        </div>
      </el-card>
      <el-card header="热门比赛">
        <div v-if="summary.topFavoriteMatches.length" class="ranking-list">
          <div v-for="item in summary.topFavoriteMatches" :key="item.name" class="rank-row">
            <strong>{{ item.name }}</strong>
            <span>{{ item.value }} 次收藏</span>
          </div>
        </div>
        <el-empty v-else description="暂无收藏排行" />
      </el-card>
    </div>
  </section>
</template>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.hero-carousel {
  overflow: hidden;
  border: 1px solid rgb(255 255 255 / 18%);
  border-radius: 18px;
  box-shadow: var(--shadow-xl);
}
.hero {
  position: relative;
  display: flex;
  height: 100%;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  padding: 42px 52px;
  color: #fff;
  overflow: hidden;
  background:
    linear-gradient(90deg, rgb(5 20 26 / 88%), rgb(8 47 73 / 66%) 52%, rgb(15 118 110 / 24%)),
    url("https://images.unsplash.com/photo-1518091043644-c1d4457512c6?auto=format&fit=crop&w=1600&q=80") center / cover;
}
.hero-alt {
  background:
    linear-gradient(90deg, rgb(5 20 26 / 88%), rgb(15 118 110 / 65%) 55%, rgb(234 88 12 / 38%)),
    url("https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1600&q=80") center / cover;
}
.hero::after {
  position: absolute;
  right: 38px;
  bottom: 28px;
  color: rgb(255 255 255 / 12%);
  content: "26";
  font-size: 142px;
  font-weight: 900;
  line-height: 1;
}
.hero-kicker {
  position: relative;
  z-index: 1;
  margin-bottom: 10px;
  color: #fde68a;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.12em;
}
.hero h1 {
  position: relative;
  z-index: 1;
  margin: 0 0 12px;
  max-width: 720px;
  font-size: 42px;
  line-height: 1.14;
}
.hero p {
  position: relative;
  z-index: 1;
  max-width: 620px;
  margin-bottom: 24px;
  color: #dbeafe;
  font-size: 16px;
}
.hero-actions {
  position: relative;
  z-index: 1;
  display: flex;
  gap: 12px;
}
.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}
.metric-grid :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 12px;
  min-height: 88px;
}
.metric-grid strong {
  color: var(--c-ink);
  font-size: 30px;
  line-height: 1;
}
.metric-grid span {
  color: var(--c-muted);
}
.content-grid {
  display: grid;
  grid-template-columns: minmax(420px, 1.35fr) minmax(260px, 0.9fr) minmax(260px, 0.9fr);
  gap: 16px;
}
.team-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}
.team-card {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 12px;
  background: linear-gradient(180deg, #fff, var(--c-bg));
  border: 1px solid var(--c-line);
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.16s ease, box-shadow 0.16s ease;
}
.team-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}
.team-card span {
  color: var(--c-muted);
}
.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.rank-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: linear-gradient(180deg, #fff, #f8fafc);
  border: 1px solid var(--c-line);
  border-radius: 8px;
  box-shadow: var(--shadow-xs);
}
.rank-row span {
  color: var(--c-muted);
  font-size: 13px;
}
</style>
