const { getCoupons } = require('../../../services/order');
const { formatPrice } = require('../../../utils/format');

Page({
  data: {
    coupons: []
  },

  onLoad() {
    this.setData({
      coupons: getCoupons().map((item) => ({
        ...item,
        discountText: formatPrice(item.discount)
      }))
    });
  }
});
