const Toast = require('@vant/weapp/toast/toast');
const { setAuthState } = require('../../utils/auth');
const { ensureNewUserCoupons } = require('../../services/order');

Page({
  data: {
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    loading: false
  },

  onUsernameChange(event) {
    this.setData({
      username: event.detail
    });
  },

  onEmailChange(event) {
    this.setData({
      email: event.detail
    });
  },

  onPasswordChange(event) {
    this.setData({
      password: event.detail
    });
  },

  onConfirmPasswordChange(event) {
    this.setData({
      confirmPassword: event.detail
    });
  },

  onSubmit() {
    if (this.data.loading) {
      return;
    }

    const username = this.data.username.trim();
    const email = this.data.email.trim();
    const password = this.data.password.trim();
    const confirmPassword = this.data.confirmPassword.trim();

    if (!username || !password || !confirmPassword) {
      Toast('请填写账号和密码');
      return;
    }

    if (password !== confirmPassword) {
      Toast('两次密码不一致');
      return;
    }

    this.setData({
      loading: true
    });

    const user = {
      id: Date.now(),
      username,
      email,
      role: 'USER',
      status: 1
    };

    setAuthState({
      token: `local-${Date.now()}`,
      user,
      expiresAt: Date.now() + 7 * 24 * 60 * 60 * 1000
    });
    ensureNewUserCoupons();
    getApp().globalData.user = user;

    wx.showToast({
      title: '注册成功',
      icon: 'success'
    });
    setTimeout(() => {
      wx.switchTab({
        url: '/pages/index/index'
      });
    }, 500);
  },

  goLogin() {
    wx.navigateBack();
  }
});
