const { getProductById } = require('../../../services/catalog');
const { addCartItem } = require('../../../services/cart');
const { formatPrice } = require('../../../utils/format');
const { getAuthState } = require('../../../utils/auth');

Page({
  data: {
    product: {},
    priceText: '0.00'
  },

  onLoad(options) {
    const product = getProductById(options.id);
    this.setData({
      product,
      priceText: formatPrice(product.price)
    });
  },

  ensureLogin() {
    if (getAuthState()) {
      return true;
    }
    wx.navigateTo({
      url: '/pages/login/login'
    });
    return false;
  },

  onAddCart() {
    if (!this.ensureLogin()) {
      return;
    }
    addCartItem(this.data.product.id);
    wx.showToast({
      title: '已加入购物车',
      icon: 'success'
    });
  },

  onBuyNow() {
    if (!this.ensureLogin()) {
      return;
    }
    addCartItem(this.data.product.id);
    wx.switchTab({
      url: '/pages/cart/cart'
    });
  }
});
