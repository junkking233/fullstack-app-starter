<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { cityApi, matchApi, stadiumApi, teamApi } from '@/api/worldcupApi';
import type { City, Match, Stadium, Team } from '@/types/worldcup';

const rows = ref<Match[]>([]);
const total = ref(0);
const teams = ref<Team[]>([]);
const cities = ref<City[]>([]);
const stadiums = ref<Stadium[]>([]);
const visible = ref(false);
const query = reactive({
  keyword: '',
  teamId: undefined as number | undefined,
  cityId: undefined as number | undefined,
  stage: '',
  groupName: '',
  status: '',
  date: '',
  page: 1,
  pageSize: 10,
});
const form = reactive<Partial<Match>>({});

onMounted(async () => {
  [teams.value, cities.value, stadiums.value] = await Promise.all([
    teamApi.list({ page: 1, pageSize: 100 }).then((p) => p.records),
    cityApi.list(),
    stadiumApi.list(),
  ]);
  load();
});
async function load() {
  const page = await matchApi.list(query);
  rows.value = page.records;
  total.value = page.total;
}
function teamName(id?: number) { return teams.value.find((team) => team.id === id)?.nameCn || '待定'; }
function open(row?: Match) {
  Object.assign(form, row || { id: undefined, matchNo: undefined, stage: 'GROUP', groupName: '', status: 'NOT_STARTED' });
  visible.value = true;
}
async function save() {
  if (form.id) await matchApi.update(form.id, form);
  else await matchApi.create(form);
  ElMessage.success('保存成功');
  visible.value = false;
  load();
}
async function remove(row: Match) {
  await ElMessageBox.confirm(`确认删除 Match ${row.matchNo}？`, '提示');
  await matchApi.delete(row.id);
  ElMessage.success('删除成功');
  load();
}
</script>

<template>
  <el-card>
    <template #header>赛程管理</template>
    <div class="toolbar">
      <el-form :model="query" inline>
        <el-form-item label="关键词"><el-input v-model="query.keyword" clearable style="width: 160px" /></el-form-item>
        <el-form-item label="球队">
          <el-select v-model="query.teamId" clearable filterable style="width: 170px">
            <el-option v-for="team in teams" :key="team.id" :label="team.nameCn" :value="team.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="城市">
          <el-select v-model="query.cityId" clearable filterable style="width: 150px">
            <el-option v-for="city in cities" :key="city.id" :label="city.nameCn" :value="city.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="阶段"><el-input v-model="query.stage" clearable style="width: 150px" /></el-form-item>
        <el-form-item label="小组"><el-input v-model="query.groupName" clearable style="width: 100px" /></el-form-item>
        <el-form-item label="日期"><el-date-picker v-model="query.date" value-format="YYYY-MM-DD" type="date" clearable style="width: 150px" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" clearable style="width: 130px">
            <el-option label="未开始" value="NOT_STARTED" />
            <el-option label="进行中" value="LIVE" />
            <el-option label="已结束" value="FINISHED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="query.page = 1; load()">查询</el-button></el-form-item>
      </el-form>
      <el-button type="primary" @click="open()">新增比赛</el-button>
    </div>
    <el-table :data="rows" border>
      <el-table-column prop="matchNo" label="编号" width="80" />
      <el-table-column label="对阵" min-width="200"><template #default="{ row }">{{ teamName(row.homeTeamId) }} vs {{ teamName(row.awayTeamId) }}</template></el-table-column>
      <el-table-column prop="stage" label="阶段" />
      <el-table-column prop="matchTime" label="时间" />
      <el-table-column label="比分" width="100"><template #default="{ row }">{{ row.homeScore ?? '-' }} : {{ row.awayScore ?? '-' }}</template></el-table-column>
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="140"><template #default="{ row }"><el-button link type="primary" @click="open(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @change="load" />
    <el-dialog v-model="visible" title="比赛信息" width="680px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="编号"><el-input-number v-model="form.matchNo" :min="1" /></el-form-item>
        <el-form-item label="阶段"><el-input v-model="form.stage" /></el-form-item>
        <el-form-item label="小组"><el-input v-model="form.groupName" /></el-form-item>
        <el-form-item label="主队"><el-select v-model="form.homeTeamId" filterable clearable style="width: 220px"><el-option v-for="team in teams" :key="team.id" :label="team.nameCn" :value="team.id" /></el-select></el-form-item>
        <el-form-item label="客队"><el-select v-model="form.awayTeamId" filterable clearable style="width: 220px"><el-option v-for="team in teams" :key="team.id" :label="team.nameCn" :value="team.id" /></el-select></el-form-item>
        <el-form-item label="比赛时间"><el-input v-model="form.matchTime" placeholder="2026-06-11T13:00:00" /></el-form-item>
        <el-form-item label="城市"><el-select v-model="form.cityId" filterable clearable style="width: 220px"><el-option v-for="city in cities" :key="city.id" :label="city.nameCn" :value="city.id" /></el-select></el-form-item>
        <el-form-item label="场馆"><el-select v-model="form.stadiumId" filterable clearable style="width: 260px"><el-option v-for="stadium in stadiums" :key="stadium.id" :label="stadium.nameCn" :value="stadium.id" /></el-select></el-form-item>
        <el-form-item label="比分"><el-input-number v-model="form.homeScore" :min="0" /> <span class="score-split">:</span> <el-input-number v-model="form.awayScore" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status" style="width: 180px"><el-option label="未开始" value="NOT_STARTED" /><el-option label="进行中" value="LIVE" /><el-option label="已结束" value="FINISHED" /><el-option label="已取消" value="CANCELLED" /></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </el-card>
</template>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
}
.score-split {
  margin: 0 10px;
}
</style>
