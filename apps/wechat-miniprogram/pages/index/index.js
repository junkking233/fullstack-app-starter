const { getHomeData } = require('../../services/catalog');
const { addCartItem } = require('../../services/cart');
const { formatPrice } = require('../../utils/format');
const { getAuthState } = require('../../utils/auth');

function normalizeProduct(product) {
  return {
    ...product,
    priceText: formatPrice(product.price),
    originalPriceText: formatPrice(product.originalPrice)
  };
}

Page({
  data: {
    keyword: '',
    banners: [],
    categories: [],
    hotProducts: [],
    seckillProducts: [],
    newProducts: []
  },

  onLoad() {
    const data = getHomeData();
    this.setData({
      banners: data.banners,
      categories: data.categories,
      hotProducts: data.hotProducts.map(normalizeProduct),
      seckillProducts: data.seckillProducts.map(normalizeProduct),
      newProducts: data.newProducts.map(normalizeProduct)
    });
  },

  onInput(event) {
    this.setData({
      keyword: event.detail.value
    });
  },

  doSearch() {
    const keyword = this.data.keyword.trim();
    getApp().globalData.categoryIntent = {
      keyword
    };
    wx.switchTab({
      url: '/pages/category/category'
    });
  },

  goCategory(event) {
    getApp().globalData.categoryIntent = {
      id: Number(event.currentTarget.dataset.id)
    };
    wx.switchTab({
      url: '/pages/category/category'
    });
  },

  goCategoryList() {
    wx.switchTab({
      url: '/pages/category/category'
    });
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
