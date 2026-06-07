const { getOrders } = require('../../../services/order');
const { formatPrice } = require('../../../utils/format');

Page({
  data: {
    orders: []
  },

  onShow() {
    this.setData({
      orders: getOrders().map((item) => ({
        ...item,
        payAmountText: formatPrice(item.payAmount)
      }))
    });
  },

  goDetail(event) {
    wx.navigateTo({
      url: `/pages/order/detail/detail?id=${event.currentTarget.dataset.id}`
    });
  },

  goHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
});
