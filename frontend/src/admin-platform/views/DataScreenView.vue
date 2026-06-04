<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import ScreenPanel from "../components/ScreenPanel.vue";
import ProgressList from "../components/ProgressList.vue";

const emit = defineEmits(["back"]);

const now = ref("");
let timer = null;
function updateTime() {
  const d = new Date();
  now.value = d.toLocaleString("zh-CN", {
    year: "numeric", month: "2-digit", day: "2-digit",
    hour: "2-digit", minute: "2-digit", second: "2-digit",
    hour12: false,
  });
}
onMounted(() => { updateTime(); timer = setInterval(updateTime, 1000); });
onUnmounted(() => clearInterval(timer));

const cities = [
  { name: "上海", percent: 92, color: "var(--primary)" },
  { name: "杭州", percent: 87, color: "var(--success)" },
  { name: "深圳", percent: 80, color: "var(--info)" },
  { name: "成都", percent: 68, color: "var(--warning)" },
  { name: "南京", percent: 56, color: "var(--primary)" },
];

const funnel = [
  { label: "访问", percent: 100, value: "42,890", color: "var(--primary)" },
  { label: "线索", percent: 72, value: "18,492", color: "var(--info)" },
  { label: "商机", percent: 48, value: "8,760", color: "var(--success)" },
  { label: "成交", percent: 27, value: "3,120", color: "var(--warning)" },
];

const alerts = [
  { text: "支付回调延迟", level: "P1", cls: "p1" },
  { text: "华东库存偏低", level: "P2", cls: "p2" },
  { text: "接口错误率升高", level: "P2", cls: "p2" },
  { text: "同步任务完成", level: "OK", cls: "ok" },
  { text: "数据库连接池满载", level: "P1", cls: "p1" },
];

const marquees = [
  "系统运行正常 · 数据同步实时 · 华东节点负载 72% · 华北节点负载 58% · 华南节点负载 65% ·",
  "今日交易峰值 14:32 · 支付成功率 99.97% · 平均响应 128ms · 在线用户 12,847 ·",
];

const metrics = [
  { label: "销售额", value: "8,642.9万", trend: "+16.8% ▲", accent: "#10BFA3", icon: "💎" },
  { label: "订单数", value: "128,492", trend: "+12.4% ▲", accent: "#22C55E", icon: "📦" },
  { label: "客户数", value: "24,890", trend: "+6.2% ▲", accent: "#1E73E8", icon: "👥" },
  { label: "异常数", value: "36", trend: "待处理 ⚠", accent: "#FF5C48", icon: "⚡", alert: true },
];

const trendBars = [72,88,66,112,138,122,151,132,158,172,146,184,160,195,210,188];
</script>

