const BASE_URL = 'http://localhost:8888/api';

function buildHeader(extraHeader = {}) {
  const token = wx.getStorageSync('token');
  const header = {
    'Content-Type': 'application/json',
    ...extraHeader
  };
  if (token) {
    header.Authorization = `Bearer ${token}`;
  }
  return header;
}

function request(options) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: buildHeader(options.header),
      success(res) {
        const body = res.data || {};
        if (body.code === 200) {
          resolve(body.data);
          return;
        }

        if (body.code === 401) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('user');
          wx.navigateTo({ url: '/pages/login/login' });
        }

        reject(new Error(body.message || '请求失败'));
      },
      fail(err) {
        reject(err);
      }
    });
  });
}

function get(url, data) {
  return request({ url, method: 'GET', data });
}

function post(url, data) {
  return request({ url, method: 'POST', data });
}

module.exports = {
  request,
  get,
  post
};
