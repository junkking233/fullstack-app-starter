const TOKEN_KEY = 'starter_token';
const USER_KEY = 'starter_user';
const EXPIRES_AT_KEY = 'starter_expires_at';

function getAuthState() {
  const token = wx.getStorageSync(TOKEN_KEY);
  const user = wx.getStorageSync(USER_KEY);
  const expiresAt = wx.getStorageSync(EXPIRES_AT_KEY);

  if (!token || !user) {
    return null;
  }

  if (expiresAt && Number(expiresAt) <= Date.now()) {
    clearAuthState();
    return null;
  }

  return {
    token,
    user,
    expiresAt
  };
}

function setAuthState(data) {
  wx.setStorageSync(TOKEN_KEY, data.token);
  wx.setStorageSync(USER_KEY, data.user);
  wx.setStorageSync(EXPIRES_AT_KEY, data.expiresAt || 0);
}

function clearAuthState() {
  wx.removeStorageSync(TOKEN_KEY);
  wx.removeStorageSync(USER_KEY);
  wx.removeStorageSync(EXPIRES_AT_KEY);
}

module.exports = {
  TOKEN_KEY,
  USER_KEY,
  EXPIRES_AT_KEY,
  getAuthState,
  setAuthState,
  clearAuthState
};
