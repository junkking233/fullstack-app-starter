<script setup>
defineProps({
  title: { type: String, required: true },
});
</script>

<template>
  <div class="screen-panel">
    <h2>{{ title }}</h2>
    <slot />
  </div>
</template>

<style scoped>
.screen-panel {
  border: 1px solid rgba(99,102,241,.2);
  border-radius: var(--radius);
  background: rgba(15,23,42,.6);
  backdrop-filter: blur(12px);
  box-shadow: 0 8px 32px rgba(0,0,0,.3), inset 0 1px 0 rgba(255,255,255,.04);
  padding: 18px;
  transition: all var(--transition);
}
.screen-panel:hover {
  border-color: rgba(99,102,241,.35);
  box-shadow: 0 8px 32px rgba(0,0,0,.4), inset 0 1px 0 rgba(255,255,255,.06);
}
.screen-panel h2 {
  margin: 0 0 14px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(99,102,241,.15);
  font-size: 14px;
  font-weight: 700;
  color: #c7d2fe;
  letter-spacing: .5px;
}
</style>