<template>
  <section class="screen">
    <!-- 背景光效 -->
    <div class="bg-glow bg-glow-1"></div>
    <div class="bg-glow bg-glow-2"></div>
    <div class="scanline"></div>

    <!-- 顶部状态栏 -->
    <header class="screen-header">
      <div class="header-left">
        <span class="live-dot"></span>
        <span class="live-text">LIVE</span>
        <span class="sys-status">● 系统正常运行中</span>
      </div>
      <div class="header-center">
        <div class="title-decoration left"></div>
        <h1>企业经营数据实时监控大屏</h1>
        <div class="title-decoration right"></div>
        <p>REAL-TIME BUSINESS OPERATION DASHBOARD</p>
      </div>
      <div class="header-right">
        <div class="clock">{{ now }}</div>
        <button class="back-btn" @click="emit('back')">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <path d="M3 12h18M3 12l6-6M3 12l6 6"/>
          </svg>
          返回控制台
        </button>
      </div>
    </header>

    <!-- 核心指标 -->
    <div class="screen-metrics">
      <div v-for="m in metrics" :key="m.label" class="metric-card" :style="{ '--accent': m.accent }">
        <div class="metric-glow"></div>
        <div class="metric-icon">{{ m.icon }}</div>
        <span class="metric-label">{{ m.label }}</span>
        <b class="metric-value">{{ m.value }}</b>
        <em :class="{ alert: m.alert }">{{ m.trend }}</em>
        <div class="metric-border"></div>
      </div>
    </div>

    <!-- 主内容网格 -->
    <div class="screen-grid">
      <!-- 区域排行 -->
      <ScreenPanel title="🔥 区域销售排行">
        <div class="rank-bars">
          <div v-for="(c, i) in cities" :key="c.name" class="rank-bar" :style="{ animationDelay: i * 120 + 'ms' }">
            <span class="rank-name">{{ c.name }}</span>
            <div class="rank-track"><div class="rank-fill" :style="{ width: c.percent + '%' }"></div></div>
            <span class="rank-num">{{ c.percent }}%</span>
          </div>
        </div>
        <div class="rank-summary">
          <div class="rank-stat"><b>华东</b><span>贡献 38%</span></div>
          <div class="rank-stat"><b>华南</b><span>贡献 29%</span></div>
          <div class="rank-stat"><b>华北</b><span>贡献 21%</span></div>
        </div>
      </ScreenPanel>

      <!-- 热力分布 -->
      <ScreenPanel title="🗺️ 全国业务热力分布" class="screen-map">
        <div class="heat">
          <svg class="heat-lines" viewBox="0 0 400 300">
            <line x1="80" y1="70" x2="200" y2="100" />
            <line x1="200" y1="100" x2="320" y2="200" />
            <line x1="80" y1="70" x2="150" y2="220" />
            <line x1="150" y1="220" x2="320" y2="200" />
            <line x1="200" y1="100" x2="150" y2="220" />
          </svg>
          <i class="heat-node h1"><span>上海</span></i>
          <i class="heat-node h2"><span>杭州</span></i>
          <i class="heat-node h3"><span>深圳</span></i>
          <i class="heat-node h4"><span>成都</span></i>
          <i class="heat-node h5"><span>北京</span></i>
        </div>
        <p class="heat-note">
          <span class="pulse-dot"></span>
          华东区域贡献最高，线上渠道增长最快
        </p>
      </ScreenPanel>

      <!-- 渠道占比 -->
      <ScreenPanel title="📊 渠道转化占比">
        <div class="rings">
          <div class="ring-outer">
            <div class="ring-inner">
              <div class="ring-center">
                <span class="rc-label">总转化</span>
                <b class="rc-value">27.6%</b>
              </div>
            </div>
          </div>
        </div>
        <div class="channel-legend">
          <div><i style="background:#10BFA3"></i><b>直营</b><span>38%</span></div>
          <div><i style="background:#22C55E"></i><b>线上</b><span>24%</span></div>
          <div><i style="background:#FF9F43"></i><b>代理</b><span>21%</span></div>
          <div><i style="background:#1E73E8"></i><b>其他</b><span>17%</span></div>
        </div>
      </ScreenPanel>

      <!-- 实时告警 -->
      <ScreenPanel title="🚨 实时告警中心">
        <div class="alert-list">
          <div v-for="(a, i) in alerts" :key="i" class="alert-item" :class="a.cls">
            <div class="alert-bar"></div>
            <span class="alert-text">{{ a.text }}</span>
            <span class="alert-level">{{ a.level }}</span>
            <span class="alert-time">{{ i + 1 }}分钟前</span>
          </div>
        </div>
      </ScreenPanel>

      <!-- 销售漏斗 -->
      <ScreenPanel title="🌊 核心流程漏斗">
        <div class="funnel-viz">
          <div v-for="(f, i) in funnel" :key="f.label" class="funnel-step" :style="{ '--step-width': (100 - i * 18) + '%', '--step-color': f.color, animationDelay: i * 150 + 'ms' }">
            <span class="funnel-label">{{ f.label }}</span>
            <b class="funnel-value">{{ f.value }}</b>
            <span class="funnel-rate">{{ f.percent }}%</span>
          </div>
        </div>
      </ScreenPanel>

      <!-- 业务趋势 -->
      <ScreenPanel title="📈 业务趋势预测">
        <div class="trend-viz">
          <div class="trend-bars">
            <div v-for="(h, i) in trendBars" :key="i" class="trend-bar" :style="{ height: (h / 240 * 100) + '%', animationDelay: i * 60 + 'ms' }"></div>
          </div>
          <svg class="trend-line" viewBox="0 0 360 140" preserveAspectRatio="none">
            <defs>
              <linearGradient id="trendArea" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#10BFA3" stop-opacity=".35"/>
                <stop offset="100%" stop-color="#10BFA3" stop-opacity="0"/>
              </linearGradient>
            </defs>
            <path fill="url(#trendArea)" d="M10 120 C30 90 50 100 70 70 S110 40 140 55 180 20 210 35 250 10 280 25 320 5 350 15 L350 140 L10 140Z"/>
            <path class="trend-path" d="M10 120 C30 90 50 100 70 70 S110 40 140 55 180 20 210 35 250 10 280 25 320 5 350 15"/>
            <path class="trend-path-dash" d="M210 35 C230 45 250 25 280 20 S320 30 350 25"/>
          </svg>
        </div>
        <div class="trend-labels">
          <span>00:00</span><span>06:00</span><span>12:00</span><span>18:00</span><span>24:00</span>
        </div>
      </ScreenPanel>
    </div>

    <!-- 底部跑马灯 -->
    <div class="marquee-bar">
      <div class="marquee-track">
        <span v-for="(text, i) in marquees" :key="i">{{ text }}</span>
        <span v-for="(text, i) in marquees" :key="'r' + i">{{ text }}</span>
      </div>
    </div>
  </section>
