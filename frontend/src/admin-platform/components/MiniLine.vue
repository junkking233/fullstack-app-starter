<script setup>
const props = defineProps({
  risk: { type: String, default: "低" },
});

const paths = {
  "高": "M2 10 C22 14 40 16 56 20 S82 25 104 27 128 28 148 30",
  "中": "M2 12 C22 20 38 22 52 17 S82 28 104 20 126 9 148 15",
  "低": "M2 26 C22 8 36 18 52 13 S82 4 104 14 126 22 148 7",
};

const cls = props.risk === "高" ? "down" : props.risk === "中" ? "mid" : "up";
const d = paths[props.risk] || paths["低"];
</script>

<template>
  <svg class="mini-line" :class="cls" viewBox="0 0 150 34">
    <path :d="d" />
  </svg>
</template>

<style scoped>
.mini-line { width: 120px; height: 32px; }
.mini-line path { fill: none; stroke-width: 2.5; stroke-linecap: round; stroke-linejoin: round; }
.mini-line.up path { stroke: var(--success); }
.mini-line.down path { stroke: var(--danger); }
.mini-line.mid path { stroke: var(--warning); }
</style>
