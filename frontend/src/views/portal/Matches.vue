<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Star } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { cityApi, favoriteApi, matchApi, stadiumApi, teamApi } from '@/api/worldcupApi';
import type { City, Match, Stadium, Team } from '@/types/worldcup';
import { getCurrentUser } from '@/utils/auth';

const router = useRouter();
const matches = ref<Match[]>([]);
const teams = ref<Team[]>([]);
const cities = ref<City[]>([]);
const stadiums = ref<Stadium[]>([]);
const total = ref(0);
const loading = ref(false);
const query = reactive({
  keyword: '',
  teamId: undefined as number | undefined,
  cityId: undefined as number | undefined,
  groupName: '',
  stage: '',
  status: '',
  date: '',
  page: 1,
  pageSize: 10,
});

onMounted(async () => {
  const [teamPage, cityList, stadiumList] = await Promise.all([teamApi.list({ page: 1, pageSize: 100 }), cityApi.list(), stadiumApi.list()]);
  teams.value = teamPage.records;
  cities.value = cityList;
  stadiums.value = stadiumList;
  fetchMatches();
});

function teamName(id?: number) {
  return teams.value.find((team) => team.id === id)?.nameCn || '待定';
}
function cityName(id?: number) {
  return cities.value.find((city) => city.id === id)?.nameCn || '-';
}
function stadiumName(id?: number) {
  return stadiums.value.find((stadium) => stadium.id === id)?.nameCn || '-';
}
async function fetchMatches() {
  loading.value = true;
  try {
    const page = await matchApi.list(query);
    matches.value = page.records;
    total.value = page.total;
  } finally {
    loading.value = false;
  }
}
async function favorite(row: Match) {
  if (!getCurrentUser()) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }
  await favoriteApi.create('MATCH', row.id);
  ElMessage.success('收藏成功');
}
</script>

<template>
  <el-card>
    <template #header>赛程信息</template>
    <el-form :model="query" inline>
      <el-form-item label="关键词"><el-input v-model="query.keyword" clearable placeholder="编号/球队/阶段" style="width: 170px" /></el-form-item>
      <el-form-item label="球队">
        <el-select v-model="query.teamId" clearable filterable placeholder="选择球队" style="width: 180px">
          <el-option v-for="team in teams" :key="team.id" :label="team.nameCn" :value="team.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="城市">
        <el-select v-model="query.cityId" clearable filterable placeholder="选择城市" style="width: 160px">
          <el-option v-for="city in cities" :key="city.id" :label="city.nameCn" :value="city.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="小组"><el-input v-model="query.groupName" clearable style="width: 90px" /></el-form-item>
      <el-form-item label="日期"><el-date-picker v-model="query.date" value-format="YYYY-MM-DD" type="date" clearable style="width: 150px" /></el-form-item>
      <el-form-item label="阶段">
        <el-select v-model="query.stage" clearable style="width: 150px">
          <el-option label="小组赛" value="GROUP" />
          <el-option label="32 强" value="ROUND_OF_32" />
          <el-option label="16 强" value="ROUND_OF_16" />
          <el-option label="四分之一决赛" value="QUARTER_FINAL" />
          <el-option label="半决赛" value="SEMI_FINAL" />
          <el-option label="决赛" value="FINAL" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 140px">
          <el-option label="未开始" value="NOT_STARTED" />
          <el-option label="进行中" value="LIVE" />
          <el-option label="已结束" value="FINISHED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="query.page = 1; fetchMatches()">查询</el-button></el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="matches" border stripe>
      <el-table-column prop="matchNo" label="编号" width="80" />
      <el-table-column label="对阵" min-width="220">
        <template #default="{ row }">{{ teamName(row.homeTeamId) }} vs {{ teamName(row.awayTeamId) }}</template>
      </el-table-column>
      <el-table-column prop="stage" label="阶段" width="130" />
      <el-table-column prop="groupName" label="小组" width="80" />
      <el-table-column prop="matchTime" label="时间" min-width="170" />
      <el-table-column label="场馆" min-width="180">
        <template #default="{ row }">{{ cityName(row.cityId) }} · {{ stadiumName(row.stadiumId) }}</template>
      </el-table-column>
      <el-table-column label="比分" width="100">
        <template #default="{ row }">{{ row.homeScore ?? '-' }} : {{ row.awayScore ?? '-' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="130" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="router.push(`/portal/matches/${row.id}`)">详情</el-button>
          <el-button :icon="Star" link @click="favorite(row)">收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, sizes, prev, pager, next" :total="total" @change="fetchMatches" />
    </div>
  </el-card>
</template>

<style scoped>
.pager {
  display: flex;
  justify-content: flex-end;
  padding-top: 16px;
}
</style>
