<script setup>
defineProps({
  title: { type: String, required: true },
  sub: { type: String, default: "" },
});
</script>

<template>
  <section class="card-box">
    <div class="head">
      <h2>{{ title }}</h2>
      <span v-if="sub">{{ sub }}</span>
    </div>
    <slot />
  </section>
</template>

<style scoped>
.card-box {
  background: var(--surface-elevated);
  border: 1px solid var(--line);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
  transition: all var(--transition);
  padding: 24px;
  cursor: pointer;
  user-select: none;
}
.card-box:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-3px);
  border-color: var(--line-strong);
}
.card-box:active { transform: translateY(-1px) scale(.99); }
.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.head h2 { margin: 0; font-size: 15px; font-weight: 700; color: var(--ink); letter-spacing: -.2px; }
.head span { color: var(--muted-light); font-size: 12px; font-weight: 500; }
</style>
