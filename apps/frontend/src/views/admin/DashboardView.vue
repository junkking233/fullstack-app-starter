<script setup>
import MetricCard from "./components/MetricCard.vue";
import CardBox from "./components/CardBox.vue";

const metrics = [
  { label: "今日访问量", value: "128,492", trend: "较昨日 +12.8%", tone: "" },
  { label: "新增订单", value: "3,846", trend: "转化率 8.6%", tone: "" },
  { label: "待处理告警", value: "27", trend: "8 条高优先级", tone: "warning" },
  { label: "接口异常率", value: "0.38%", trend: "近 2 小时升高", tone: "danger" },
];

const depts = [
  ["销售中心", 88, "var(--primary)"],
  ["客服中心", 76, "var(--success)"],
  ["运营中心", 69, "var(--warning)"],
  ["技术中心", 93, "var(--info)"],
  ["财务中心", 81, "var(--primary)"],
];
</script>

<template>
  <div class="grid">
    <div class="grid metrics">
      <MetricCard v-for="item in metrics" :key="item.label" :item="item" />
    </div>
    <div class="grid two wide">
      <CardBox title="近 7 日业务趋势" sub="访问 / 订单 / 支付">
        <div class="chart">
          <svg viewBox="0 0 760 260" preserveAspectRatio="none">
            <defs>
              <linearGradient id="areaPrimary" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#10BFA3" />
                <stop offset="100%" stop-color="#10BFA3" stop-opacity="0" />
              </linearGradient>
              <linearGradient id="areaGreen" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#22C55E" />
                <stop offset="100%" stop-color="#22C55E" stop-opacity="0" />
              </linearGradient>
              <linearGradient id="areaOrange" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#FF9F43" />
                <stop offset="100%" stop-color="#FF9F43" stop-opacity="0" />
              </linearGradient>
              <linearGradient id="gradPrimary" x1="0" y1="0" x2="1" y2="0">
                <stop offset="0%" stop-color="#10BFA3" />
                <stop offset="100%" stop-color="#31D18B" />
              </linearGradient>
              <linearGradient id="gradGreen" x1="0" y1="0" x2="1" y2="0">
                <stop offset="0%" stop-color="#22C55E" />
                <stop offset="100%" stop-color="#86EFAC" />
              </linearGradient>
              <linearGradient id="gradOrange" x1="0" y1="0" x2="1" y2="0">
                <stop offset="0%" stop-color="#FF9F43" />
                <stop offset="100%" stop-color="#FBBF24" />
              </linearGradient>
            </defs>
            <path class="area-primary" d="M0 195 C80 147 118 181 170 133 S285 77 355 110 470 181 545 93 664 71 760 57 L760 260 L0 260Z"/>
            <path class="area-green" d="M0 210 C90 190 132 198 190 162 S286 126 360 143 466 200 540 150 660 122 760 102 L760 260 L0 260Z"/>
            <path class="area-orange" d="M0 220 C88 209 130 212 190 193 S284 169 360 179 460 211 540 183 660 159 760 141 L760 260 L0 260Z"/>
            <path class="line-primary" d="M0 195 C80 147 118 181 170 133 S285 77 355 110 470 181 545 93 664 71 760 57"/>
            <path class="line-green" d="M0 210 C90 190 132 198 190 162 S286 126 360 143 466 200 540 150 660 122 760 102"/>
            <path class="line-orange" d="M0 220 C88 209 130 212 190 193 S284 169 360 179 460 211 540 183 660 159 760 141"/>
          </svg>
        </div>
      </CardBox>
      <CardBox title="部门达成率" sub="本月">
        <div class="progress-list">
          <div v-for="d in depts" :key="d[0]" class="progress-row">
            <span>{{ d[0] }}</span>
            <div class="bar"><i :style="{ width: d[1] + '%', '--bar-color': d[2] }"></i></div>
            <b>{{ d[1] }}%</b>
          </div>
        </div>
      </CardBox>
    </div>
    <div class="grid two">
      <CardBox title="实时告警" sub="自动分级">
        <div class="alert-list">
          <p><span class="tag danger">紧急</span> 支付回调延迟超过阈值</p>
          <p><span class="tag warning">关注</span> 华东节点 CPU 持续偏高</p>
          <p><span class="tag">已恢复</span> 会员同步任务已恢复</p>
        </div>
      </CardBox>
      <CardBox title="快捷入口" sub="常用操作">
        <div class="quick-grid">
          <button class="btn primary">新增数据</button>
          <button class="btn">审批工单</button>
          <button class="btn">报表中心</button>
          <button class="btn">权限配置</button>
          <button class="btn">日志审计</button>
          <button class="btn">数据导入</button>
        </div>
      </CardBox>
    </div>
  </div>
</template>

<style scoped>
.grid { display: grid; gap: 20px; }
.metrics { grid-template-columns: repeat(4, minmax(0, 1fr)); }
.two { grid-template-columns: 1fr 1fr; }
.wide { grid-template-columns: 1.45fr .68fr; }

