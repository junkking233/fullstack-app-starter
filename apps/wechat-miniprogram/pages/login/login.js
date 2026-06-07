const Toast = require('@vant/weapp/toast/toast');
const { login } = require('../../utils/api');
const { setAuthState } = require('../../utils/auth');

Page({
  data: {
    username: 'admin',
    password: 'admin123',
    loading: false
  },

  onUsernameChange(event) {
    this.setData({
      username: event.detail
    });
  },

  onPasswordChange(event) {
    this.setData({
      password: event.detail
    });
  },

  async onSubmit() {
    if (this.data.loading) {
      return;
    }

    const username = this.data.username.trim();
    const password = this.data.password.trim();

    if (!username || !password) {
      Toast('请输入账号和密码');
      return;
    }

    this.setData({
      loading: true
    });

    try {
      const data = await login({
        username,
        password
      });
      setAuthState(data);
      getApp().globalData.user = data.user;
      wx.switchTab({
        url: '/pages/index/index'
      });
    } catch (error) {
      Toast(error.message || '登录失败');
    } finally {
      this.setData({
        loading: false
      });
    }
  },

  goRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  }
});