</template>

<style scoped>
/* ========== 大屏基础 ========== */
.screen {
  min-height: 100vh;
  padding: 20px 36px 24px;
  color: #e2e8f0;
  background: linear-gradient(180deg, #0b1120 0%, #0f172a 40%, #1e293b 100%);
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 背景光效 */
.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(120px);
  pointer-events: none;
  opacity: .5;
}
.bg-glow-1 {
  width: 600px; height: 600px;
  top: -200px; left: 20%;
  background: radial-gradient(circle, rgba(99,102,241,.25), transparent 70%);
  animation: glowMove 12s ease-in-out infinite alternate;
}
.bg-glow-2 {
  width: 500px; height: 500px;
  bottom: -150px; right: 10%;
  background: radial-gradient(circle, rgba(6,182,212,.15), transparent 70%);
  animation: glowMove 15s ease-in-out infinite alternate-reverse;
}
@keyframes glowMove {
  from { transform: translate(0,0) scale(1); }
  to { transform: translate(40px, -30px) scale(1.1); }
}

/* 扫描线 */
.scanline {
  position: absolute;
  inset: 0;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 2px,
    rgba(99,102,241,.015) 2px,
    rgba(99,102,241,.015) 4px
  );
  pointer-events: none;
  z-index: 2;
}