.chart {
  min-height: 260px;
  border-radius: var(--radius);
  background:
    linear-gradient(var(--line) 1px, transparent 1px) 0 0 / 100% 48px,
    linear-gradient(90deg, var(--line) 1px, transparent 1px) 0 0 / 92px 100%;
  overflow: hidden;
  position: relative;
  cursor: pointer;
  transition: all var(--transition-fast);
}
.chart:hover { filter: brightness(1.02); }
.chart:active { transform: scale(.99); }
.chart svg { width: 100%; height: 100%; display: block; pointer-events: none; }

.line-primary { fill: none; stroke: url(#gradPrimary); stroke-width: 4; stroke-linecap: round; stroke-linejoin: round; }
.line-green { fill: none; stroke: url(#gradGreen); stroke-width: 3.5; stroke-linecap: round; stroke-linejoin: round; }
.line-orange { fill: none; stroke: url(#gradOrange); stroke-width: 3.5; stroke-linecap: round; stroke-linejoin: round; }
.area-primary { fill: url(#areaPrimary); stroke: none; opacity: .25; }
.area-green { fill: url(#areaGreen); stroke: none; opacity: .2; }
.area-orange { fill: url(#areaOrange); stroke: none; opacity: .15; }

.progress-list { display: grid; gap: 16px; }
.progress-row {
  display: grid;
  grid-template-columns: 100px 1fr 56px;
  gap: 14px;
  align-items: center;
  font-size: 13px;
  padding: 6px 8px;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}
.progress-row:hover { background: #f8fafc; }
.progress-row:active { transform: scale(.99); }
.progress-row > span:first-child { color: var(--ink-secondary); font-weight: 600; }
.progress-row > b { text-align: right; color: var(--muted); font-weight: 700; }

.bar {
  height: 10px;
  border-radius: 999px;
  background: #f1f5f9;
  overflow: hidden;
  position: relative;
}
.bar i {
  display: block;
  height: 100%;
  border-radius: 999px;
  background: var(--bar-color, var(--primary));
  position: relative;
  animation: barGrow 1s cubic-bezier(.4,0,.2,1) forwards;
  transform-origin: left;
  transform: scaleX(0);
}
@keyframes barGrow { to { transform: scaleX(1); } }
.bar i::after {
  content: "";
  position: absolute; inset: 0;
  border-radius: 999px;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,.35), transparent);
  animation: barShine 2.5s ease-in-out infinite;
}
@keyframes barShine {
  0% { transform: translateX(-100%); }
  50%,100% { transform: translateX(100%); }
}

.alert-list p {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 13.5px;
  color: var(--ink-secondary);
  transition: all var(--transition-fast);
  cursor: pointer;
  border-radius: var(--radius-sm);
  user-select: none;
}
.alert-list p:last-child { border-bottom: none; }
.alert-list p:hover { background: #f8fafc; padding-left: 16px; }
.alert-list p:active { transform: scale(.99); }

.quick-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }
.quick-grid .btn { justify-content: center; height: 40px; }

.tag {
  display: inline-flex;
  height: 26px;
  align-items: center;
  border-radius: 6px;
  padding: 0 10px;
  font-size: 11.5px;
  font-weight: 700;
  letter-spacing: .3px;
  background: var(--success-soft);
  color: var(--success);
  border: 1px solid rgba(16,185,129,.15);
  transition: all var(--transition-fast);
}
.tag:hover { transform: scale(1.05); }
.tag.warning { background: var(--warning-soft); color: var(--warning); border-color: rgba(245,158,11,.15); }
.tag.danger { background: var(--danger-soft); color: var(--danger); border-color: rgba(244,63,94,.15); }

.btn {
  height: 36px;
  border: 1px solid var(--line-strong);
  border-radius: var(--radius-sm);
  padding: 0 16px;
  background: var(--surface);
  color: var(--ink-secondary);
  font-weight: 600; font-size: 13px;
  cursor: pointer;
  transition: all var(--transition-fast);
  display: inline-flex; align-items: center; gap: 6px;
}
.btn:hover {
  border-color: var(--line-strong);
  background: #f8fafc;
  transform: translateY(-1px);
  box-shadow: var(--shadow);
}
.btn:active { transform: translateY(0); }
.btn.primary {
  background: linear-gradient(135deg, var(--primary), #31D18B);
  border-color: transparent;
  color: #fff;
  box-shadow: 0 4px 14px rgba(79,70,229,.3);
}
.btn.primary:hover {
  box-shadow: 0 6px 20px rgba(79,70,229,.4);
  transform: translateY(-1px);
}

@media (max-width: 1280px) {
  .metrics { grid-template-columns: repeat(2, 1fr); }
  .two, .wide { grid-template-columns: 1fr; }
}
</style>
