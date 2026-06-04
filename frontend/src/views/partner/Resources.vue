<script setup lang="ts">
import { ref } from 'vue';
import {
  Delete,
  Document,
  Download,
  Picture,
  Plus,
  Search,
  VideoCamera,
} from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const keyword = ref('');
const typeFilter = ref('');

const typeOptions = [
  { label: '全部', value: '' },
  { label: '文档', value: 'doc' },
  { label: '图片', value: 'image' },
  { label: '视频', value: 'video' },
];

const resources = ref([
  { id: 1, name: '企业入驻指南.pdf', type: 'doc', size: '2.4 MB', updateTime: '2025-06-01 10:00' },
  { id: 2, name: '服务流程说明.docx', type: 'doc', size: '1.1 MB', updateTime: '2025-05-28 14:30' },
  { id: 3, name: '平台宣传海报.png', type: 'image', size: '3.8 MB', updateTime: '2025-05-25 09:00' },
  { id: 4, name: '培训视频教程.mp4', type: 'video', size: '156 MB', updateTime: '2025-05-20 16:00' },
  { id: 5, name: '价格表模板.xlsx', type: 'doc', size: '0.5 MB', updateTime: '2025-05-18 11:20' },
]);

const typeIconMap: Record<string, any> = {
  doc: Document,
  image: Picture,
  video: VideoCamera,
};

const typeColorMap: Record<string, { bg: string; color: string }> = {
  doc: { bg: '#eff6ff', color: '#3b82f6' },
  image: { bg: '#fffbeb', color: '#f59e0b' },
  video: { bg: '#fef2f2', color: '#ef4444' },
};

const typeLabelMap: Record<string, string> = {
  doc: '文档',
  image: '图片',
  video: '视频',
};

async function removeResource(row: any) {
  await ElMessageBox.confirm(`确认删除资源 ${row.name}？`, '删除确认', { type: 'warning' });
  resources.value = resources.value.filter((r) => r.id !== row.id);
  ElMessage.success('删除成功');
}
</script>

<template>
  <div class="resources-page">
    <div class="page-header">
      <div class="header-text">
        <h1>资源管理</h1>
        <p>上传和管理您的业务资源文件</p>
      </div>
      <el-button type="primary" :icon="Plus" class="upload-btn">
        上传资源
      </el-button>
    </div>

    <section class="toolbar">
      <el-input v-model="keyword" clearable placeholder="搜索资源名称" :prefix-icon="Search" style="width: 280px" />
      <el-select v-model="typeFilter" clearable placeholder="类型" style="width: 140px">
        <el-option v-for="t in typeOptions" :key="t.value" :label="t.label" :value="t.value" />
      </el-select>
    </section>

    <div class="resource-grid">
      <div v-for="r in resources" :key="r.id" class="resource-card">
        <div class="resource-icon" :style="{ background: typeColorMap[r.type].bg, color: typeColorMap[r.type].color }">
          <el-icon><component :is="typeIconMap[r.type]" /></el-icon>
        </div>
        <div class="resource-info">
          <h4>{{ r.name }}</h4>
          <div class="resource-meta">
            <el-tag size="small" effect="light" round>{{ typeLabelMap[r.type] }}</el-tag>
            <span>{{ r.size }}</span>
            <span class="divider">·</span>
            <span>{{ r.updateTime }}</span>
          </div>
        </div>
        <div class="resource-actions">
          <el-button :icon="Download" text circle class="action-btn" />
          <el-button :icon="Delete" text circle type="danger" class="action-btn danger" @click="removeResource(r)" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.resources-page {
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

.upload-btn {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 20px;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 18px;
  padding: 14px 18px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.resource-grid {
  display: grid;
  gap: 12px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.resource-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xs);
  transition: all var(--transition-base);
}

.resource-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
  border-color: rgb(245 158 11 / 15%);
}

.resource-icon {
  display: grid;
  flex-shrink: 0;
  width: 50px;
  height: 50px;
  place-items: center;
  font-size: 22px;
  border-radius: var(--radius-md);
  transition: transform var(--transition-base);
}

.resource-card:hover .resource-icon {
  transform: scale(1.1);
}

.resource-info {
  flex: 1;
  min-width: 0;
}

.resource-info h4 {
  margin: 0 0 8px;
  font-size: 15px;
  font-weight: 600;
}

.resource-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.resource-meta span {
  color: var(--c-muted);
  font-size: 12px;
}

.divider {
  color: var(--c-line) !important;
}

.resource-actions {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}

.action-btn {
  transition: all var(--transition-fast);
}

.action-btn:hover {
  transform: scale(1.1);
}

@media (max-width: 860px) {
  .resources-page {
    padding: 20px 16px;
  }

  .resource-grid {
    grid-template-columns: 1fr;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
