<script setup lang="ts">
import { ref } from 'vue';
import { Check, Filter, Refresh, Search, View } from '@element-plus/icons-vue';

const keyword = ref('');
const status = ref('');
const priority = ref('');

const statusOptions = [
  { label: '全部', value: '' },
  { label: '待处理', value: 'pending' },
  { label: '处理中', value: 'processing' },
  { label: '已完成', value: 'done' },
];

const priorityOptions = [
  { label: '全部', value: '' },
  { label: '紧急', value: 'high' },
  { label: '普通', value: 'normal' },
  { label: '低', value: 'low' },
];

const tasks = [
  { id: 'T20250601001', title: '审核新入驻机构资质材料', type: '资质审核', status: 'pending', priority: 'high', createTime: '2025-06-01 09:30', deadline: '2025-06-01 18:00' },
  { id: 'T20250601002', title: '处理用户投诉工单 #2847', type: '用户投诉', status: 'processing', priority: 'high', createTime: '2025-06-01 10:15', deadline: '2025-06-01 17:00' },
  { id: 'T20250531003', title: '更新服务价格表及说明文档', type: '内容更新', status: 'pending', priority: 'normal', createTime: '2025-05-31 14:00', deadline: '2025-06-02 12:00' },
  { id: 'T20250530004', title: '提交月度运营数据报告', type: '数据报表', status: 'processing', priority: 'normal', createTime: '2025-05-30 16:20', deadline: '2025-06-05 18:00' },
  { id: 'T20250529005', title: '补充企业用户缺失资料', type: '资料补全', status: 'done', priority: 'low', createTime: '2025-05-29 11:00', deadline: '2025-05-31 18:00' },
];

const statusMap: Record<string, { label: string; type: string }> = {
  pending: { label: '待处理', type: 'warning' },
  processing: { label: '处理中', type: 'primary' },
  done: { label: '已完成', type: 'success' },
};

const priorityMap: Record<string, { label: string; type: string }> = {
  high: { label: '紧急', type: 'danger' },
  normal: { label: '普通', type: 'info' },
  low: { label: '低', type: 'success' },
};
</script>

<template>
  <div class="tasks-page">
    <div class="page-header">
      <div class="header-text">
        <h1>工单管理</h1>
        <p>查看和处理分配给你的全部工单</p>
      </div>
    </div>

    <section class="filter-bar">
      <div class="filter-group">
        <el-input v-model="keyword" clearable placeholder="工单标题或编号" :prefix-icon="Search" style="width: 240px" />
        <el-select v-model="status" clearable placeholder="状态" style="width: 130px">
          <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
        <el-select v-model="priority" clearable placeholder="优先级" style="width: 130px">
          <el-option v-for="p in priorityOptions" :key="p.value" :label="p.label" :value="p.value" />
        </el-select>
      </div>
      <div class="filter-actions">
        <el-button type="primary" :icon="Filter">筛选</el-button>
        <el-button :icon="Refresh">重置</el-button>
      </div>
    </section>

    <section class="table-wrap">
      <el-table :data="tasks" stripe>
        <el-table-column prop="id" label="工单编号" min-width="160">
          <template #default="{ row }">
            <span class="code-text">{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="type" label="类型" width="110">
          <template #default="{ row }">
            <el-tag size="small" effect="light" round>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="(statusMap[row.status]?.type as any)" size="small" effect="light" round>
              {{ statusMap[row.status]?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优先级" width="90">
          <template #default="{ row }">
            <el-tag :type="(priorityMap[row.priority]?.type as any)" size="small" effect="light" round>
              {{ priorityMap[row.priority]?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column prop="deadline" label="截止时间" width="150" />
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" :icon="View" link>详情</el-button>
            <el-button v-if="row.status !== 'done'" type="success" :icon="Check" link>完成</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination layout="total, sizes, prev, pager, next" :total="tasks.length" :page-sizes="[10, 20, 50]" />
      </div>
    </section>
  </div>
</template>

<style scoped>
.tasks-page {
  max-width: 1180px;
  margin: 0 auto;
  padding: 32px 28px;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0 0 6px;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.page-header p {
  margin: 0;
  color: var(--c-muted);
  font-size: 15px;
}

.filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
  padding: 16px 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.table-wrap {
  padding: 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.code-text {
  font-family: 'SF Mono', Monaco, monospace;
  font-size: 13px;
  color: var(--c-muted);
  background: var(--c-bg);
  padding: 2px 8px;
  border-radius: 4px;
}

.pagination-bar {
  display: flex;
  justify-content: flex-end;
  padding-top: 14px;
}

@media (max-width: 860px) {
  .tasks-page {
    padding: 20px 16px;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-group {
    width: 100%;
  }

  .filter-group .el-input,
  .filter-group .el-select {
    width: 100% !important;
  }
}
</style>