/* ========== 顶部状态栏 ========== */
.screen-header {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 24px;
  margin-bottom: 20px;
  position: relative;
  z-index: 3;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.live-dot {
  width: 10px; height: 10px;
  border-radius: 50%;
  background: #22C55E;
  box-shadow: 0 0 12px 4px rgba(34,197,94,.4);
  animation: liveBlink 1.5s ease-in-out infinite;
}
@keyframes liveBlink {
  0%,100% { opacity: 1; transform: scale(1); }
  50% { opacity: .4; transform: scale(.8); }
}
.live-text {
  font-size: 12px;
  font-weight: 900;
  color: #22C55E;
  letter-spacing: 2px;
}
.sys-status {
  font-size: 12px;
  color: #64748b;
  font-weight: 600;
}

.header-center {
  text-align: center;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.title-decoration {
  position: absolute;
  top: 50%;
  width: 120px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(16,191,163,.6), transparent);
}
.title-decoration.left { right: calc(100% + 16px); }
.title-decoration.right { left: calc(100% + 16px); }
.title-decoration::after {
  content: "";
  position: absolute;
  top: -2px;
  width: 5px; height: 5px;
  border-radius: 50%;
  background: #10BFA3;
  box-shadow: 0 0 8px 2px rgba(16,191,163,.5);
}
.title-decoration.left::after { right: 0; }
.title-decoration.right::after { left: 0; }

.screen-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 900;
  letter-spacing: 4px;
  background: linear-gradient(180deg, #fff 0%, #a5b4fc 50%, #10BFA3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 0 40px rgba(99,102,241,.2);
}
.screen-header > .header-center > p {
  margin: 6px 0 0;
  color: #31D18B;
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 4px;
  opacity: .8;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  justify-content: flex-end;
}
.clock {
  font-size: 14px;
  font-weight: 700;
  color: #94a3b8;
  font-family: "SF Mono", monospace;
  letter-spacing: 1px;
  background: rgba(15,23,42,.6);
  padding: 6px 14px;
  border-radius: 8px;
  border: 1px solid rgba(99,102,241,.15);
}
.back-btn {
  height: 36px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
  border: 1px solid rgba(99,102,241,.3);
  border-radius: 8px;
  background: rgba(99,102,241,.1);
  color: #a5b4fc;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  transition: all .3s ease;
  backdrop-filter: blur(8px);
}
.back-btn:hover {
  background: rgba(99,102,241,.2);
  border-color: rgba(16,191,163,.5);
  color: #fff;
  box-shadow: 0 0 20px rgba(99,102,241,.25);
  transform: translateX(-2px);
}
.back-btn svg { width: 16px; height: 16px; }

/* ========== 指标卡片 ========== */
.screen-metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
  position: relative; z-index: 3;
}
.metric-card {
  position: relative;
  padding: 20px 22px;
  border-radius: var(--radius);
  background: rgba(15,23,42,.5);
  border: 1px solid rgba(99,102,241,.12);
  backdrop-filter: blur(12px);
  overflow: hidden;
  transition: all var(--transition);
  cursor: pointer;
  user-select: none;
}
.metric-card:hover {
  border-color: rgba(99,102,241,.3);
  transform: translateY(-4px);
  box-shadow: 0 16px 50px rgba(0,0,0,.5);
}
.metric-card:active { transform: translateY(-2px) scale(.98); }
.metric-card::before {
  content: "";
  position: absolute; top: 0; left: 0; right: 0; height: 2px;
  background: linear-gradient(90deg, var(--accent), transparent 80%);
}
.metric-glow {
  position: absolute;
  right: -40px; top: -40px;
  width: 100px; height: 100px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--accent), transparent 70%);
  opacity: .15;
  filter: blur(20px);
}
.metric-border {
  position: absolute;
  inset: 0;
  border-radius: var(--radius);
  padding: 1px;
  background: linear-gradient(135deg, var(--accent), transparent 50%, transparent);
  -webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  opacity: 0;
  transition: opacity var(--transition);
}
.metric-card:hover .metric-border { opacity: .5; }

.metric-icon {
  font-size: 22px;
  margin-bottom: 8px;
}
.metric-label {
  font-size: 12px;
  color: #64748b;
  font-weight: 700;
  letter-spacing: 1px;
}
.metric-value {
  display: block;
  margin-top: 8px;
  font-size: 26px;
  font-weight: 800;
  color: #fff;
  letter-spacing: -1px;
}
.metric-card em {
  display: inline-block;
  margin-top: 8px;
  font-style: normal;
  font-size: 12px;
  font-weight: 800;
  padding: 2px 8px;
  border-radius: 4px;
  background: rgba(34,197,94,.12);
  color: #4ade80;
}
.metric-card em.alert {
  background: rgba(255,92,72,.12);
  color: #ff8a7a;
}

/* ========== 主网格 ========== */
.screen-grid {
  display: grid;
  grid-template-columns: 320px 1fr 340px;
  grid-template-rows: 1fr 1fr;
  gap: 16px 20px;
  flex: 1;
  min-height: 0;
  position: relative;
  z-index: 3;
}
.screen-map { grid-row: span 2; }

