function formatPrice(value) {
  const numberValue = Number(value || 0);
  return numberValue.toFixed(2);
}

function formatCount(value) {
  const numberValue = Number(value || 0);
  if (numberValue >= 10000) {
    return `${(numberValue / 10000).toFixed(1)}万`;
  }
  return String(numberValue);
}

module.exports = {
  formatPrice,
  formatCount
};
