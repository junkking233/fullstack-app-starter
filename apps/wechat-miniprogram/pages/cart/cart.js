const { getCartItems, updateCartItem, getCartSummary } = require('../../services/cart');
const { formatPrice } = require('../../utils/format');
const { getAuthState } = require('../../utils/auth');

Page({
  data: {
    items: [],
    totalCount: 0,
    totalAmountText: '0.00'
  },

  onShow() {
    this.refresh();
  },

  refresh() {
    const items = getCartItems().map((item) => ({
      ...item,
      priceText: formatPrice(item.product.price)
    }));
    const summary = getCartSummary(items);
    this.setData({
      items,
      totalCount: summary.totalCount,
      totalAmountText: formatPrice(summary.totalAmount)
    });
  },

  toggleChecked(event) {
    const id = Number(event.currentTarget.dataset.id);
    const item = this.data.items.find((entry) => entry.id === id);
    updateCartItem(id, {
      checked: !item.checked
    });
    this.refresh();
  },

  increase(event) {
    const id = Number(event.currentTarget.dataset.id);
    const item = this.data.items.find((entry) => entry.id === id);
    updateCartItem(id, {
      quantity: item.quantity + 1
    });
    this.refresh();
  },

  decrease(event) {
    const id = Number(event.currentTarget.dataset.id);
    const item = this.data.items.find((entry) => entry.id === id);
    updateCartItem(id, {
      quantity: item.quantity - 1
    });
    this.refresh();
  },

  goHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  },

  goCheckout() {
    if (!getAuthState()) {
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return;
    }
    if (!this.data.totalCount) {
      wx.showToast({
        title: '请选择商品',
        icon: 'none'
      });
      return;
    }
    wx.navigateTo({
      url: '/pages/order/checkout/checkout'
    });
  }
});
