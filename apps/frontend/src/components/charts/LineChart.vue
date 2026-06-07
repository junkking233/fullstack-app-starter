<script setup lang="ts">
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { LineChart as ELineChart } from 'echarts/charts';
import {
  GridComponent,
  LegendComponent,
  TooltipComponent,
} from 'echarts/components';
import VChart from 'vue-echarts';
import { computed } from 'vue';

use([CanvasRenderer, ELineChart, GridComponent, LegendComponent, TooltipComponent]);

const props = defineProps<{
  title?: string;
  xData: string[];
  series: { name: string; data: number[]; color?: string }[];
}>();

const option = computed(() => ({
  tooltip: {
    trigger: 'axis',
    backgroundColor: '#fff',
    borderColor: '#e2e8f0',
    borderWidth: 1,
    textStyle: { color: '#334155', fontSize: 13 },
    padding: [10, 14],
    extraCssText: 'box-shadow: 0 4px 12px rgba(15,23,42,0.08); border-radius: 8px;',
  },
  legend: {
    bottom: 0,
    icon: 'circle',
    itemWidth: 8,
    itemHeight: 8,
    textStyle: { color: '#64748b', fontSize: 12 },
  },
  grid: {
    left: 16,
    right: 16,
    top: props.title ? 40 : 20,
    bottom: 32,
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: props.xData,
    axisLine: { lineStyle: { color: '#e2e8f0' } },
    axisTick: { show: false },
    axisLabel: { color: '#94a3b8', fontSize: 12 },
  },
  yAxis: {
    type: 'value',
    splitLine: { lineStyle: { color: '#f1f5f9', type: 'dashed' } },
    axisLabel: { color: '#94a3b8', fontSize: 12 },
  },
  series: props.series.map((s) => ({
    name: s.name,
    type: 'line',
    data: s.data,
    smooth: true,
    symbol: 'circle',
    symbolSize: 6,
    lineStyle: { width: 3, color: s.color || '#0d9488' },
    itemStyle: { color: s.color || '#0d9488', borderWidth: 2, borderColor: '#fff' },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: (s.color || '#0d9488') + '25' },
          { offset: 1, color: (s.color || '#0d9488') + '05' },
        ],
      },
    },
  })),
}));
</script>

<template>
  <div class="line-chart">
    <div v-if="title" class="chart-title">{{ title }}</div>
    <v-chart class="chart" :option="option" autoresize />
  </div>
</template>

<style scoped>
.line-chart {
  width: 100%;
  height: 100%;
}

.chart-title {
  font-size: 14px;
  font-weight: 700;
  color: var(--c-ink);
  margin-bottom: 8px;
}

.chart {
  width: 100%;
  height: calc(100% - 28px);
  min-height: 200px;
}
</style>
