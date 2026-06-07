const request = require('./request');

function login(data) {
  return request({
    url: '/auth/login',
    method: 'POST',
    data
  });
}

function getCurrentUser() {
  return request({
    url: '/auth/me'
  });
}

module.exports = {
  login,
  getCurrentUser
};
