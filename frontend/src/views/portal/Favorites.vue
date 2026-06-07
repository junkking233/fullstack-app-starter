<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { favoriteApi, matchApi, teamApi } from '@/api/worldcupApi';
import type { Favorite, Match, Team } from '@/types/worldcup';

const favorites = ref<Favorite[]>([]);
const teams = ref<Team[]>([]);
const matches = ref<Match[]>([]);

onMounted(load);
async function load() {
  favorites.value = await favoriteApi.my();
  teams.value = (await teamApi.list({ page: 1, pageSize: 100 })).records;
  matches.value = (await matchApi.list({ page: 1, pageSize: 120 })).records;
}
function label(item: Favorite) {
  if (item.objectType === 'TEAM') {
    return teams.value.find((team) => team.id === item.objectId)?.nameCn || `球队 ${item.objectId}`;
  }
  const match = matches.value.find((row) => row.id === item.objectId);
  return match ? `Match ${match.matchNo}` : `比赛 ${item.objectId}`;
}
async function remove(item: Favorite) {
  await favoriteApi.delete(item.objectType, item.objectId);
  ElMessage.success('已取消收藏');
  load();
}
</script>

<template>
  <el-card>
    <template #header>我的收藏</template>
    <el-table :data="favorites" border>
      <el-table-column prop="objectType" label="类型" width="120" />
      <el-table-column label="收藏对象"><template #default="{ row }">{{ label(row) }}</template></el-table-column>
      <el-table-column prop="createTime" label="收藏时间" width="180" />
      <el-table-column label="操作" width="120"><template #default="{ row }"><el-button type="danger" link @click="remove(row)">取消收藏</el-button></template></el-table-column>
    </el-table>
  </el-card>
</template>
