const { getAuthState, clearAuthState } = require('./auth');

function request(options) {
  const app = getApp();
  const authState = getAuthState();
  const headers = Object.assign(
    {
      'content-type': 'application/json'
    },
    options.header || {}
  );

  if (authState && authState.token) {
    headers.Authorization = `Bearer ${authState.token}`;
  }

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${app.globalData.apiBaseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: headers,
      success(res) {
        if (res.statusCode === 401) {
          clearAuthState();
          reject(new Error('登录已失效，请重新登录'));
          return;
        }

        const body = res.data;
        if (body && typeof body === 'object' && Object.prototype.hasOwnProperty.call(body, 'code')) {
          if (body.code === 200) {
            resolve(body.data);
            return;
          }
          reject(new Error(body.message || '请求失败'));
          return;
        }

        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(body);
          return;
        }

        reject(new Error(`请求失败：${res.statusCode}`));
      },
      fail(err) {
        reject(new Error(err.errMsg || '网络请求失败'));
      }
    });
  });
}

module.exports = request;
