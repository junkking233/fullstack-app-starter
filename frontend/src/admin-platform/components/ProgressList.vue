<script setup>
defineProps({
  items: { type: Array, required: true },
  rank: { type: Boolean, default: false },
  dark: { type: Boolean, default: false },
});
</script>

<template>
  <div class="progress-list">
    <div v-for="(it, i) in items" :key="i" class="progress-row" :class="{ dark }">
      <span>{{ rank ? (i + 1) + '. ' + (it.name || it.label) : (it.label || it.name) }}</span>
      <div class="bar"><i :style="{ width: (it.percent || it.value) + '%', '--bar-color': it.color || 'var(--primary)' }"></i></div>
      <b>{{ rank ? (it.percent || it.value) + '%' : (it.value || it.percent) }}</b>
    </div>
  </div>
</template>

<style scoped>
.progress-list { display: grid; gap: 16px; }
.progress-row {
  display: grid;
  grid-template-columns: 100px 1fr 56px;
  gap: 14px;
  align-items: center;
  font-size: 13px;
  padding: 5px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
}
.progress-row:hover { background: rgba(0,0,0,.03); }
.progress-row.dark:hover { background: rgba(255,255,255,.05); }
.progress-row:active { transform: scale(.99); }
.progress-row > span:first-child { color: var(--ink-secondary); font-weight: 600; }
.progress-row > b { text-align: right; color: var(--muted); font-weight: 700; }
.progress-row.dark > span:first-child { color: #cbd5e1; }
.progress-row.dark > b { color: #94a3b8; }

.bar {
  height: 10px;
  border-radius: 999px;
  background: #f1f5f9;
  overflow: hidden;
  position: relative;
}
.dark .bar { background: rgba(255,255,255,.06); }
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
.dark .bar i { background: linear-gradient(90deg, #10BFA3, #31D18B); }
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
</style>
