<script setup>
import CardBox from "../components/CardBox.vue";

const customers = [
  ["C-10291", "杭州科启信息有限公司", "企业", "王明", "今天 10:24", "正常"],
  ["C-10288", "上海远拓科技", "企业", "张琳", "昨天 18:03", "待跟进"],
  ["C-10271", "陈晓峰", "个人", "赵一", "2 天前", "正常"],
  ["C-10264", "苏州云径贸易", "企业", "李晴", "3 天前", "异常"],
  ["C-10253", "宁波海越服务", "企业", "王明", "5 天前", "冻结"],
  ["C-10249", "周宁", "个人", "张琳", "5 天前", "正常"],
  ["C-10237", "厦门诚佑供应链", "企业", "赵一", "6 天前", "待跟进"],
];

const stats = [
  ["总客户", "24,890"],
  ["本周新增", "1,206"],
  ["待跟进", "538"],
  ["异常数据", "42"],
  ["转化率", "18.7%"],
];

function tagClass(value) {
  if (["异常", "高"].includes(value)) return "danger";
  if (["待跟进", "中"].includes(value)) return "warning";
  if (value === "冻结") return "info";
  return "";
}
</script>

<template>
  <div class="grid split">
    <CardBox title="数据目录" sub="12 组">
      <div class="tree">
        <div class="active">📁 全部客户</div>
        <div>🏢 企业客户</div>
        <div class="child">⭐ 重点客户</div>
        <div class="child">🏢 普通企业</div>
        <div>👤 个人客户</div>
        <div class="child">🔥 高活跃用户</div>
        <div class="child">😴 沉默用户</div>
        <div>🧹 待清洗数据</div>
        <div>🚫 黑名单</div>
      </div>
    </CardBox>
    <CardBox title="客户数据管理" sub="分组筛选 / 批量操作">
      <div class="filters">
        <div class="input">🔍 输入名称 / 手机 / 编号</div>
        <div class="select">客户类型</div>
        <div class="select">状态</div>
        <button class="btn">重置</button>
        <button class="btn primary">查询</button>
      </div>
      <div class="stat-strip">
        <div v-for="s in stats" :key="s[0]">
          <span>{{ s[0] }}</span>
          <b>{{ s[1] }}</b>
        </div>
      </div>
      <div class="table-wrap">
        <table>
          <thead>
            <tr><th>客户编号</th><th>客户名称</th><th>类型</th><th>负责人</th><th>最近跟进</th><th>状态</th><th>操作</th></tr>
          </thead>
          <tbody>
            <tr v-for="r in customers" :key="r[0]">
              <td v-for="(c, i) in r" :key="i">
                <span v-if="i === 5" class="tag" :class="tagClass(c)">{{ c }}</span>
                <template v-else>{{ c }}</template>
              </td>
              <td style="color:var(--primary);font-weight:700;cursor:pointer">查看 编辑</td>
            </tr>
          </tbody>
        </table>
      </div>
    </CardBox>
  </div>
</template>

<style scoped>
.grid { display: grid; gap: 20px; }
.split { grid-template-columns: 280px minmax(0, 1fr); }

.tree div {
  height: 40px;
  display: flex; align-items: center;
  border-radius: var(--radius-sm);
  padding: 0 14px;
  font-size: 13px;
  font-weight: 500;
  color: var(--muted);
  cursor: pointer;
  transition: all var(--transition-fast);
  margin-bottom: 2px;
  user-select: none;
}
.tree div:hover {
  background: #f1f5f9;
  color: var(--ink-secondary);
  transform: translateX(3px);
}
.tree div:active { transform: translateX(3px) scale(.98); }
.tree .active {
  background: linear-gradient(135deg, var(--primary-soft), #d4f5e9);
  color: var(--primary);
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(79,70,229,.1);
}
.tree .child { padding-left: 32px; font-size: 12.5px; }

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
.input:focus-within, .select:focus-within {
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px var(--primary-soft);
  background: #fff;
  cursor: text;
}

.stat-strip {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}
.stat-strip > div {
  padding: 16px;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: #f8fafc;
  transition: all var(--transition-fast);
  cursor: pointer;
  user-select: none;
}
.stat-strip > div:hover {
  background: #fff;
  border-color: var(--primary-light);
  box-shadow: var(--shadow);
  transform: translateY(-3px);
}
.stat-strip > div:active { transform: translateY(-1px) scale(.98); }
.stat-strip span { display: block; color: var(--muted); font-size: 11.5px; font-weight: 600; }
.stat-strip b { display: block; margin-top: 6px; font-size: 20px; font-weight: 800; color: var(--ink); letter-spacing: -.5px; }

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

@media (max-width: 1280px) {
  .split { grid-template-columns: 1fr; }
}
</style>
