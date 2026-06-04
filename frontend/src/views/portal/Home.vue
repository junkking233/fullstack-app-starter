<script setup lang="ts">
import { useRouter } from 'vue-router';
import {
  ArrowRight,
  ChatDotRound,
  DocumentChecked,
  MessageBox,
  Notification,
  Search,
  Star,
  TrendCharts,
} from '@element-plus/icons-vue';

const router = useRouter();

const services = [
  { title: '在线办理', desc: '表单填写、预约申请、进度查询一站完成', icon: DocumentChecked, color: 'teal' },
  { title: '消息中心', desc: '系统通知、站内信和业务提醒统一管理', icon: MessageBox, color: 'blue' },
  { title: '智能问答', desc: '接入 AI 对话能力，快速解答常见问题', icon: ChatDotRound, color: 'amber' },
  { title: '数据洞察', desc: '可视化数据看板，实时掌握业务动态', icon: TrendCharts, color: 'green' },
];

const notices = [
  { title: '平台服务升级公告', date: '2025-06-01', tag: '公告', tagType: 'info' },
  { title: '关于端午节假期服务安排的说明', date: '2025-05-28', tag: '通知', tagType: 'warning' },
  { title: '新功能上线：在线预约系统开放体验', date: '2025-05-20', tag: '上新', tagType: 'success' },
];

const quickLinks = [
  { label: '我要预约', path: '/portal/services', icon: Star },
  { label: '我的订单', path: '/portal/my-business', icon: DocumentChecked },
  { label: '意见反馈', path: '/portal/profile', icon: MessageBox },
  { label: '帮助中心', path: '/portal/services', icon: Notification },
];
</script>

<template>
  <div class="portal-home">
    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-bg">
        <div class="hero-orb orb-1" />
        <div class="hero-orb orb-2" />
      </div>
      <div class="hero-inner">
        <div class="hero-copy">
          <div class="hero-badge">
            <el-icon><Star /></el-icon>
            <span>面向普通用户的一站式服务平台</span>
          </div>
          <h1>
            把查询、办理和消息提醒
            <br>
            <span class="highlight">放在一个清爽入口</span>
          </h1>
          <p>适合快速改造成预约系统、服务平台、资讯门户或会员中心。完善的组件体系，开箱即用。</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" class="hero-btn-primary" @click="router.push('/portal/services')">
              <el-icon><Search /></el-icon>
              查找服务
            </el-button>
            <el-button size="large" class="hero-btn-secondary" @click="router.push('/portal/services')">
              <el-icon><ChatDotRound /></el-icon>
              咨询助手
            </el-button>
          </div>
        </div>

        <div class="hero-card">
          <div class="hero-card-header">
            <div class="card-title">
              <el-icon><DocumentChecked /></el-icon>
              <span>业务进度</span>
            </div>
            <el-tag type="warning" effect="dark" round size="small">进行中</el-tag>
          </div>
          <el-steps :active="2" finish-status="success" direction="vertical" class="hero-steps">
            <el-step title="提交申请" description="资料已通过基础校验" />
            <el-step title="平台审核" description="预计 1 个工作日内完成" />
            <el-step title="结果通知" description="短信和站内信同步提醒" />
          </el-steps>
        </div>
      </div>
    </section>

    <!-- 内容区域 -->
    <div class="content-wrapper">
      <!-- 常用能力 -->
      <section class="section-card">
        <div class="section-header">
          <div class="section-title">
            <div class="title-line" />
            <h3>常用能力</h3>
          </div>
          <el-button type="primary" link class="more-link" @click="router.push('/portal/services')">
            查看全部 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        <div class="service-grid">
          <article v-for="s in services" :key="s.title" class="service-card" @click="router.push('/portal/services')">
            <div class="service-icon" :class="s.color">
              <el-icon><component :is="s.icon" /></el-icon>
            </div>
            <div class="service-body">
              <h4>{{ s.title }}</h4>
              <p>{{ s.desc }}</p>
            </div>
            <el-icon class="service-arrow"><ArrowRight /></el-icon>
          </article>
        </div>
      </section>

      <!-- 下半区 -->
      <div class="split-grid">
        <section class="section-card">
          <div class="section-header">
            <div class="section-title">
              <div class="title-line" />
              <h3>最新公告</h3>
            </div>
            <el-button type="primary" link class="more-link">
              更多 <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          <div class="notice-list">
            <div v-for="n in notices" :key="n.title" class="notice-item">
              <div class="notice-main">
                <el-tag size="small" :type="n.tagType as any" effect="light" round>{{ n.tag }}</el-tag>
                <span class="notice-title">{{ n.title }}</span>
              </div>
              <span class="notice-date">{{ n.date }}</span>
            </div>
          </div>
        </section>

        <section class="section-card">
          <div class="section-header">
            <div class="section-title">
              <div class="title-line" />
              <h3>快捷入口</h3>
            </div>
          </div>
          <div class="quick-links">
            <div
              v-for="link in quickLinks"
              :key="link.label"
              class="quick-link"
              @click="router.push(link.path)"
            >
              <div class="quick-icon">
                <el-icon><component :is="link.icon" /></el-icon>
              </div>
              <span>{{ link.label }}</span>
              <el-icon class="quick-arrow"><ArrowRight /></el-icon>
            </div>
          </div>
          <div class="tips-box">
            <el-icon><Notification /></el-icon>
            <p>接口、列表、详情、审核流和消息通知都可以从这个区域继续拆组件。</p>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.portal-home {
  padding-bottom: 40px;
}

