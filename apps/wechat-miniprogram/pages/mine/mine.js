const { getAuthState, clearAuthState } = require('../../utils/auth');
const { getOrders, getAddresses, getCoupons } = require('../../services/order');

const roleMap = {
  ADMIN: '管理员',
  PARTNER: '服务方',
  USER: '鲜享会员'
};

Page({
  data: {
    isLoggedIn: false,
    user: {},
    avatarText: '鲜',
    roleText: '鲜享会员',
    orderCount: 0,
    couponCount: 0,
    points: 0,
    addressCount: 0
  },

  onShow() {
    const authState = getAuthState();
    const user = authState ? authState.user : {};
    const orders = authState ? getOrders() : [];
    const coupons = authState ? getCoupons() : [];
    const addresses = authState ? getAddresses() : [];
    this.setData({
      isLoggedIn: Boolean(authState),
      user,
      avatarText: user.username ? user.username.slice(0, 1).toUpperCase() : '鲜',
      roleText: roleMap[user.role] || user.role || '鲜享会员',
      orderCount: orders.length,
      couponCount: coupons.length,
      points: orders.reduce((sum, order) => sum + Math.floor(Number(order.payAmount || 0)), 0),
      addressCount: addresses.length
    });
  },

  goLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  onLogout() {
    wx.showModal({
      title: '提示',
      content: '确定退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          clearAuthState();
          getApp().globalData.user = null;
          this.onShow();
          wx.showToast({
            title: '已退出',
            icon: 'success'
          });
        }
      }
    });
  },

  goOrders() {
    wx.navigateTo({
      url: '/pages/order/list/list'
    });
  },

  goCoupons() {
    wx.navigateTo({
      url: '/pages/coupon/list/list'
    });
  },

  goAddresses() {
    wx.navigateTo({
      url: '/pages/address/list/list'
    });
  }
});
