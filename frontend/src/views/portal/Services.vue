<script setup lang="ts">
import { ref } from 'vue';
import {
  ArrowRight,
  ChatDotRound,
  DocumentChecked,
  Filter,
  Monitor,
  Search,
  TrendCharts,
  Wallet,
} from '@element-plus/icons-vue';

const keyword = ref('');
const category = ref('');

const categories = [
  { label: '全部', value: '', count: 8 },
  { label: '政务服务', value: 'gov', count: 3 },
  { label: '生活服务', value: 'life', count: 2 },
  { label: '企业服务', value: 'biz', count: 2 },
  { label: '金融服务', value: 'finance', count: 1 },
];

const services = [
  { title: '在线预约', category: 'gov', desc: '在线预约各类政务服务，减少排队等待时间', icon: DocumentChecked, hot: true, color: 'teal' },
  { title: '进度查询', category: 'gov', desc: '实时查询业务办理进度，掌握最新状态', icon: TrendCharts, hot: false, color: 'blue' },
  { title: '缴费服务', category: 'life', desc: '水电燃气、物业费等生活缴费一站式办理', icon: Wallet, hot: true, color: 'amber' },
  { title: '企业开办', category: 'biz', desc: '企业注册、变更、注销等工商业务办理', icon: Monitor, hot: false, color: 'green' },
  { title: '智能客服', category: 'life', desc: '7×24小时在线解答常见问题', icon: ChatDotRound, hot: true, color: 'teal' },
  { title: '贷款申请', category: 'finance', desc: '个人及企业贷款在线申请与进度跟踪', icon: Wallet, hot: false, color: 'blue' },
  { title: '证件办理', category: 'gov', desc: '身份证、护照、驾照等各类证件业务', icon: DocumentChecked, hot: false, color: 'green' },
  { title: '数据报表', category: 'biz', desc: '企业运营数据可视化分析与报表导出', icon: TrendCharts, hot: false, color: 'amber' },
];

const filteredServices = ref(services);

function filterServices() {
  filteredServices.value = services.filter((s) => {
    const matchKeyword = !keyword.value || s.title.includes(keyword.value) || s.desc.includes(keyword.value);
    const matchCategory = !category.value || s.category === category.value;
    return matchKeyword && matchCategory;
  });
}

function resetFilter() {
  keyword.value = '';
  category.value = '';
  filteredServices.value = services;
}
</script>

<template>
  <div class="services-page">
    <div class="page-header">
      <div class="header-text">
        <h1>服务大厅</h1>
        <p>浏览和搜索平台提供的全部服务</p>
      </div>
      <div class="header-stats">
        <div class="stat-pill">
          <strong>{{ services.length }}</strong>
          <span>项服务</span>
        </div>
      </div>
    </div>

    <!-- 搜索区 -->
    <section class="filter-bar">
      <div class="filter-left">
        <el-input
          v-model="keyword"
          clearable
          placeholder="搜索服务名称"
          :prefix-icon="Search"
          style="width: 280px"
          @keyup.enter="filterServices"
        />
        <el-select v-model="category" clearable placeholder="服务分类" style="width: 160px" @change="filterServices">
          <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value">
            <span>{{ c.label }}</span>
            <span style="float:right;color:var(--c-muted);font-size:12px">{{ c.count }}项</span>
          </el-option>
        </el-select>
      </div>
      <div class="filter-right">
        <el-button :icon="Filter" @click="filterServices">筛选</el-button>
        <el-button text @click="resetFilter">重置</el-button>
      </div>
    </section>

    <!-- 分类标签 -->
    <div class="category-bar">
      <button
        v-for="c in categories"
        :key="c.value"
        class="category-btn"
        :class="{ active: category === c.value }"
        @click="category = c.value; filterServices()"
      >
        {{ c.label }}
        <span class="category-count">{{ c.count }}</span>
      </button>
    </div>

    <!-- 服务列表 -->
    <div class="services-grid">
      <article v-for="s in filteredServices" :key="s.title" class="service-item">
        <div class="service-visual" :class="s.color">
          <el-icon><component :is="s.icon" /></el-icon>
        </div>
        <div class="service-content">
          <div class="service-title-row">
            <h3>{{ s.title }}</h3>
            <el-tag v-if="s.hot" size="small" type="danger" effect="light" round>热门</el-tag>
          </div>
          <p>{{ s.desc }}</p>
        </div>
        <div class="service-action">
          <el-button type="primary" size="small" round class="action-btn">
            立即办理 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </article>
    </div>

    <el-empty v-if="filteredServices.length === 0" description="暂无匹配的服务" />
  </div>
</template>

<style scoped>
.services-page {
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

.stat-pill {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: var(--c-primary-bg);
  border: 1px solid rgb(13 148 136 / 12%);
  border-radius: 100px;
}

.stat-pill strong {
  font-size: 20px;
  color: var(--c-primary);
}

.stat-pill span {
  color: var(--c-muted);
  font-size: 13px;
}

.filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 18px;
  padding: 18px 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.filter-right {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.category-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.category-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  color: var(--c-body);
  font-size: 14px;
  font-weight: 500;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: 100px;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.category-btn:hover {
  border-color: var(--c-primary);
  color: var(--c-primary);
}

.category-btn.active {
  color: #fff;
  background: linear-gradient(135deg, var(--c-primary), var(--c-primary-dark));
  border-color: transparent;
  box-shadow: 0 2px 8px rgb(13 148 136 / 25%);
}

.category-count {
  padding: 1px 8px;
  font-size: 11px;
  font-weight: 600;
  background: rgb(0 0 0 / 6%);
  border-radius: 100px;
}

.services-grid {
  display: grid;
  gap: 14px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.service-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 22px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xs);
  transition: all var(--transition-base);
}

.service-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
  border-color: rgb(13 148 136 / 15%);
}

.service-visual {
  display: grid;
  flex-shrink: 0;
  width: 54px;
  height: 54px;
  place-items: center;
  color: #fff;
  font-size: 24px;
  border-radius: var(--radius-md);
  transition: transform var(--transition-base);
}

.service-item:hover .service-visual {
  transform: scale(1.08) rotate(-3deg);
}

.service-visual.teal { background: linear-gradient(135deg, #0d9488, #0f766e); }
.service-visual.blue { background: linear-gradient(135deg, #3b82f6, #1d4ed8); }
.service-visual.amber { background: linear-gradient(135deg, #f59e0b, #f97316); }
.service-visual.green { background: linear-gradient(135deg, #22c55e, #16a34a); }

.service-content {
  flex: 1;
  min-width: 0;
}

.service-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.service-title-row h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
}

.service-content p {
  margin: 0;
  color: var(--c-muted);
  font-size: 13px;
  line-height: 1.6;
}

.service-action {
  flex-shrink: 0;
}

.action-btn {
  font-weight: 600;
}

@media (max-width: 860px) {
  .services-page {
    padding: 20px 16px;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-left {
    width: 100%;
  }

  .filter-left .el-input,
  .filter-left .el-select {
    width: 100% !important;
  }

  .services-grid {
    grid-template-columns: 1fr;
  }

  .service-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
