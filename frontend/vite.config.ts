import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 9999,
    proxy: {
      '/api/ai': {
        target: process.env.VITE_PROXY_AI || 'http://localhost:8000',
        changeOrigin: true,
      },
      '/api': {
        target: process.env.VITE_PROXY_BACKEND || 'http://localhost:8888',
        changeOrigin: true,
      },
    },
  },
});
