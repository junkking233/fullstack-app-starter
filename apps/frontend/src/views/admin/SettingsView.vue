<script setup>
import MetricCard from "./components/MetricCard.vue";
import CardBox from "./components/CardBox.vue";

const metrics = [
  { label: "角色数量", value: "12", trend: "启用 10 个", tone: "" },
  { label: "成员账号", value: "86", trend: "本月新增 6 个", tone: "" },
  { label: "待处理权限", value: "9", trend: "需管理员确认", tone: "warning" },
];

const fields = [
  ["角色名称", "平台超级管理员"],
  ["数据范围", "全部部门、全部业务线、全部客户分组"],
  ["菜单权限", "运营概览、数据管理、可视化列表、数据可视化、流程工单、系统设置"],
  ["敏感操作", "允许导出、删除、批量修改与权限分配"],
  ["登录安全", "启用短信二次验证，异地登录自动提醒"],
];
</script>

<template>
  <div class="grid split">
    <CardBox title="设置菜单" sub="">
      <div class="settings-menu">
        <div class="active">🔐 角色权限</div>
        <div>🔒 账号安全</div>
        <div>📖 数据字典</div>
        <div>🔔 通知策略</div>
        <div>🔑 接口密钥</div>
        <div>📋 审计日志</div>
      </div>
    </CardBox>
    <CardBox title="管理员角色配置" sub="最近保存：今天 09:18">
      <div class="metrics-row">
        <MetricCard v-for="item in metrics" :key="item.label" :item="item" />
      </div>
      <div v-for="f in fields" :key="f[0]" class="form-row">
        <b>{{ f[0] }}</b>
        <div class="field">{{ f[1] }}</div>
      </div>
    </CardBox>
  </div>
</template>

<style scoped>
.grid { display: grid; gap: 20px; }
.split { grid-template-columns: 280px minmax(0, 1fr); }

.settings-menu div {
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
.settings-menu div:hover {
  background: #f1f5f9;
  color: var(--ink-secondary);
  transform: translateX(3px);
}
.settings-menu div:active { transform: translateX(3px) scale(.98); }
.settings-menu .active {
  background: linear-gradient(135deg, var(--primary-soft), #d4f5e9);
  color: var(--primary);
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(79,70,229,.1);
}

.metrics-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 20px;
  align-items: center;
  min-height: 56px;
  border-bottom: 1px solid #f1f5f9;
  font-size: 13px;
  padding: 0 4px;
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}
.form-row:hover { background: #f8fafc; border-radius: var(--radius-sm); }
.form-row:active { transform: scale(.995); }
.form-row:last-child { border-bottom: none; }
.form-row b { color: var(--muted); font-weight: 600; }
.field {
  min-height: 40px;
  display: flex; align-items: center;
  padding: 0 14px;
  border: 1px solid var(--line);
  border-radius: var(--radius-sm);
  background: #f8fafc;
  color: var(--ink-secondary);
  font-weight: 500;
  transition: all var(--transition-fast);
  cursor: pointer;
}
.field:hover { border-color: var(--line-strong); background: #fff; }
.field:active { transform: scale(.98); }

@media (max-width: 1280px) {
  .split { grid-template-columns: 1fr; }
}
</style>
