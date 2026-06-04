<script setup lang="ts">
import {
  ArrowDown,
  ArrowUp,
  Calendar,
  Check,
  Collection,
  DataAnalysis,
  Tickets,
  TrendCharts,
} from '@element-plus/icons-vue';

const metrics = [
  { title: '待处理工单', value: '28', change: '+6', icon: Tickets, color: 'teal', bg: '#f0fdf9' },
  { title: '服务覆盖率', value: '96%', change: '+4%', icon: DataAnalysis, color: 'blue', bg: '#eff6ff' },
  { title: '待确认资料', value: '12', change: '-3', icon: Collection, color: 'amber', bg: '#fffbeb' },
  { title: '本周完成', value: '156', change: '+12', icon: TrendCharts, color: 'green', bg: '#f0fdf4' },
];

const timeline = [
  { time: '09:30', title: '处理新提交资料并分派负责人', type: 'primary' },
  { time: '11:00', title: '审核通过 3 份企业资质申请', type: 'success' },
  { time: '13:00', title: '同步服务结果给平台管理员', type: 'success' },
  { time: '15:30', title: '收到用户投诉工单，需加急处理', type: 'warning' },
  { time: '17:20', title: '生成今日运营日报', type: 'info' },
];

const todoList = [
  { title: '审核新入驻机构资质', priority: 'high', deadline: '今日 18:00' },
  { title: '回复用户咨询工单 #2847', priority: 'high', deadline: '今日 17:00' },
  { title: '更新服务价格表', priority: 'normal', deadline: '明日 12:00' },
  { title: '提交月度运营报告', priority: 'normal', deadline: '2025-06-05' },
];
</script>

<template>
  <div class="partner-dashboard">
    <div class="page-header">
      <div class="header-text">
        <h1>服务方工作台</h1>
        <p>欢迎回来，今日有 3 项高优先级任务待处理</p>
      </div>
      <div class="header-date">
        <el-icon><Calendar /></el-icon>
        <span>2025年6月2日 星期一</span>
      </div>
    </div>

    <!-- 数据指标 -->
    <div class="metrics-grid">
      <article v-for="m in metrics" :key="m.title" class="metric-card">
        <div class="metric-top">
          <div class="metric-icon" :style="{ background: m.bg, color: 'var(--c-' + m.color + ')' }">
            <el-icon><component :is="m.icon" /></el-icon>
          </div>
          <div class="metric-trend" :class="m.change.startsWith('+') ? 'up' : 'down'">
            <el-icon>
              <component :is="m.change.startsWith('+') ? ArrowUp : ArrowDown" />
            </el-icon>
            <span>{{ m.change }}</span>
          </div>
        </div>
        <div class="metric-value">{{ m.value }}</div>
        <div class="metric-label">{{ m.title }}</div>
      </article>
    </div>

    <!-- 下半区 -->
    <div class="dashboard-split">
      <section class="panel-card">
        <div class="panel-header">
          <div class="panel-title">
            <div class="title-dot" />
            <span>今日流程</span>
          </div>
          <el-button type="primary" link>查看全部</el-button>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="item in timeline"
            :key="item.time"
            :timestamp="item.time"
            :type="item.type as any"
          >
            {{ item.title }}
          </el-timeline-item>
        </el-timeline>
      </section>

      <section class="panel-card">
        <div class="panel-header">
          <div class="panel-title">
            <div class="title-dot" />
            <span>待办事项</span>
          </div>
          <el-tag type="danger" effect="light" round>
            {{ todoList.filter(t => t.priority === 'high').length }} 条紧急
          </el-tag>
        </div>
        <div class="todo-list">
          <div v-for="todo in todoList" :key="todo.title" class="todo-item">
            <div class="todo-check">
              <div class="check-circle" :class="todo.priority">
                <el-icon v-if="todo.priority === 'high'"><Check /></el-icon>
              </div>
            </div>
            <div class="todo-body">
              <span class="todo-title">{{ todo.title }}</span>
              <span class="todo-deadline">截止：{{ todo.deadline }}</span>
            </div>
            <el-tag size="small" :type="todo.priority === 'high' ? 'danger' : 'info'" effect="light" round>
              {{ todo.priority === 'high' ? '紧急' : '普通' }}
            </el-tag>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped>
.partner-dashboard {
  max-width: 1180px;
  margin: 0 auto;
  padding: 32px 28px;
}

.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 24px;
}

.header-text h1 {
  margin: 0 0 6px;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.header-text p {
  margin: 0;
  color: var(--c-muted);
  font-size: 15px;
}

.header-date {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  color: var(--c-muted);
  font-size: 13px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: 100px;
}

.metrics-grid {
  display: grid;
  gap: 14px;
  margin-bottom: 20px;
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.metric-card {
  padding: 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xs);
  transition: all var(--transition-base);
}

.metric-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}

.metric-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.metric-icon {
  display: grid;
  width: 44px;
  height: 44px;
  place-items: center;
  font-size: 20px;
  border-radius: var(--radius-md);
}

.metric-trend {
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 4px 10px;
  font-size: 12px;
  font-weight: 700;
  border-radius: 100px;
}

.metric-trend.up {
  color: var(--c-green);
  background: var(--c-green-bg);
}

.metric-trend.down {
  color: var(--c-red);
  background: var(--c-red-bg);
}

.metric-value {
  font-size: 30px;
  font-weight: 800;
  color: var(--c-ink);
  line-height: 1;
  margin-bottom: 6px;
}

.metric-label {
  color: var(--c-muted);
  font-size: 13px;
}

.dashboard-split {
  display: grid;
  gap: 16px;
  grid-template-columns: minmax(0, 1fr) 440px;
}

.panel-card {
  padding: 24px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}

.panel-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-dot {
  width: 8px;
  height: 8px;
  background: linear-gradient(135deg, var(--c-amber), var(--c-orange));
  border-radius: 50%;
}

.panel-title span {
  font-size: 17px;
  font-weight: 700;
  color: var(--c-ink);
}

.todo-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.todo-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: var(--c-bg);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.todo-item:hover {
  background: var(--c-amber-bg);
  border-color: rgb(245 158 11 / 15%);
  transform: translateX(4px);
}

.check-circle {
  display: grid;
  width: 22px;
  height: 22px;
  place-items: center;
  border: 2px solid var(--c-line);
  border-radius: 50%;
  font-size: 11px;
  color: var(--c-surface);
  transition: all var(--transition-fast);
  cursor: pointer;
}

.check-circle.high {
  border-color: var(--c-red);
  background: var(--c-red);
}

.check-circle.normal {
  border-color: var(--c-muted-light);
}

.check-circle:hover {
  transform: scale(1.15);
}

.todo-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.todo-title {
  font-size: 14px;
  font-weight: 500;
  color: var(--c-body);
}

.todo-deadline {
  font-size: 12px;
  color: var(--c-muted-light);
}

@media (max-width: 1120px) {
  .dashboard-split {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 860px) {
  .partner-dashboard {
    padding: 20px 16px;
  }

  .metrics-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}

@media (max-width: 560px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>
