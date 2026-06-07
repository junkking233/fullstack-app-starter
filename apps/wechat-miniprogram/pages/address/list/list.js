const { getAddresses } = require('../../../services/order');

Page({
  data: {
    addresses: []
  },

  onShow() {
    this.setData({
      addresses: getAddresses()
    });
  },

  goEdit() {
    wx.navigateTo({
      url: '/pages/address/edit/edit'
    });
  }
});
