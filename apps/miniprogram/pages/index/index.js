const Toast = require('@vant/weapp/toast/toast');
const { isLoggedIn, getUser } = require('../../utils/auth');

Page({
  data: {
    loggedIn: false,
    loginLabel: '登录后可访问完整业务能力'
  },

  onShow() {
    const loggedIn = isLoggedIn();
    const user = getUser();
    this.setData({
      loggedIn,
      loginLabel: loggedIn && user ? `${user.nickname || user.username}，欢迎回来` : '登录后可访问完整业务能力'
    });
  },

  handleLoginEntry() {
    if (this.data.loggedIn) {
      Toast.success('当前已登录');
      return;
    }
    wx.navigateTo({ url: '/pages/login/login' });
  }
});
