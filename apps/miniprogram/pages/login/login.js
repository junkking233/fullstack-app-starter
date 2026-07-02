const Toast = require('@vant/weapp/toast/toast');
const { login } = require('../../api/auth');
const { setAuthState } = require('../../utils/auth');

Page({
  data: {
    form: {
      username: '',
      password: ''
    },
    loading: false
  },

  onUsernameChange(event) {
    this.setData({
      'form.username': event.detail
    });
  },

  onPasswordChange(event) {
    this.setData({
      'form.password': event.detail
    });
  },

  async handleLogin() {
    if (this.data.loading) {
      return;
    }

    const { username, password } = this.data.form;
    if (!username || !password) {
      Toast.fail('请输入用户名和密码');
      return;
    }

    this.setData({ loading: true });
    Toast.loading({ message: '登录中...', forbidClick: true, duration: 0 });

    try {
      const result = await login({ username, password });
      setAuthState(result.token, result.user);
      Toast.success('登录成功');
      setTimeout(() => {
        if (getCurrentPages().length > 1) {
          wx.navigateBack({ delta: 1 });
        } else {
          wx.reLaunch({ url: '/pages/index/index' });
        }
      }, 800);
    } catch (error) {
      Toast.fail(error.message || '登录失败');
    } finally {
      this.setData({ loading: false });
    }
  }
});
