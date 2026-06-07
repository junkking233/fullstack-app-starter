<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { teamApi } from '@/api/worldcupApi';
import type { Team } from '@/types/worldcup';

const rows = ref<Team[]>([]);
const total = ref(0);
const visible = ref(false);
const query = reactive({ keyword: '', groupName: '', page: 1, pageSize: 10 });
const form = reactive<Partial<Team>>({});

onMounted(load);
async function load() {
  const page = await teamApi.list(query);
  rows.value = page.records;
  total.value = page.total;
}
function open(row?: Team) {
  Object.assign(form, row || { id: undefined, nameCn: '', nameEn: '', country: '', confederation: '', groupName: '', description: '' });
  visible.value = true;
}
async function save() {
  if (form.id) await teamApi.update(form.id, form);
  else await teamApi.create(form);
  ElMessage.success('保存成功');
  visible.value = false;
  load();
}
async function remove(row: Team) {
  await ElMessageBox.confirm(`确认删除 ${row.nameCn}？`, '提示');
  await teamApi.delete(row.id);
  ElMessage.success('删除成功');
  load();
}
</script>

<template>
  <el-card>
    <template #header>球队管理</template>
    <div class="toolbar">
      <el-form :model="query" inline>
        <el-form-item label="关键词"><el-input v-model="query.keyword" clearable /></el-form-item>
        <el-form-item label="小组"><el-input v-model="query.groupName" clearable style="width: 100px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="query.page = 1; load()">查询</el-button></el-form-item>
      </el-form>
      <el-button type="primary" @click="open()">新增球队</el-button>
    </div>
    <el-table :data="rows" border>
      <el-table-column prop="nameCn" label="中文名" />
      <el-table-column prop="nameEn" label="英文名" />
      <el-table-column prop="country" label="国家或地区" />
      <el-table-column prop="confederation" label="洲际" width="100" />
      <el-table-column prop="groupName" label="小组" width="80" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }"><el-button link type="primary" @click="open(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @change="load" />
    <el-dialog v-model="visible" title="球队信息" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="中文名"><el-input v-model="form.nameCn" /></el-form-item>
        <el-form-item label="英文名"><el-input v-model="form.nameEn" /></el-form-item>
        <el-form-item label="国家地区"><el-input v-model="form.country" /></el-form-item>
        <el-form-item label="洲际足联"><el-input v-model="form.confederation" /></el-form-item>
        <el-form-item label="小组"><el-input v-model="form.groupName" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.description" type="textarea" /></el-form-item>
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
</style>
