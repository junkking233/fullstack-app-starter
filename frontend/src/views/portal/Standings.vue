<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { standingApi, teamApi } from '@/api/worldcupApi';
import type { Standing, Team } from '@/types/worldcup';

const standings = ref<Standing[]>([]);
const teams = ref<Team[]>([]);
const groups = computed(() => [...new Set(standings.value.map((item) => item.groupName))]);

onMounted(async () => {
  [standings.value, teams.value] = await Promise.all([
    standingApi.list(),
    teamApi.list({ page: 1, pageSize: 100 }).then((page) => page.records),
  ]);
});
function teamName(id: number) {
  return teams.value.find((team) => team.id === id)?.nameCn || `球队${id}`;
}
function rows(group: string) {
  return standings.value.filter((item) => item.groupName === group);
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
</script>

<template>
  <el-card>
    <template #header>小组积分榜</template>
    <el-tabs>
      <el-tab-pane v-for="group in groups" :key="group" :label="`Group ${group}`">
        <el-table :data="rows(group)" border>
          <el-table-column prop="rankNo" label="排名" width="80" />
          <el-table-column label="球队" min-width="160"><template #default="{ row }">{{ teamName(row.teamId) }}</template></el-table-column>
          <el-table-column prop="played" label="场" width="70" />
          <el-table-column prop="wins" label="胜" width="70" />
          <el-table-column prop="draws" label="平" width="70" />
          <el-table-column prop="losses" label="负" width="70" />
          <el-table-column prop="goalsFor" label="进球" width="80" />
          <el-table-column prop="goalsAgainst" label="失球" width="80" />
          <el-table-column prop="goalDiff" label="净胜球" width="90" />
          <el-table-column prop="points" label="积分" width="80" />
          <el-table-column label="晋级状态" width="150"><template #default="{ row }">{{ statusLabel(row.qualifyStatus) }}</template></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
