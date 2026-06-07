<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Star } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { favoriteApi, teamApi } from '@/api/worldcupApi';
import type { Team } from '@/types/worldcup';
import { getCurrentUser } from '@/utils/auth';

const router = useRouter();
const teams = ref<Team[]>([]);
const total = ref(0);
const loading = ref(false);
const query = reactive({ keyword: '', groupName: '', confederation: '', page: 1, pageSize: 12 });

onMounted(fetchTeams);

async function fetchTeams() {
  loading.value = true;
  try {
    const page = await teamApi.list(query);
    teams.value = page.records;
    total.value = page.total;
  } finally {
    loading.value = false;
  }
}

async function favorite(team: Team) {
  if (!getCurrentUser()) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }
  await favoriteApi.create('TEAM', team.id);
  ElMessage.success('收藏成功');
}
</script>

<template>
  <section class="page">
    <el-card>
      <template #header>球队信息</template>
      <el-form :model="query" inline>
        <el-form-item label="关键词"><el-input v-model="query.keyword" clearable placeholder="中文/英文/国家" /></el-form-item>
        <el-form-item label="小组"><el-input v-model="query.groupName" clearable placeholder="A" style="width: 100px" /></el-form-item>
        <el-form-item label="洲际"><el-input v-model="query.confederation" clearable placeholder="UEFA" style="width: 140px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="query.page = 1; fetchTeams()">查询</el-button></el-form-item>
      </el-form>
      <div v-loading="loading" class="team-list">
        <el-card v-for="team in teams" :key="team.id" class="team-card">
          <h3>{{ team.nameCn }}</h3>
          <p>{{ team.nameEn }}</p>
          <div class="tags">
            <el-tag>Group {{ team.groupName }}</el-tag>
            <el-tag type="info">{{ team.confederation }}</el-tag>
          </div>
          <div class="actions">
            <el-button type="primary" link @click="router.push(`/portal/teams/${team.id}`)">详情</el-button>
            <el-button :icon="Star" link @click="favorite(team)">收藏</el-button>
          </div>
        </el-card>
      </div>
      <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @change="fetchTeams" />
    </el-card>
  </section>
</template>

<style scoped>
.team-list {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin: 16px 0;
}
.team-card h3 {
  margin: 0 0 4px;
}
.team-card p {
  color: #64748b;
}
.tags, .actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
</style>