/* Hero */
.hero {
  position: relative;
  padding: 48px 28px 36px;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.hero-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
}

.orb-1 {
  width: 400px;
  height: 400px;
  top: -120px;
  right: 10%;
  background: rgb(13 148 136 / 10%);
}

.orb-2 {
  width: 300px;
  height: 300px;
  bottom: -80px;
  left: 5%;
  background: rgb(59 130 246 / 8%);
}

.hero-inner {
  position: relative;
  display: grid;
  align-items: center;
  gap: 32px;
  max-width: 1180px;
  margin: 0 auto;
  grid-template-columns: minmax(0, 1.3fr) minmax(340px, 0.7fr);
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  color: var(--c-primary);
  font-size: 13px;
  font-weight: 600;
  background: var(--c-primary-bg);
  border: 1px solid rgb(13 148 136 / 15%);
  border-radius: 100px;
  margin-bottom: 20px;
}

.hero-badge .el-icon {
  font-size: 15px;
}

.hero-copy h1 {
  max-width: 620px;
  margin: 0 0 16px;
  font-size: 42px;
  line-height: 1.2;
  letter-spacing: -1px;
  color: var(--c-ink);
  font-weight: 800;
}

.hero-copy h1 .highlight {
  background: linear-gradient(135deg, var(--c-primary), var(--c-blue));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-copy p {
  max-width: 520px;
  margin: 0;
  color: var(--c-muted);
  font-size: 16px;
  line-height: 1.7;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}

.hero-btn-primary {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 28px;
  height: 48px;
  font-size: 15px;
}

.hero-btn-secondary {
  font-weight: 600;
  border-radius: var(--radius-md);
  padding: 0 28px;
  height: 48px;
  font-size: 15px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  color: var(--c-body);
  transition: all var(--transition-fast);
}

.hero-btn-secondary:hover {
  border-color: var(--c-primary);
  color: var(--c-primary);
  background: var(--c-primary-bg);
  transform: translateY(-1px);
  box-shadow: var(--shadow-sm);
}

.hero-card {
  padding: 28px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
  transition: box-shadow var(--transition-base);
}

.hero-card:hover {
  box-shadow: var(--shadow-xl);
}

.hero-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  font-size: 15px;
}

.card-title .el-icon {
  color: var(--c-primary);
  font-size: 18px;
}

.hero-steps :deep(.el-step__title) {
  font-size: 14px;
  font-weight: 600;
}

.hero-steps :deep(.el-step__description) {
  font-size: 12px;
  color: var(--c-muted);
}

/* Content */
.content-wrapper {
  max-width: 1180px;
  margin: 0 auto;
  padding: 0 28px;
}

.section-card {
  padding: 24px;
  background: var(--c-surface);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  transition: box-shadow var(--transition-base);
}

.section-card:hover {
  box-shadow: var(--shadow-md);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-line {
  width: 4px;
  height: 20px;
  background: linear-gradient(180deg, var(--c-primary), var(--c-blue));
  border-radius: 2px;
}

.section-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: var(--c-ink);
}

.more-link {
  font-weight: 600;
}

