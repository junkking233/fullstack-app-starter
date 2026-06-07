<script setup lang="ts">
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart as EPieChart } from 'echarts/charts';
import { LegendComponent, TooltipComponent } from 'echarts/components';
import VChart from 'vue-echarts';
import { computed } from 'vue';

use([CanvasRenderer, EPieChart, LegendComponent, TooltipComponent]);

const props = defineProps<{
  title?: string;
  data: { name: string; value: number }[];
  colors?: string[];
}>();

const defaultColors = ['#0d9488', '#3b82f6', '#f59e0b', '#22c55e', '#ef4444', '#84cc16', '#f97316', '#14b8a6'];

const option = computed(() => ({
  tooltip: {
    trigger: 'item',
    backgroundColor: '#fff',
    borderColor: '#e2e8f0',
    borderWidth: 1,
    textStyle: { color: '#334155', fontSize: 13 },
    padding: [10, 14],
    formatter: '{b}: {c} ({d}%)',
    extraCssText: 'box-shadow: 0 4px 12px rgba(15,23,42,0.08); border-radius: 8px;',
  },
  legend: {
    orient: 'vertical',
    right: 10,
    top: 'center',
    icon: 'circle',
    itemWidth: 8,
    itemHeight: 8,
    itemGap: 14,
    textStyle: { color: '#64748b', fontSize: 12 },
  },
  color: props.colors || defaultColors,
  series: [
    {
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 6,
        borderColor: '#fff',
        borderWidth: 2,
      },
      label: { show: false },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold',
          color: '#334155',
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.1)',
        },
      },
      labelLine: { show: false },
      data: props.data,
    },
  ],
}));
</script>

<template>
  <div class="pie-chart">
    <div v-if="title" class="chart-title">{{ title }}</div>
    <v-chart class="chart" :option="option" autoresize />
  </div>
</template>

<style scoped>
.pie-chart {
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
