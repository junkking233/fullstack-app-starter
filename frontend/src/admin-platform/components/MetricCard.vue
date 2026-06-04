<script setup>
defineProps({
  item: { type: Object, required: true },
});

const icons = {
  "今日访问量": "📈",
  "新增订单": "🛒",
  "待处理告警": "🔔",
  "接口异常率": "⚡",
  "本月销售额": "💰",
  "有效线索": "🎯",
  "成交转化率": "🎉",
  "待处理异常": "🔔",
  "角色数量": "👥",
  "成员账号": "🧑‍💻",
  "待处理权限": "🔐",
};
</script>

<template>
  <div class="metric" :class="item.tone">
    <div class="metric-icon">{{ icons[item.label] || "📊" }}</div>
    <div>
      <span class="metric-label">{{ item.label }}</span>
      <strong class="metric-value">{{ item.value }}</strong>
      <em class="metric-delta">{{ item.trend }}</em>
    </div>
  </div>
</template>

<style scoped>
.metric {
  min-height: 130px;
  position: relative;
  overflow: hidden;
  padding: 24px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: var(--surface-elevated);
  border: 1px solid var(--line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
  transition: all var(--transition);
  cursor: pointer;
  user-select: none;
}
.metric:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-3px);
  border-color: var(--line-strong);
}
.metric:active { transform: translateY(-1px) scale(.98); }
.metric::before {
  content: "";
  position: absolute; right: -30px; bottom: -40px;
  width: 120px; height: 120px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--metric-glow, rgba(79,70,229,.08)) 0%, transparent 70%);
  transition: all var(--transition-slow);
}
.metric:hover::before { transform: scale(1.3); }

.metric-icon {
  width: 40px; height: 40px;
  border-radius: var(--radius);
  display: grid; place-items: center;
  font-size: 18px;
  margin-bottom: 12px;
  background: var(--metric-icon-bg, var(--primary-soft));
  color: var(--metric-icon-color, var(--primary));
  box-shadow: 0 2px 8px var(--metric-icon-shadow, rgba(16,191,163,.15));
}
.metric-label { color: var(--muted); font-size: 13px; font-weight: 500; }
.metric-value {
  display: block;
  margin-top: 6px;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -1px;
  color: var(--ink);
  line-height: 1.2;
}
.metric-delta {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-top: 8px;
  font-style: normal;
  font-size: 12px;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 6px;
  background: var(--metric-delta-bg, var(--success-soft));
  color: var(--metric-delta-color, var(--success));
}

.metric.warning {
  --metric-glow: rgba(255,159,67,.1);
  --metric-icon-bg: var(--warning-soft);
  --metric-icon-color: var(--warning);
  --metric-icon-shadow: rgba(255,159,67,.15);
  --metric-delta-bg: var(--warning-soft);
  --metric-delta-color: var(--warning);
}
.metric.danger {
  --metric-glow: rgba(255,92,72,.1);
  --metric-icon-bg: var(--danger-soft);
  --metric-icon-color: var(--danger);
  --metric-icon-shadow: rgba(255,92,72,.15);
  --metric-delta-bg: var(--danger-soft);
  --metric-delta-color: var(--danger);
}
.metric.info {
  --metric-glow: rgba(30,115,232,.1);
  --metric-icon-bg: var(--info-soft);
  --metric-icon-color: var(--info);
  --metric-icon-shadow: rgba(30,115,232,.15);
  --metric-delta-bg: var(--info-soft);
  --metric-delta-color: var(--info);
}
</style>
