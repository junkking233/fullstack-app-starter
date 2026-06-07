<script setup lang="ts">
import { ref } from 'vue';
import { Refresh, Search, View } from '@element-plus/icons-vue';

const keyword = ref('');
const status = ref('');

const statusOptions = [
  { label: '全部', value: '' },
  { label: '进行中', value: 'processing' },
  { label: '已完成', value: 'completed' },
  { label: '已驳回', value: 'rejected' },
];

const records = [
  { id: 'A20250601001', title: '企业注册申请', type: '企业服务', status: 'processing', date: '2025-06-01' },
  { id: 'A20250528002', title: '个人证件换发', type: '政务服务', status: 'completed', date: '2025-05-28' },
  { id: 'A20250520003', title: '贷款额度申请', type: '金融服务', status: 'rejected', date: '2025-05-20' },
  { id: 'A20250515004', title: '物业缴费', type: '生活服务', status: 'completed', date: '2025-05-15' },
  { id: 'A20250510005', title: '数据报表导出', type: '企业服务', status: 'processing', date: '2025-05-10' },
];

const statusMap: Record<string, { label: string; type: string }> = {
  processing: { label: '进行中', type: 'warning' },
  completed: { label: '已完成', type: 'success' },
  rejected: { label: '已驳回', type: 'danger' },
};
</script>

<template>
  <div class="my-business-page">
    <div class="page-header">
      <div class="header-text">
        <h1>我的业务</h1>
        <p>查看和管理您提交的全部业务申请</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon all">
          <el-icon><Search /></el-icon>
        </div>
        <div class="stat-info">
          <strong>12</strong>
          <span>全部业务</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon processing">
          <el-icon><Refresh /></el-icon>
        </div>
        <div class="stat-info">
          <strong>3</strong>
          <span>进行中</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon completed">
          <el-icon><View /></el-icon>
        </div>
        <div class="stat-info">
          <strong>8</strong>
          <span>已完成</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon rejected">
          <el-icon><Search /></el-icon>
        </div>
        <div class="stat-info">
          <strong>1</strong>
          <span>已驳回</span>
        </div>
      </div>
    </div>

    <!-- 筛选 -->
    <section class="filter-bar">
      <el-input v-model="keyword" clearable placeholder="搜索业务名称或编号" :prefix-icon="Search" style="width: 260px" />
      <el-select v-model="status" clearable placeholder="状态" style="width: 140px">
        <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
      </el-select>
      <el-button type="primary" :icon="Search">查询</el-button>
      <el-button :icon="Refresh">重置</el-button>
    </section>

    <!-- 表格 -->
    <section class="table-wrap">
      <el-table :data="records" stripe>
        <el-table-column prop="id" label="业务编号" min-width="160">
          <template #default="{ row }">
            <span class="code-text">{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="业务名称" min-width="180" />
        <el-table-column prop="type" label="服务类型" width="120">
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
        <el-table-column prop="date" label="提交时间" width="130" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default>
            <el-button type="primary" :icon="View" link>详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination layout="total, prev, pager, next" :total="records.length" :page-size="10" />
      </div>
    </section>
  </div>
</template>

<style scoped>
.my-business-page {
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

.stats-row {
  display: grid;
  gap: 14px;
  margin-bottom: 20px;
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xs);
  transition: all var(--transition-base);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  display: grid;
  width: 44px;
  height: 44px;
  place-items: center;
  font-size: 20px;
  border-radius: var(--radius-md);
  flex-shrink: 0;
}

.stat-icon.all { background: var(--c-primary-bg); color: var(--c-primary); }
.stat-icon.processing { background: var(--c-amber-bg); color: var(--c-amber); }
.stat-icon.completed { background: var(--c-green-bg); color: var(--c-green); }
.stat-icon.rejected { background: var(--c-red-bg); color: var(--c-red); }

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.stat-info strong {
  font-size: 24px;
  font-weight: 800;
  color: var(--c-ink);
  line-height: 1;
}

.stat-info span {
  color: var(--c-muted);
  font-size: 13px;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  padding: 16px 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
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
  .my-business-page {
    padding: 20px 16px;
  }

  .stats-row {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
}
</style>
