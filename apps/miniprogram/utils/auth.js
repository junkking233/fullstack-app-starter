function getUser() {
  return wx.getStorageSync('user') || null;
}

function isLoggedIn() {
  return Boolean(wx.getStorageSync('token'));
}

function setAuthState(token, user) {
  wx.setStorageSync('token', token);
  wx.setStorageSync('user', user);
}

function clearAuthState() {
  wx.removeStorageSync('token');
  wx.removeStorageSync('user');
}

module.exports = {
  getUser,
  isLoggedIn,
  setAuthState,
  clearAuthState
};
