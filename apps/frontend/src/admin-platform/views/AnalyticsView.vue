<script setup>
import MetricCard from "../components/MetricCard.vue";
import CardBox from "../components/CardBox.vue";
import ProgressList from "../components/ProgressList.vue";

const metrics = [
  { label: "本月销售额", value: "¥ 8,642,900", trend: "+16.8%", tone: "" },
  { label: "有效线索", value: "18,492", trend: "+9.4%", tone: "" },
  { label: "成交转化率", value: "27.6%", trend: "+3.1%", tone: "" },
  { label: "待处理异常", value: "36", trend: "需关注", tone: "warning" },
];

const bars = [72,88,66,112,138,122,151,132,158,172,146,184];

const funnel = [
  { label: "访问", percent: 100, value: "42,890", color: "var(--primary)" },
  { label: "线索", percent: 72, value: "18,492", color: "var(--info)" },
  { label: "商机", percent: 48, value: "8,760", color: "var(--success)" },
  { label: "成交", percent: 27, value: "3,120", color: "var(--warning)" },
];

const cities = [
  { name: "上海", percent: 92, color: "var(--primary)" },
  { name: "杭州", percent: 87, color: "var(--success)" },
  { name: "深圳", percent: 80, color: "var(--info)" },
  { name: "成都", percent: 68, color: "var(--warning)" },
  { name: "南京", percent: 56, color: "var(--primary)" },
];
</script>

<template>
  <div class="grid">
    <div class="grid metrics">
      <MetricCard v-for="item in metrics" :key="item.label" :item="item" />
    </div>
    <div class="grid two wide">
      <CardBox title="销售额与订单量组合分析" sub="近 12 周">
          <div class="combo">
            <div v-for="(h, i) in bars" :key="i" class="pair" :style="{ animationDelay: i * 50 + 'ms' }">
              <i :style="{ height: h + 'px', animationDelay: i * 50 + 'ms' }"></i>
              <b :style="{ height: Math.round(h * .72) + 'px', animationDelay: i * 50 + 100 + 'ms' }"></b>
            </div>
            <svg viewBox="0 0 760 260" preserveAspectRatio="none">
              <defs>
                <linearGradient id="gradPrimary" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stop-color="#10BFA3" />
                  <stop offset="100%" stop-color="#31D18B" />
                </linearGradient>
                <linearGradient id="gradOrange" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stop-color="#FF9F43" />
                  <stop offset="100%" stop-color="#FBBF24" />
                </linearGradient>
              </defs>
              <path class="line-primary" d="M20 210 C76 182 120 190 164 146 S250 98 314 122 388 76 464 90 582 42 646 66 710 36 748 22"/>
              <path class="line-orange dash" d="M20 235 C80 224 122 210 172 188 S270 150 330 168 418 132 506 144 608 116 660 126 720 90 748 104"/>
            </svg>
        </div>
      </CardBox>
      <CardBox title="渠道贡献与转化" sub="按来源拆分">
        <div class="donut-wrap">
          <div class="donut"><span>总转化<br><b>27.6%</b></span></div>
          <div class="donut-legend">
            <span><i style="background:var(--primary)"></i>直营 38%</span>
            <span><i style="background:var(--success)"></i>线上 24%</span>
            <span><i style="background:var(--warning)"></i>代理 21%</span>
            <span><i style="background:var(--lime)"></i>渠道 17%</span>
          </div>
        </div>
      </CardBox>
    </div>
    <div class="grid three">
      <CardBox title="销售漏斗" sub="线索到成交"><ProgressList :items="funnel" /></CardBox>
      <CardBox title="重点城市排行" sub="Top 5"><ProgressList :items="cities" rank /></CardBox>
      <CardBox title="异常洞察" sub="自动识别">
        <div class="alert-list">
          <p><span class="tag warning">关注</span> 华东大客户续费放缓</p>
          <p><span class="tag danger">异常</span> 线上渠道退款率升高</p>
          <p><span class="tag">增长</span> 代理商转化效率提升</p>
          <p><span class="tag warning">库存</span> 智能硬件库存偏低</p>
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
.three { grid-template-columns: repeat(3, minmax(0, 1fr)); }

.combo {
  position: relative;
  height: 260px;
  display: flex;
  align-items: end;
  gap: 14px;
  padding: 30px 28px 0;
}
.pair {
  height: 210px;
  display: flex;
  align-items: end;
  gap: 3px;
  flex: 1;
  cursor: pointer;
  transition: all var(--transition-fast);
}
.pair:hover { transform: translateY(-4px); }
.pair:active { transform: translateY(-2px) scale(.98); }
.pair i, .pair b {
  display: block;
  width: 100%;
  border-radius: 6px 6px 0 0;
  transition: all var(--transition);
  animation: barRise 1s cubic-bezier(.4,0,.2,1) forwards;
  transform-origin: bottom;
  transform: scaleY(0);
}
@keyframes barRise { to { transform: scaleY(1); } }
.pair i { background: linear-gradient(180deg, rgba(16,191,163,.25), rgba(16,191,163,.08)); }
.pair b { background: linear-gradient(180deg, rgba(30,115,232,.25), rgba(30,115,232,.08)); }
.pair:hover i, .pair:hover b { filter: brightness(1.15); }
.combo svg { position: absolute; inset: 0; pointer-events: none; }

.line-primary { fill: none; stroke: url(#gradPrimary); stroke-width: 4; stroke-linecap: round; stroke-linejoin: round; }
.line-orange { fill: none; stroke: url(#gradOrange); stroke-width: 3.5; stroke-linecap: round; stroke-linejoin: round; }
.dash { stroke-dasharray: 8 7; }

.donut-wrap { text-align: center; padding: 8px 0; }
.donut {
  width: 180px; height: 180px;
  margin: 0 auto 16px;
  border-radius: 50%;
  display: grid; place-items: center;
  background: conic-gradient(var(--primary) 0 38%, var(--success) 38% 62%, var(--warning) 62% 83%, var(--lime) 83% 100%);
  box-shadow: 0 8px 30px rgba(79,70,229,.15);
  position: relative;
  animation: donutSpin 1.2s cubic-bezier(.4,0,.2,1);
  cursor: pointer;
  user-select: none;
  transition: all var(--transition-fast);
}
.donut:hover { transform: scale(1.05); box-shadow: 0 12px 40px rgba(79,70,229,.25); }
.donut:active { transform: scale(1.02); }
@keyframes donutSpin { from { transform: rotate(-90deg) scale(.8); opacity:0; } to { transform: rotate(0) scale(1); opacity:1; } }
.donut span {
  width: 110px; height: 110px;
  display: grid; place-items: center; text-align: center;
  border-radius: 50%;
  background: #fff;
  color: var(--muted);
  font-weight: 700; font-size: 11px; line-height: 1.4;
  box-shadow: inset 0 2px 8px rgba(0,0,0,.04);
}
.donut b { color: var(--primary); font-size: 24px; font-weight: 800; display: block; letter-spacing: -1px; }
.donut-legend { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 16px; margin-top: 8px; }
.donut-legend span {
  display: flex; align-items: center; gap: 8px;
  font-size: 12.5px; color: var(--ink-secondary); font-weight: 600;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
  user-select: none;
}
.donut-legend span:hover { background: #f8fafc; }
.donut-legend span:active { transform: scale(.97); }
.donut-legend i { width: 10px; height: 10px; border-radius: 3px; flex-shrink: 0; }

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

@media (max-width: 1280px) {
  .metrics, .three { grid-template-columns: repeat(2, 1fr); }
  .two, .wide { grid-template-columns: 1fr; }
}
</style>
