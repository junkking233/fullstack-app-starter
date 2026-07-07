<script setup lang="ts">
import { useRouter } from 'vue-router';
import { Connection, Key, Monitor, User } from '@element-plus/icons-vue';

const router = useRouter();

const scaffoldItems = [
  {
    title: '账号与权限',
    desc: '保留登录、注册、Token、用户角色和后台用户管理示例。',
    icon: Key,
  },
  {
    title: '前后端接口',
    desc: '保留 Axios 封装、统一响应结构和 Spring Boot API 骨架。',
    icon: Connection,
  },
  {
    title: 'Figma-first 开发',
    desc: '业务页面按 PRD、Figma Frame 和 GoalPlan 逐页实现。',
    icon: Monitor,
  },
];
</script>

<template>
  <div class="home-page">
    <section class="hero">
      <div>
        <p class="eyebrow">Fullstack Business Scaffold</p>
        <h1>保留最小可运行案例，业务功能按 PRD 扩展。</h1>
        <p class="summary">
          这个脚手架只提供登录、用户管理、接口封装和基础布局。新项目开始后，请先完成 ScopeBudget，再按 Figma 设计稿实现 P0/P1 页面。
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="router.push('/login')">
            <el-icon><User /></el-icon>
            登录示例
          </el-button>
          <el-button size="large" @click="router.push('/admin/users')">
            用户管理
          </el-button>
        </div>
      </div>
    </section>

    <section class="feature-grid">
      <article v-for="item in scaffoldItems" :key="item.title" class="feature-card">
        <div class="feature-icon">
          <el-icon><component :is="item.icon" /></el-icon>
        </div>
        <h2>{{ item.title }}</h2>
        <p>{{ item.desc }}</p>
      </article>
    </section>
  </div>
</template>

<style scoped>
.home-page {
  display: grid;
  gap: 24px;
}

.hero {
  padding: 44px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
}

.eyebrow {
  margin: 0 0 10px;
  color: var(--c-primary);
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.3px;
}

h1 {
  max-width: 760px;
  margin: 0;
  color: var(--c-ink);
  font-size: 36px;
  line-height: 1.2;
  letter-spacing: 0;
}

.summary {
  max-width: 720px;
  margin: 18px 0 0;
  color: var(--c-body);
  font-size: 16px;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  gap: 12px;
  margin-top: 28px;
  flex-wrap: wrap;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.feature-card {
  padding: 24px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xs);
}

.feature-icon {
  display: grid;
  width: 42px;
  height: 42px;
  place-items: center;
  color: var(--c-primary);
  background: var(--c-primary-bg);
  border-radius: 12px;
  font-size: 20px;
}

.feature-card h2 {
  margin: 16px 0 8px;
  color: var(--c-ink);
  font-size: 18px;
}

.feature-card p {
  margin: 0;
  color: var(--c-muted);
  line-height: 1.7;
}

@media (max-width: 760px) {
  .hero {
    padding: 28px;
  }

  h1 {
    font-size: 28px;
  }

  .feature-grid {
    grid-template-columns: 1fr;
  }
}
</style>
