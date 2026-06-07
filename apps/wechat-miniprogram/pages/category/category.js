const { getCategories, getProductsByCategory } = require('../../services/catalog');
const { addCartItem } = require('../../services/cart');
const { formatPrice } = require('../../utils/format');
const { getAuthState } = require('../../utils/auth');

Page({
  data: {
    categories: [],
    currentCatId: 0,
    currentCatName: '',
    products: []
  },

  onLoad(options) {
    const categories = getCategories();
    const current = categories.find((item) => item.id === Number(options.id)) || categories[0];
    this.setData({
      categories,
      currentCatId: current.id,
      currentCatName: current.name
    });
    this.loadProducts(options.keyword || '');
  },

  onShow() {
    const intent = getApp().globalData.categoryIntent;
    if (!this.data.categories.length) {
      const categories = getCategories();
      const current = categories.find((item) => item.id === Number(intent && intent.id)) || categories[0];
      this.setData({
        categories,
        currentCatId: current.id,
        currentCatName: current.name
      });
      this.loadProducts(intent && intent.keyword);
      getApp().globalData.categoryIntent = null;
      return;
    }

    if (intent) {
      const current = this.data.categories.find((item) => item.id === Number(intent.id)) || this.data.categories[0];
      this.setData({
        currentCatId: current.id,
        currentCatName: current.name
      });
      this.loadProducts(intent.keyword);
      getApp().globalData.categoryIntent = null;
    }
  },

  loadProducts(keyword) {
    const products = getProductsByCategory(this.data.currentCatId, keyword).map((item) => ({
      ...item,
      priceText: formatPrice(item.price)
    }));
    this.setData({
      products
    });
  },

  selectCategory(event) {
    const id = Number(event.currentTarget.dataset.id);
    const current = this.data.categories.find((item) => item.id === id);
    this.setData({
      currentCatId: id,
      currentCatName: current.name
    });
    this.loadProducts();
  },

  goDetail(event) {
    wx.navigateTo({
      url: `/pages/product/detail/detail?id=${event.currentTarget.dataset.id}`
    });
  },

  onAdd(event) {
    if (!getAuthState()) {
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return;
    }
    addCartItem(event.currentTarget.dataset.id);
    wx.showToast({
      title: '已加入购物车',
      icon: 'success'
    });
  }
});
