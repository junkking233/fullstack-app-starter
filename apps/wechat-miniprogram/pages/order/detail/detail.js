const { getOrderById } = require('../../../services/order');
const { formatPrice } = require('../../../utils/format');

Page({
  data: {
    order: null,
    totalAmountText: '0.00',
    payAmountText: '0.00'
  },

  onLoad(options) {
    const order = getOrderById(options.id);
    if (!order) {
      return;
    }
    this.setData({
      order: {
        ...order,
        items: order.items.map((item) => ({
          ...item,
          amountText: formatPrice(item.product.price * item.quantity)
        }))
      },
      totalAmountText: formatPrice(order.totalAmount),
      payAmountText: formatPrice(order.payAmount)
    });
  }
});
