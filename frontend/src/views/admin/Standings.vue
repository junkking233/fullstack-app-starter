<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { standingApi, teamApi } from '@/api/worldcupApi';
import type { Standing, Team } from '@/types/worldcup';

const rows = ref<Standing[]>([]);
const teams = ref<Team[]>([]);
onMounted(load);

async function load() {
  [rows.value, teams.value] = await Promise.all([
    standingApi.list(),
    teamApi.list({ page: 1, pageSize: 100 }).then((page) => page.records),
  ]);
}
function teamName(id: number) {
  return teams.value.find((team) => team.id === id)?.nameCn || `球队${id}`;
}
function statusLabel(status: string) {
  const labels: Record<string, string> = {
    QUALIFIED: '直接晋级',
    BEST_THIRD_QUALIFIED: '最佳第三名晋级',
    THIRD_PENDING: '第三名待定',
    ELIMINATED: '已淘汰',
    PENDING: '待定',
  };
  return labels[status] || status;
}
async function recalculate() {
  rows.value = await standingApi.recalculate();
  ElMessage.success('积分榜已重新计算');
}
</script>

<template>
  <el-card>
    <template #header>
      <div class="header"><span>积分榜管理</span><el-button type="primary" @click="recalculate">根据小组赛赛果重算</el-button></div>
    </template>
    <el-table :data="rows" border>
      <el-table-column prop="groupName" label="小组" width="80" />
      <el-table-column label="球队"><template #default="{ row }">{{ teamName(row.teamId) }}</template></el-table-column>
      <el-table-column prop="rankNo" label="排名" width="80" />
      <el-table-column prop="played" label="场" width="70" />
      <el-table-column prop="wins" label="胜" width="70" />
      <el-table-column prop="draws" label="平" width="70" />
      <el-table-column prop="losses" label="负" width="70" />
      <el-table-column prop="goalDiff" label="净胜球" width="90" />
      <el-table-column prop="points" label="积分" width="80" />
      <el-table-column label="晋级状态" width="150"><template #default="{ row }">{{ statusLabel(row.qualifyStatus) }}</template></el-table-column>
    </el-table>
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
