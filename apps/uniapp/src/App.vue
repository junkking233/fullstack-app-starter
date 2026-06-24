<script setup lang="ts">
import { onLaunch } from '@dcloudio/uni-app'
import { getAuthState, clearAuthState } from './utils/auth'

onLaunch(() => {
  const authState = getAuthState()
  if (!authState || !authState.token) {
    clearAuthState()
  }
})
</script>

<style>
page {
  background-color: #f5f7fb;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>
