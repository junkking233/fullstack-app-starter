const { getAuthState, clearAuthState } = require('./utils/auth');

App({
  globalData: {
    apiBaseUrl: 'http://localhost:8888/api',
    user: null,
    categoryIntent: null
  },

  onLaunch() {
    const authState = getAuthState();
    if (authState && authState.user) {
      this.globalData.user = authState.user;
    } else {
      clearAuthState();
    }
  }
});
