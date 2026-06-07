<script setup lang="ts">
import { onMounted, ref } from 'vue';
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { BarChart, PieChart } from 'echarts/charts';
import { GridComponent, LegendComponent, TooltipComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { chartApi, type PieChartItem } from '@/api/chartApi';

use([BarChart, PieChart, GridComponent, LegendComponent, TooltipComponent, CanvasRenderer]);

const stage = ref<PieChartItem[]>([]);
const city = ref<PieChartItem[]>([]);
const teams = ref<PieChartItem[]>([]);
const matches = ref<PieChartItem[]>([]);
const points = ref<{ xData: string[]; series: { name: string; data: number[] }[] }>({ xData: [], series: [] });

onMounted(async () => {
  [stage.value, city.value, teams.value, matches.value, points.value] = await Promise.all([
    chartApi.matchStageDistribution(),
    chartApi.cityMatchCount(),
    chartApi.topFavoriteTeams(),
    chartApi.topFavoriteMatches(),
    chartApi.groupPoints(),
  ]);
});
function pie(title: string, data: PieChartItem[]) {
  return { tooltip: { trigger: 'item' }, legend: { bottom: 0 }, series: [{ name: title, type: 'pie', radius: '58%', data }] };
}
function bar(title: string, data: PieChartItem[]) {
  return { tooltip: {}, xAxis: { type: 'category', data: data.map((item) => item.name), axisLabel: { rotate: 30 } }, yAxis: { type: 'value' }, series: [{ name: title, type: 'bar', data: data.map((item) => item.value), itemStyle: { color: '#0d9488' } }], grid: { left: 40, right: 20, bottom: 80, top: 20 } };
}
function pointBar() {
  return { tooltip: {}, xAxis: { type: 'category', data: points.value.xData, axisLabel: { rotate: 45 } }, yAxis: { type: 'value' }, series: [{ name: '积分', type: 'bar', data: points.value.series[0]?.data || [], itemStyle: { color: '#2563eb' } }], grid: { left: 40, right: 20, bottom: 100, top: 20 } };
}
</script>

<template>
  <div class="chart-grid">
    <el-card header="比赛阶段分布"><VChart class="chart" :option="pie('阶段', stage)" autoresize /></el-card>
    <el-card header="城市比赛数量"><VChart class="chart" :option="bar('比赛数', city)" autoresize /></el-card>
    <el-card header="小组积分"><VChart class="chart" :option="pointBar()" autoresize /></el-card>
    <el-card header="热门收藏球队"><VChart class="chart" :option="bar('收藏数', teams)" autoresize /></el-card>
    <el-card header="热门收藏比赛"><VChart class="chart" :option="bar('收藏数', matches)" autoresize /></el-card>
  </div>
</template>

<style scoped>
.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}
.chart {
  height: 340px;
}
</style>
