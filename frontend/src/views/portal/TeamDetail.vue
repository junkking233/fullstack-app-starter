<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { favoriteApi, matchApi, teamApi } from '@/api/worldcupApi';
import type { Match, Team } from '@/types/worldcup';
import { getCurrentUser } from '@/utils/auth';

const route = useRoute();
const router = useRouter();
const team = ref<Team>();
const matches = ref<Match[]>([]);
const favorited = ref(false);

onMounted(async () => {
  const id = Number(route.params.id);
  team.value = await teamApi.get(id);
  matches.value = (await matchApi.list({ teamId: id, page: 1, pageSize: 20 })).records;
  if (getCurrentUser()) {
    favorited.value = (await favoriteApi.status('TEAM', id)).favorited;
  }
});

async function toggleFavorite() {
  if (!team.value) return;
  if (!getCurrentUser()) {
    ElMessage.warning('请先登录');
    router.push({ path: '/login', query: { redirect: route.fullPath } });
    return;
  }
  if (favorited.value) {
    await favoriteApi.delete('TEAM', team.value.id);
    favorited.value = false;
    ElMessage.success('已取消收藏');
  } else {
    await favoriteApi.create('TEAM', team.value.id);
    favorited.value = true;
    ElMessage.success('收藏成功');
  }
}
</script>

<template>
  <el-card v-if="team">
    <template #header>
      <div class="header">
        <span>{{ team.nameCn }} / {{ team.nameEn }}</span>
        <el-button :type="favorited ? 'default' : 'primary'" @click="toggleFavorite">
          {{ favorited ? '取消收藏' : '收藏球队' }}
        </el-button>
      </div>
    </template>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="国家或地区">{{ team.country }}</el-descriptions-item>
      <el-descriptions-item label="洲际足联">{{ team.confederation }}</el-descriptions-item>
      <el-descriptions-item label="小组">Group {{ team.groupName }}</el-descriptions-item>
      <el-descriptions-item label="数据来源">{{ team.source }}</el-descriptions-item>
      <el-descriptions-item label="简介" :span="2">{{ team.description }}</el-descriptions-item>
    </el-descriptions>
    <h3>相关比赛</h3>
    <el-table :data="matches">
      <el-table-column prop="matchNo" label="编号" width="90" />
      <el-table-column prop="stage" label="阶段" />
      <el-table-column prop="groupName" label="小组" />
      <el-table-column prop="matchTime" label="时间" />
      <el-table-column prop="status" label="状态" />
    </el-table>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
h3 {
  margin: 24px 0 12px;
}
</style>
