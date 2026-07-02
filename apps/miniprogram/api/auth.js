const { post } = require('./request');

function login(data) {
  return post('/auth/login', data);
}

module.exports = {
  login
};
