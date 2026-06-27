<script setup>
import CardBox from "../components/CardBox.vue";
import MiniLine from "../components/MiniLine.vue";

const products = [
  ["01", "智能温控套装 Pro", "智能硬件", "¥893,420", "9.8%", 86, "低"],
  ["02", "企业数据网关 V3", "软件服务", "¥758,900", "7.2%", 72, "低"],
  ["03", "商户收银终端 S2", "智能硬件", "¥609,310", "5.4%", 48, "中"],
  ["04", "行业报表分析包", "软件服务", "¥436,210", "4.9%", 69, "低"],
  ["05", "移动巡检终端", "行业设备", "¥382,600", "2.8%", 34, "高"],
  ["06", "会员营销插件", "增值服务", "¥298,470", "6.1%", 76, "低"],
  ["07", "门店协同看板", "软件服务", "¥267,120", "5.7%", 81, "低"],
];

function tagClass(value) {
  if (["异常", "高"].includes(value)) return "danger";
  if (["待跟进", "中"].includes(value)) return "warning";
  if (value === "冻结") return "info";
  return "";
}
</script>

<template>
  <CardBox title="商品表现可视化列表" sub="排名 / 趋势 / 健康度">
    <div class="filters">
      <div class="input">🔍 搜索商品 / 类目</div>
      <div class="select">全部类目</div>
      <button class="btn">列配置</button>
      <button class="btn primary">生成报表</button>
    </div>
    <div class="table-wrap">
      <table>
        <thead>
          <tr><th>排名</th><th>商品</th><th>类目</th><th>销售额</th><th>访问趋势</th><th>转化率</th><th>库存健康</th><th>风险</th></tr>
        </thead>
        <tbody>
          <tr v-for="r in products" :key="r[0]">
            <td style="color:var(--muted);font-weight:700">#{{ r[0] }}</td>
            <td style="font-weight:600">{{ r[1] }}</td>
            <td><span class="tag info">{{ r[2] }}</span></td>
            <td style="font-weight:700">{{ r[3] }}</td>
            <td><MiniLine :risk="r[6]" /></td>
            <td style="font-weight:700">{{ r[4] }}</td>
            <td>
              <div class="bar">
                <i :style="{ width: r[5] + '%', '--bar-color': r[5] < 45 ? 'var(--warning)' : r[5] > 80 ? 'var(--success)' : 'var(--primary)' }"></i>
              </div>
            </td>
            <td><span class="tag" :class="tagClass(r[6])">{{ r[6] }}</span></td>
          </tr>
        </tbody>
      </table>
    </div>
  </CardBox>
</template>

<style scoped>
.filters { display: flex; gap: 10px; margin-bottom: 18px; flex-wrap: wrap; }
.input, .select {
  height: 38px;
  display: flex; align-items: center;
  border: 1px solid var(--line);
  border-radius: var(--radius-sm);
  padding: 0 14px;
  background: #f8fafc;
  color: var(--muted);
  font-size: 13px;
  transition: all var(--transition-fast);
  min-width: 140px;
  cursor: pointer;
  user-select: none;
}
.input:hover, .select:hover {
  border-color: var(--primary-light);
  background: #fff;
}
.input:active, .select:active { transform: scale(.98); }
.table-wrap {
  border: 1px solid var(--line);
  border-radius: var(--radius);
  overflow: hidden;
  background: var(--surface);
}
table { width: 100%; border-collapse: collapse; font-size: 13px; }
th {
  height: 44px;
  padding: 0 16px;
  text-align: left;
  background: #f8fafc;
  color: var(--muted);
  font-size: 11.5px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .5px;
  border-bottom: 1px solid var(--line);
}
td {
  height: 56px;
  padding: 0 16px;
  border-bottom: 1px solid #f1f5f9;
  color: var(--ink-secondary);
  transition: background var(--transition-fast);
}
tr {
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}
tr:hover td { background: #f8fafc; }
tr:active { transform: scale(.995); }
tr:last-child td { border-bottom: none; }

.bar {
  height: 10px;
  border-radius: 999px;
  background: #f1f5f9;
  overflow: hidden;
  position: relative;
  cursor: pointer;
}
.bar:hover { filter: brightness(.95); }
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
}
.tag.warning { background: var(--warning-soft); color: var(--warning); border-color: rgba(245,158,11,.15); }
.tag.danger { background: var(--danger-soft); color: var(--danger); border-color: rgba(244,63,94,.15); }
.tag.info { background: var(--info-soft); color: var(--info); border-color: rgba(14,165,233,.15); }

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
</style>
