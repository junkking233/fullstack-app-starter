const { getCartItems, getCartSummary } = require('../../../services/cart');
const { getAddresses, getCoupons, createOrder } = require('../../../services/order');
const { formatPrice } = require('../../../utils/format');

Page({
  data: {
    items: [],
    address: null,
    addressText: '请选择收货地址',
    deliveryType: 'HOME',
    coupon: null,
    totalAmount: 0,
    totalAmountText: '0.00',
    payAmountText: '0.00'
  },

  onLoad() {
    const items = getCartItems()
      .filter((item) => item.checked !== false)
      .map((item) => ({
        ...item,
        amountText: formatPrice(item.product.price * item.quantity)
      }));
    const address = getAddresses()[0] || null;
    this.setData({
      items,
      address,
      addressText: address ? `${address.region} ${address.detail}` : '请选择收货地址'
    });
    this.recalculate();
  },

  recalculate() {
    const summary = getCartSummary(this.data.items);
    const discount = this.data.coupon ? this.data.coupon.discount : 0;
    this.setData({
      totalAmount: summary.totalAmount,
      totalAmountText: formatPrice(summary.totalAmount),
      payAmountText: formatPrice(Math.max(summary.totalAmount - discount, 0))
    });
  },

  setDelivery(event) {
    this.setData({
      deliveryType: event.currentTarget.dataset.type
    });
  },

  selectAddress() {
    wx.navigateTo({
      url: '/pages/address/list/list?select=1'
    });
  },

  selectCoupon() {
    const coupon = getCoupons().find((item) => this.data.totalAmount >= item.threshold);
    if (!coupon) {
      wx.showToast({
        title: '暂无可用优惠券',
        icon: 'none'
      });
      return;
    }
    this.setData({
      coupon: {
        ...coupon,
        discountText: formatPrice(coupon.discount)
      }
    });
    this.recalculate();
  },

  submitOrder() {
    if (!this.data.address) {
      wx.showToast({
        title: '请选择收货地址',
        icon: 'none'
      });
      return;
    }
    if (!this.data.items.length) {
      wx.showToast({
        title: '请选择商品',
        icon: 'none'
      });
      return;
    }

    const order = createOrder({
      address: this.data.address,
      deliveryType: this.data.deliveryType,
      deliveryTime: '今天 18:00-19:00',
      coupon: this.data.coupon,
      items: this.data.items,
      totalAmount: this.data.totalAmount,
      payAmount: Number(this.data.payAmountText)
    });

    wx.showToast({
      title: '下单成功',
      icon: 'success'
    });
    setTimeout(() => {
      wx.redirectTo({
        url: `/pages/order/detail/detail?id=${order.id}`
      });
    }, 600);
  }
});