/* Service Grid */
.service-grid {
  display: grid;
  gap: 14px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.service-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  cursor: pointer;
  background: linear-gradient(135deg, var(--c-surface), var(--c-line-light));
  border: 1px solid var(--c-line);
  border-radius: var(--radius-lg);
  transition: all var(--transition-base);
}

.service-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
  border-color: rgb(13 148 136 / 20%);
}

.service-icon {
  display: grid;
  flex-shrink: 0;
  width: 50px;
  height: 50px;
  place-items: center;
  color: #fff;
  font-size: 22px;
  border-radius: var(--radius-md);
  transition: transform var(--transition-base);
}

.service-card:hover .service-icon {
  transform: scale(1.08);
}

.service-icon.teal { background: linear-gradient(135deg, #0d9488, #0f766e); }
.service-icon.blue { background: linear-gradient(135deg, #3b82f6, #1d4ed8); }
.service-icon.amber { background: linear-gradient(135deg, #f59e0b, #f97316); }
.service-icon.green { background: linear-gradient(135deg, #22c55e, #16a34a); }

.service-body {
  flex: 1;
  min-width: 0;
}

.service-body h4 {
  margin: 0 0 6px;
  font-size: 15px;
  font-weight: 700;
  color: var(--c-ink);
}

.service-body p {
  margin: 0;
  color: var(--c-muted);
  font-size: 13px;
  line-height: 1.6;
}

.service-arrow {
  color: var(--c-muted-light);
  font-size: 16px;
  transition: all var(--transition-fast);
}

.service-card:hover .service-arrow {
  color: var(--c-primary);
  transform: translateX(3px);
}

/* Split Grid */
.split-grid {
  display: grid;
  gap: 16px;
  margin-top: 16px;
  grid-template-columns: minmax(0, 1.1fr) minmax(0, 0.9fr);
}

/* Notice */
.notice-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.notice-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 14px 16px;
  cursor: pointer;
  background: var(--c-bg);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.notice-item:hover {
  background: var(--c-primary-bg);
  border-color: rgb(13 148 136 / 15%);
  transform: translateX(4px);
}

.notice-main {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.notice-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
  color: var(--c-body);
  font-weight: 500;
}

.notice-date {
  flex-shrink: 0;
  color: var(--c-muted-light);
  font-size: 12px;
}

/* Quick Links */
.quick-links {
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.quick-link {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px;
  cursor: pointer;
  background: var(--c-bg);
  border: 1px solid var(--c-line);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.quick-link:hover {
  background: var(--c-primary-bg);
  border-color: rgb(13 148 136 / 15%);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.quick-icon {
  display: grid;
  width: 36px;
  height: 36px;
  place-items: center;
  color: var(--c-primary);
  font-size: 18px;
  background: var(--c-primary-bg);
  border-radius: var(--radius-sm);
  flex-shrink: 0;
}

.quick-link span {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
  color: var(--c-body);
}

.quick-arrow {
  color: var(--c-muted-light);
  font-size: 13px;
  transition: all var(--transition-fast);
}

.quick-link:hover .quick-arrow {
  color: var(--c-primary);
  transform: translateX(3px);
}

/* Tips */
.tips-box {
  display: flex;
  gap: 10px;
  margin-top: 16px;
  padding: 14px;
  color: var(--c-muted);
  font-size: 13px;
  line-height: 1.6;
  background: linear-gradient(135deg, var(--c-bg), var(--c-line-light));
  border: 1px dashed var(--c-line);
  border-radius: var(--radius-md);
}

.tips-box .el-icon {
  color: var(--c-amber);
  font-size: 18px;
  flex-shrink: 0;
  margin-top: 1px;
}

.tips-box p {
  margin: 0;
}

/* Responsive */
@media (max-width: 1120px) {
  .hero-inner,
  .split-grid {
    grid-template-columns: 1fr;
  }

  .hero-copy h1 {
    font-size: 32px;
  }
}

@media (max-width: 860px) {
  .hero {
    padding: 28px 16px 24px;
  }

  .hero-copy h1 {
    font-size: 26px;
  }

  .content-wrapper {
    padding: 0 16px;
  }

  .service-grid {
    grid-template-columns: 1fr;
  }

  .quick-links {
    grid-template-columns: 1fr;
  }

  .split-grid {
    grid-template-columns: 1fr;
  }
}
</style>
