<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { dataMaintenanceApi } from '@/api/worldcupApi';
import type { DataMaintenance } from '@/types/worldcup';

const rows = ref<DataMaintenance[]>([]);
const total = ref(0);
const query = reactive({ dataType: '', page: 1, pageSize: 10 });
const form = reactive<Partial<DataMaintenance>>({
  dataType: 'MATCH',
  actionType: 'MANUAL_UPDATE',
  source: 'FIFA World Cup 2026 official schedule page',
  remark: '',
});

onMounted(load);

async function load() {
  const page = await dataMaintenanceApi.list(query);
  rows.value = page.records;
  total.value = page.total;
}

async function save() {
  await dataMaintenanceApi.create(form);
  ElMessage.success('维护记录已保存');
  form.remark = '';
  load();
}
</script>

<template>
  <el-card>
    <template #header>数据维护记录</template>
    <div class="maintenance-grid">
      <el-card shadow="never">
        <template #header>新增维护记录</template>
        <el-form :model="form" label-width="88px">
          <el-form-item label="数据类型">
            <el-select v-model="form.dataType" style="width: 220px">
              <el-option label="球队数据" value="TEAM" />
              <el-option label="赛程数据" value="MATCH" />
              <el-option label="积分榜" value="STANDING" />
              <el-option label="城市场馆" value="HOST_CITY" />
            </el-select>
          </el-form-item>
          <el-form-item label="动作">
            <el-select v-model="form.actionType" style="width: 220px">
              <el-option label="手动更新" value="MANUAL_UPDATE" />
              <el-option label="AI辅助更新" value="AI_ASSISTED_UPDATE" />
              <el-option label="官方数据核对" value="SOURCE_VERIFY" />
            </el-select>
          </el-form-item>
          <el-form-item label="来源"><el-input v-model="form.source" /></el-form-item>
          <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="4" /></el-form-item>
          <el-form-item><el-button type="primary" @click="save">保存记录</el-button></el-form-item>
        </el-form>
      </el-card>
      <el-card shadow="never">
        <template #header>历史记录</template>
        <el-form :model="query" inline>
          <el-form-item label="类型"><el-input v-model="query.dataType" clearable style="width: 140px" /></el-form-item>
          <el-form-item><el-button type="primary" @click="query.page = 1; load()">查询</el-button></el-form-item>
        </el-form>
        <el-table :data="rows" border>
          <el-table-column prop="dataType" label="类型" width="110" />
          <el-table-column prop="actionType" label="动作" width="150" />
          <el-table-column prop="source" label="来源" min-width="220" />
          <el-table-column prop="remark" label="备注" min-width="220" />
          <el-table-column prop="createTime" label="时间" width="180" />
        </el-table>
        <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @change="load" />
      </el-card>
    </div>
  </el-card>
</template>

<style scoped>
.maintenance-grid {
  display: grid;
  grid-template-columns: 360px minmax(0, 1fr);
  gap: 16px;
}
</style>
