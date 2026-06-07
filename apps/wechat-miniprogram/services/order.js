const { clearCheckedItems } = require('./cart');
const { getAuthState } = require('../utils/auth');

const ORDERS_KEY = 'starter_orders';
const ADDRESS_KEY = 'starter_addresses';
const COUPON_KEY = 'starter_coupons';

function scopedKey(key) {
  const authState = getAuthState();
  const user = authState && authState.user;
  if (!user) {
    return `${key}_guest`;
  }
  return `${key}_${user.id || user.username}`;
}

function readOrders() {
  return wx.getStorageSync(scopedKey(ORDERS_KEY)) || [];
}

function writeOrders(orders) {
  wx.setStorageSync(scopedKey(ORDERS_KEY), orders);
}

function getOrders() {
  return readOrders();
}

function getOrderById(orderId) {
  return readOrders().find((item) => item.id === String(orderId));
}

function createOrder(payload) {
  const order = {
    id: `SO${Date.now()}`,
    status: '待支付',
    createdAt: new Date().toLocaleString(),
    ...payload
  };
  writeOrders([order, ...readOrders()]);
  clearCheckedItems();
  return order;
}

function getAddresses() {
  return wx.getStorageSync(scopedKey(ADDRESS_KEY)) || [];
}

function saveAddress(address) {
  const addresses = getAddresses();
  if (address.isDefault) {
    addresses.forEach((item) => {
      item.isDefault = false;
    });
  }
  if (address.id) {
    const index = addresses.findIndex((item) => item.id === Number(address.id));
    if (index >= 0) {
      addresses[index] = {
        ...addresses[index],
        ...address,
        id: Number(address.id)
      };
    }
  } else {
    addresses.push({
      ...address,
      id: Date.now()
    });
  }
  wx.setStorageSync(scopedKey(ADDRESS_KEY), addresses);
  return addresses;
}

function getCoupons() {
  return wx.getStorageSync(scopedKey(COUPON_KEY)) || [];
}

function ensureNewUserCoupons() {
  const saved = getCoupons();
  if (saved.length) {
    return saved;
  }
  const coupons = [
    { id: 1, name: '新人满39减10', threshold: 39, discount: 10, status: '可用' },
    { id: 2, name: '生鲜满99减20', threshold: 99, discount: 20, status: '可用' }
  ];
  wx.setStorageSync(scopedKey(COUPON_KEY), coupons);
  return coupons;
}

module.exports = {
  getOrders,
  getOrderById,
  createOrder,
  getAddresses,
  saveAddress,
  getCoupons,
  ensureNewUserCoupons
};
