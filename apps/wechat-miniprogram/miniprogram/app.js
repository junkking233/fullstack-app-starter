App({
  globalData: {
    apiBaseUrl: 'http://localhost:8888/api'
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
    }
  }
});