/* ========== 区域排行 ========== */
.rank-bars { display: grid; gap: 14px; margin-top: 4px; }
.rank-bar {
  display: grid;
  grid-template-columns: 48px 1fr 44px;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  animation: slideIn .6s ease backwards;
  padding: 5px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}
.rank-bar:hover { background: rgba(255,255,255,.04); }
.rank-bar:active { transform: scale(.98); }
@keyframes slideIn { from { opacity:0; transform: translateX(-20px); } to { opacity:1; transform: translateX(0); } }
.rank-name { color: #94a3b8; font-weight: 600; }
.rank-track {
  height: 10px;
  border-radius: 999px;
  background: rgba(255,255,255,.06);
  overflow: hidden;
}
.rank-fill {
  height: 100%;
  border-radius: 999px;
  background: linear-gradient(90deg, #10BFA3, #31D18B);
  animation: fillGrow 1.2s cubic-bezier(.4,0,.2,1) backwards;
  animation-delay: inherit;
  box-shadow: 0 0 12px rgba(99,102,241,.3);
}
@keyframes fillGrow { from { width: 0 !important; } }
.rank-num { color: #cbd5e1; font-weight: 800; text-align: right; font-size: 12px; }

.rank-summary {
  display: flex;
  gap: 16px;
  margin-top: 18px;
  padding-top: 14px;
  border-top: 1px solid rgba(99,102,241,.1);
}
.rank-stat { text-align: center; flex: 1; cursor: pointer; padding: 8px; border-radius: 6px; transition: all var(--transition-fast); user-select: none; }
.rank-stat:hover { background: rgba(255,255,255,.04); }
.rank-stat:active { transform: scale(.97); }
.rank-stat b { display: block; color: #fff; font-size: 16px; font-weight: 800; }
.rank-stat span { display: block; color: #64748b; font-size: 11px; margin-top: 4px; }

/* ========== 热力图 ========== */
.heat {
  position: relative;
  height: 100%;
  min-height: 200px;
  border-radius: var(--radius);
  background: radial-gradient(circle at 30% 40%, rgba(99,102,241,.08) 0%, transparent 50%),
              radial-gradient(circle at 70% 60%, rgba(6,182,212,.06) 0%, transparent 50%);
  border: 1px solid rgba(99,102,241,.08);
  overflow: hidden;
}
.heat-lines {
  position: absolute; inset: 0;
  width: 100%; height: 100%;
}
.heat-lines line {
  stroke: rgba(99,102,241,.15);
  stroke-width: 1;
  stroke-dasharray: 4 4;
  animation: lineFlow 3s linear infinite;
}
@keyframes lineFlow { to { stroke-dashoffset: -16; } }

.heat-node {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(99,102,241,.2) 0%, transparent 70%);
  border: 1px solid rgba(99,102,241,.25);
  display: grid;
  place-items: center;
  animation: heatPulse 3s ease-in-out infinite;
  cursor: pointer;
  transition: all var(--transition-fast);
}
.heat-node:hover {
  background: radial-gradient(circle, rgba(99,102,241,.35) 0%, transparent 70%);
  border-color: rgba(16,191,163,.5);
  transform: scale(1.15);
}
.heat-node:active { transform: scale(1.08); }
.heat-node span {
  font-size: 11px;
  font-weight: 700;
  color: #a5b4fc;
  text-shadow: 0 0 8px rgba(16,191,163,.5);
}
.heat-node::after {
  content: "";
  position: absolute;
  width: 6px; height: 6px;
  border-radius: 50%;
  background: #FFD166;
  box-shadow: 0 0 16px 6px rgba(255,209,102,.3);
  animation: dotPulse 2s ease-in-out infinite;
}
.h1 { left: 18%; top: 18%; width: 90px; height: 70px; animation-delay: 0s; }
.h2 { left: 42%; top: 25%; width: 110px; height: 85px; animation-delay: .6s; }
.h3 { left: 62%; top: 55%; width: 100px; height: 75px; animation-delay: 1.2s; }
.h4 { left: 22%; top: 62%; width: 120px; height: 90px; animation-delay: 1.8s; }
.h5 { left: 55%; top: 15%; width: 80px; height: 60px; animation-delay: 2.4s; }

@keyframes heatPulse {
  0%,100% { transform: scale(1); opacity: .6; }
  50% { transform: scale(1.12); opacity: 1; }
}
@keyframes dotPulse {
  0%,100% { box-shadow: 0 0 16px 6px rgba(255,209,102,.3); }
  50% { box-shadow: 0 0 28px 12px rgba(255,209,102,.5); }
}

.heat-note {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
  color: #a5b4fc;
  font-size: 12px;
  font-weight: 700;
}
.pulse-dot {
  width: 8px; height: 8px;
  border-radius: 50%;
  background: #FFD166;
  box-shadow: 0 0 10px 3px rgba(255,209,102,.4);
  animation: liveBlink 2s ease-in-out infinite;
}

/* ========== 渠道占比环形图 ========== */
.rings {
  display: grid;
  place-items: center;
  padding: 8px 0;
}
.ring-outer {
  width: 160px; height: 160px;
  border-radius: 50%;
  padding: 10px;
  background: conic-gradient(
    #10BFA3 0% 38%,
    #22C55E 38% 62%,
    #FF9F43 62% 83%,
    #1E73E8 83% 100%
  );
  box-shadow: 0 8px 30px rgba(99,102,241,.2);
  animation: ringIn 1s cubic-bezier(.4,0,.2,1);
}
@keyframes ringIn { from { transform: scale(.7) rotate(-45deg); opacity:0; } to { transform: scale(1) rotate(0); opacity:1; } }
.ring-inner {
  width: 100%; height: 100%;
  border-radius: 50%;
  padding: 8px;
  background: #0b1120;
}
.ring-center {
  width: 100%; height: 100%;
  border-radius: 50%;
  background: radial-gradient(circle at 40% 40%, rgba(99,102,241,.1), transparent);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.rc-label { font-size: 11px; color: #64748b; font-weight: 700; letter-spacing: 1px; }
.rc-value { font-size: 22px; font-weight: 800; color: #fff; letter-spacing: -1px; margin-top: 2px; }

.channel-legend {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px 16px;
  margin-top: 16px;
}
.channel-legend div {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: all var(--transition-fast);
  user-select: none;
}
.channel-legend div:hover { background: rgba(255,255,255,.06); }
.channel-legend div:active { transform: scale(.97); }
.channel-legend i { width: 10px; height: 10px; border-radius: 3px; }
.channel-legend b { color: #e2e8f0; font-weight: 700; }
.channel-legend span { color: #64748b; font-weight: 600; margin-left: auto; }

/* ========== 告警列表 ========== */
.alert-list { display: grid; gap: 8px; }
.alert-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: 6px;
  background: rgba(255,255,255,.03);
  border: 1px solid rgba(255,255,255,.05);
  font-size: 12.5px;
  transition: all var(--transition-fast);
  position: relative;
  overflow: hidden;
  cursor: pointer;
  user-select: none;
}
.alert-item:hover {
  background: rgba(255,255,255,.08);
  transform: translateX(4px);
  border-color: rgba(99,102,241,.2);
}
.alert-item:active { transform: translateX(2px) scale(.99); }
.alert-bar {
  width: 3px; height: 20px;
  border-radius: 2px;
  flex-shrink: 0;
}
.alert-item.p1 .alert-bar { background: #FF5C48; box-shadow: 0 0 8px rgba(255,92,72,.4); animation: alertBlink 1.5s ease-in-out infinite; }
.alert-item.p2 .alert-bar { background: #FF9F43; box-shadow: 0 0 6px rgba(255,159,67,.3); }
.alert-item.ok .alert-bar { background: #22C55E; }
@keyframes alertBlink {
  0%,100% { opacity: 1; }
  50% { opacity: .3; }
}
.alert-text { color: #e2e8f0; font-weight: 600; flex: 1; }
.alert-level {
  font-size: 11px;
  font-weight: 800;
  padding: 2px 8px;
  border-radius: 4px;
}
.alert-item.p1 .alert-level { background: rgba(255,92,72,.15); color: #ff8a7a; }
.alert-item.p2 .alert-level { background: rgba(255,159,67,.15); color: #ffbd7a; }
.alert-item.ok .alert-level { background: rgba(34,197,94,.15); color: #4ade80; }
.alert-time { color: #475569; font-size: 11px; }

/* ========== 漏斗 ========== */
.funnel-viz { display: grid; gap: 10px; margin-top: 4px; }
.funnel-step {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  border-radius: 8px;
  background: linear-gradient(90deg, rgba(99,102,241,.12), transparent);
  border-left: 3px solid var(--step-color);
  font-size: 13px;
  width: var(--step-width);
  margin: 0 auto;
  animation: funnelIn .6s ease backwards;
  transition: all var(--transition-fast);
  cursor: pointer;
  user-select: none;
}
@keyframes funnelIn { from { opacity:0; transform: translateX(-20px); } to { opacity:1; transform: translateX(0); } }
.funnel-step:hover {
  background: linear-gradient(90deg, rgba(16,191,163,.22), transparent);
  transform: translateX(6px);
}
.funnel-step:active { transform: translateX(4px) scale(.99); }
.funnel-label { color: #94a3b8; font-weight: 700; width: 48px; }
.funnel-value { color: #fff; font-weight: 800; flex: 1; text-align: center; }
.funnel-rate { color: #a5b4fc; font-weight: 800; font-size: 12px; }

/* ========== 趋势图 ========== */
.trend-viz {
  position: relative;
  height: 160px;
  margin-top: 4px;
}
.trend-bars {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 4px;
  padding: 0 2px;
}
.trend-bar {
  flex: 1;
  border-radius: 3px 3px 0 0;
  background: linear-gradient(180deg, rgba(99,102,241,.2), rgba(16,191,163,.04));
  animation: barUp .8s ease backwards;
  animation-delay: inherit;
  cursor: pointer;
  transition: all var(--transition-fast);
}
.trend-bar:hover {
  background: linear-gradient(180deg, rgba(99,102,241,.35), rgba(99,102,241,.08));
  filter: brightness(1.2);
}
.trend-bar:active { transform: scaleY(.95); }
@keyframes barUp { from { height: 0 !important; } }
.trend-line {
  position: absolute; inset: 0;
  width: 100%; height: 100%;
  pointer-events: none;
}
.trend-path {
  fill: none;
  stroke: #a5b4fc;
  stroke-width: 3;
  stroke-linecap: round;
  stroke-linejoin: round;
  filter: drop-shadow(0 0 6px rgba(16,191,163,.4));
}
.trend-path-dash {
  fill: none;
  stroke: #FFD166;
  stroke-width: 2.5;
  stroke-linecap: round;
  stroke-dasharray: 6 5;
  filter: drop-shadow(0 0 4px rgba(255,209,102,.3));
}
.trend-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  padding: 0 4px;
  color: #475569;
  font-size: 11px;
  font-weight: 600;
}

/* ========== 跑马灯 ========== */
.marquee-bar {
  margin-top: 16px;
  padding: 10px 0;
  border-top: 1px solid rgba(99,102,241,.1);
  overflow: hidden;
  position: relative;
  z-index: 3;
}
.marquee-track {
  display: flex;
  gap: 60px;
  width: max-content;
  animation: marquee 30s linear infinite;
}
@keyframes marquee { to { transform: translateX(-50%); } }
.marquee-track span {
  font-size: 12px;
  color: #475569;
  font-weight: 600;
  white-space: nowrap;
}

/* ========== 响应式 ========== */
@media (max-width: 1280px) {
  .screen-grid { grid-template-columns: 1fr 1fr; grid-template-rows: auto; }
  .screen-map { grid-row: span 1; }
  .screen-metrics { grid-template-columns: repeat(2, 1fr); }
  .screen-header { grid-template-columns: 1fr; text-align: center; gap: 12px; }
  .header-left, .header-right { justify-content: center; }
}
</style>
