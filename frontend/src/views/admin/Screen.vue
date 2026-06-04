<script setup lang="ts">
import { useRouter } from 'vue-router';
import DataScreenView from '@/admin-platform/views/DataScreenView.vue';

const router = useRouter();
</script>

<template>
  <DataScreenView @back="router.push('/admin/dashboard')" />
</template>
