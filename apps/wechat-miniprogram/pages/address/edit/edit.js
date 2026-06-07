const { saveAddress } = require('../../../services/order');

Page({
  data: {
    form: {
      name: '',
      phone: '',
      region: '',
      detail: '',
      isDefault: false
    }
  },

  onInput(event) {
    const key = event.currentTarget.dataset.key;
    this.setData({
      [`form.${key}`]: event.detail.value
    });
  },

  toggleDefault() {
    this.setData({
      'form.isDefault': !this.data.form.isDefault
    });
  },

  onSave() {
    const { name, phone, region, detail } = this.data.form;
    if (!name || !phone || !region || !detail) {
      wx.showToast({
        title: '请填写完整地址',
        icon: 'none'
      });
      return;
    }
    saveAddress(this.data.form);
    wx.showToast({
      title: '已保存',
      icon: 'success'
    });
    setTimeout(() => {
      wx.navigateBack();
    }, 500);
  }
});
