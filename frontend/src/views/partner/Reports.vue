<script setup lang="ts">
import { chartApi } from '@/api/chartApi';
import BarChart from '@/components/charts/BarChart.vue';
import LineChart from '@/components/charts/LineChart.vue';
import PieChart from '@/components/charts/PieChart.vue';
import { Download, TrendCharts } from '@element-plus/icons-vue';
import { onMounted, ref } from 'vue';

const dateRange = ref('');

const reportList = [
  { title: '月度运营数据汇总', period: '2025-05', type: '运营报表', status: '已生成', createTime: '2025-06-01 00:00' },
  { title: '工单处理效率分析', period: '2025-05', type: '效率分析', status: '已生成', createTime: '2025-06-01 00:00' },
  { title: '用户满意度调查汇总', period: '2025 Q1', type: '满意度', status: '已生成', createTime: '2025-04-01 00:00' },
  { title: '服务覆盖率趋势图', period: '2025-01 ~ 2025-05', type: '趋势分析', status: '已生成', createTime: '2025-06-01 00:00' },
];

const taskTrend = ref({ xData: [] as string[], series: [] as { name: string; data: number[]; color?: string }[] });
const serviceDistribution = ref<{ name: string; value: number }[]>([]);
const satisfactionTrend = ref({ xData: [] as string[], series: [] as { name: string; data: number[]; color?: string }[] });

onMounted(async () => {
  try {
    const [tasks, services, satisfaction] = await Promise.all([
      chartApi.taskTrend(),
      chartApi.serviceDistribution(),
      chartApi.satisfactionTrend(),
    ]);
    taskTrend.value = tasks;
    serviceDistribution.value = services;
    satisfactionTrend.value = satisfaction;
  } catch {
    // fallback to defaults
  }
});
</script>

<template>
  <div class="reports-page">
    <div class="page-header">
      <div class="header-text">
        <h1>报表中心</h1>
        <p>查看和导出业务数据报表</p>
      </div>
    </div>

    <section class="toolbar">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="width: 300px"
      />
      <el-button type="primary" :icon="TrendCharts" class="gen-btn">生成报表</el-button>
    </section>

    <!-- 真实图表 -->
    <div class="chart-grid">
      <div class="chart-card">
        <div class="chart-header">
          <div class="chart-title-row">
            <div class="chart-dot teal" />
            <h4>工单处理趋势</h4>
          </div>
          <span>近 7 天工单处理量变化</span>
        </div>
        <div class="chart-body">
          <BarChart :x-data="taskTrend.xData" :series="taskTrend.series" />
        </div>
      </div>

      <div class="chart-card">
        <div class="chart-header">
          <div class="chart-title-row">
            <div class="chart-dot blue" />
            <h4>服务类型分布</h4>
          </div>
          <span>各类服务占比</span>
        </div>
        <div class="chart-body">
          <PieChart :data="serviceDistribution" />
        </div>
      </div>

      <div class="chart-card">
        <div class="chart-header">
          <div class="chart-title-row">
            <div class="chart-dot amber" />
            <h4>用户满意度</h4>
          </div>
          <span>月度满意度评分走势</span>
        </div>
        <div class="chart-body">
          <LineChart :x-data="satisfactionTrend.xData" :series="satisfactionTrend.series" />
        </div>
      </div>
    </div>

    <!-- 报表列表 -->
    <section class="table-wrap">
      <div class="table-header">
        <div class="table-title">
          <div class="title-line" />
          <span>历史报表</span>
        </div>
        <el-button type="primary" :icon="Download" link>批量导出</el-button>
      </div>
      <el-table :data="reportList" stripe>
        <el-table-column prop="title" label="报表名称" min-width="240" />
        <el-table-column prop="period" label="统计周期" width="160" />
        <el-table-column prop="type" label="报表类型" width="120">
          <template #default="{ row }">
            <el-tag size="small" effect="light" round>{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag type="success" size="small" effect="light" round>{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="生成时间" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default>
            <el-button type="primary" :icon="Download" link>下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>
  </div>
</template>

<style scoped>
.reports-page {
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

.toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding: 14px 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.gen-btn {
  font-weight: 600;
  border-radius: var(--radius-md);
}

.chart-grid {
  display: grid;
  gap: 14px;
  margin-bottom: 20px;
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.chart-card {
  padding: 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);
  height: 340px;
}

.chart-card:hover {
  box-shadow: var(--shadow-lg);
}

.chart-header {
  margin-bottom: 8px;
}

.chart-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.chart-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.chart-dot.teal { background: var(--c-primary); }
.chart-dot.blue { background: var(--c-blue); }
.chart-dot.amber { background: var(--c-amber); }

.chart-title-row h4 {
  margin: 0;
  font-size: 15px;
  font-weight: 700;
}

.chart-header span {
  color: var(--c-muted);
  font-size: 12px;
}

.chart-body {
  height: calc(100% - 50px);
}

.table-wrap {
  padding: 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
}

.table-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-line {
  width: 4px;
  height: 18px;
  background: linear-gradient(180deg, var(--c-amber), var(--c-orange));
  border-radius: 2px;
}

.table-title span {
  font-size: 17px;
  font-weight: 700;
}

@media (max-width: 1120px) {
  .chart-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .reports-page {
    padding: 20px 16px;
  }

  .chart-grid {
    grid-template-columns: 1fr;
  }
}
</style>
