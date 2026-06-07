const { getProductById } = require('./catalog');

const CART_KEY = 'starter_cart_items';

function readCart() {
  return wx.getStorageSync(CART_KEY) || [];
}

function writeCart(items) {
  wx.setStorageSync(CART_KEY, items);
}

function getCartItems() {
  return readCart().map((item) => ({
    ...item,
    product: getProductById(item.productId)
  }));
}

function addCartItem(productId, quantity = 1) {
  const items = readCart();
  const existed = items.find((item) => item.productId === Number(productId));
  if (existed) {
    existed.quantity += quantity;
  } else {
    items.push({
      id: Date.now(),
      productId: Number(productId),
      quantity,
      checked: true
    });
  }
  writeCart(items);
  return getCartItems();
}

function updateCartItem(id, patch) {
  const items = readCart().map((item) => (item.id === Number(id) ? { ...item, ...patch } : item));
  writeCart(items.filter((item) => item.quantity > 0));
  return getCartItems();
}

function removeCartItem(id) {
  writeCart(readCart().filter((item) => item.id !== Number(id)));
  return getCartItems();
}

function clearCheckedItems() {
  writeCart(readCart().filter((item) => item.checked === false));
}

function getCartSummary(items = getCartItems()) {
  return items.reduce(
    (summary, item) => {
      if (item.checked !== false) {
        summary.totalCount += item.quantity;
        summary.totalAmount += item.product.price * item.quantity;
      }
      return summary;
    },
    {
      totalCount: 0,
      totalAmount: 0
    }
  );
}

module.exports = {
  getCartItems,
  addCartItem,
  updateCartItem,
  removeCartItem,
  clearCheckedItems,
  getCartSummary
};
