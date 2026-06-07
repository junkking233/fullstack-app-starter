<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { commentApi } from '@/api/worldcupApi';
import type { MatchComment } from '@/types/worldcup';

const rows = ref<MatchComment[]>([]);
const total = ref(0);
const query = reactive({ auditStatus: '', page: 1, pageSize: 10 });
onMounted(load);

async function load() {
  const page = await commentApi.list(query);
  rows.value = page.records;
  total.value = page.total;
}
function statusLabel(status: string) {
  const map: Record<string, string> = {
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已驳回',
  };
  return map[status] || status;
}

function statusTagType(status: string) {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info'> = {
    PENDING: 'info',
    APPROVED: 'success',
    REJECTED: 'danger',
  };
  return map[status] || 'info';
}

async function review(id: number, status: 'APPROVED' | 'REJECTED') {
  await commentApi.review(id, status);
  ElMessage.success('审核完成');
  load();
}
async function remove(id: number) {
  await commentApi.delete(id);
  ElMessage.success('删除成功');
  load();
}
</script>

<template>
  <el-card>
    <template #header>评论审核</template>
    <el-form :model="query" inline>
      <el-form-item label="状态">
        <el-select v-model="query.auditStatus" clearable style="width: 150px">
          <el-option label="待审核" value="PENDING" />
          <el-option label="已通过" value="APPROVED" />
          <el-option label="已驳回" value="REJECTED" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="query.page = 1; load()">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" border>
      <el-table-column prop="matchId" label="比赛ID" width="90" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="content" label="评论内容" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.auditStatus)" size="small">{{ statusLabel(row.auditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="210">
        <template #default="{ row }">
          <el-button type="success" link @click="review(row.id, 'APPROVED')">通过</el-button>
          <el-button type="warning" link @click="review(row.id, 'REJECTED')">驳回</el-button>
          <el-button type="danger" link @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @change="load" />
  </el-card>
</template>
