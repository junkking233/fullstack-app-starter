const categories = [
  { id: 1, name: '新鲜水果', icon: '🍎' },
  { id: 2, name: '安心蔬菜', icon: '🥬' },
  { id: 3, name: '肉禽蛋品', icon: '🥚' },
  { id: 4, name: '水产海鲜', icon: '🦐' },
  { id: 5, name: '乳品烘焙', icon: '🥛' },
  { id: 6, name: '粮油调味', icon: '🍚' }
];

const products = [
  {
    id: 101,
    categoryId: 1,
    name: '云南高山蓝莓',
    spec: '125g/盒',
    price: 19.9,
    originalPrice: 25.9,
    sales: 682,
    tag: '爆款',
    imageText: '蓝莓',
    color: '#dbeafe',
    description: '果粒饱满，酸甜均衡，适合早餐、沙拉和下午茶搭配。',
    origin: '云南',
    shelfLife: '冷藏3天'
  },
  {
    id: 102,
    categoryId: 1,
    name: '突尼斯软籽石榴',
    spec: '2个装',
    price: 29.9,
    originalPrice: 36.8,
    sales: 418,
    tag: '新品',
    imageText: '石榴',
    color: '#fee2e2',
    description: '籽软多汁，甜度稳定，开盖即食更省心。',
    origin: '四川会理',
    shelfLife: '常温5天'
  },
  {
    id: 201,
    categoryId: 2,
    name: '有机上海青',
    spec: '350g/份',
    price: 6.9,
    originalPrice: 8.9,
    sales: 1204,
    tag: '今日鲜采',
    imageText: '青菜',
    color: '#dcfce7',
    description: '当日采收，叶片鲜嫩，适合清炒、煮面和火锅。',
    origin: '本地合作农场',
    shelfLife: '冷藏2天'
  },
  {
    id: 202,
    categoryId: 2,
    name: '甜糯玉米',
    spec: '2根装',
    price: 9.9,
    originalPrice: 12.9,
    sales: 916,
    tag: '秒杀',
    imageText: '玉米',
    color: '#fef9c3',
    description: '香甜软糯，蒸煮皆宜，适合家庭轻食。',
    origin: '东北',
    shelfLife: '冷藏4天'
  },
  {
    id: 301,
    categoryId: 3,
    name: '谷饲鸡蛋',
    spec: '10枚/盒',
    price: 16.8,
    originalPrice: 19.9,
    sales: 1506,
    tag: '热卖',
    imageText: '鸡蛋',
    color: '#ffedd5',
    description: '蛋黄饱满，适合早餐、水煮和烘焙。',
    origin: '安徽',
    shelfLife: '冷藏15天'
  },
  {
    id: 302,
    categoryId: 3,
    name: '鲜切牛腱肉',
    spec: '500g/份',
    price: 58.8,
    originalPrice: 68.0,
    sales: 312,
    tag: '冷链',
    imageText: '牛肉',
    color: '#fee2e2',
    description: '纹理清晰，适合卤制、炖煮和健身餐备餐。',
    origin: '内蒙古',
    shelfLife: '冷藏2天'
  },
  {
    id: 401,
    categoryId: 4,
    name: '鲜活基围虾',
    spec: '400g/份',
    price: 42.9,
    originalPrice: 49.9,
    sales: 506,
    tag: '鲜活',
    imageText: '鲜虾',
    color: '#cffafe',
    description: '肉质紧实，适合白灼、油焖和蒜蓉烹饪。',
    origin: '广东湛江',
    shelfLife: '当日食用'
  },
  {
    id: 501,
    categoryId: 5,
    name: '原味低温酸奶',
    spec: '180g*6杯',
    price: 23.9,
    originalPrice: 29.9,
    sales: 763,
    tag: '冷藏',
    imageText: '酸奶',
    color: '#f8fafc',
    description: '口感顺滑，低温冷链配送，早餐搭配更轻松。',
    origin: '上海',
    shelfLife: '冷藏21天'
  },
  {
    id: 601,
    categoryId: 6,
    name: '东北长粒香米',
    spec: '5kg/袋',
    price: 45.9,
    originalPrice: 52.0,
    sales: 629,
    tag: '家庭装',
    imageText: '大米',
    color: '#fef3c7',
    description: '米香自然，颗粒分明，适合家庭日常主食。',
    origin: '黑龙江',
    shelfLife: '12个月'
  }
];

function getHomeData() {
  return {
    banners: [
      { id: 1, title: '今日鲜采直达', desc: '新人首单满39减10' },
      { id: 2, title: '晚餐食材一站购', desc: '18:00 前下单准时送达' }
    ],
    categories,
    hotProducts: products.filter((item) => [101, 201, 301, 401].includes(item.id)),
    seckillProducts: products.filter((item) => [202, 102].includes(item.id)),
    newProducts: products.filter((item) => [501, 601, 302].includes(item.id))
  };
}

function getCategories() {
  return categories;
}

function getProductsByCategory(categoryId, keyword) {
  const normalizedKeyword = (keyword || '').trim().toLowerCase();
  return products.filter((item) => {
    const categoryMatched = !categoryId || item.categoryId === Number(categoryId);
    const keywordMatched = !normalizedKeyword || item.name.toLowerCase().includes(normalizedKeyword);
    return categoryMatched && keywordMatched;
  });
}

function getProductById(id) {
  return products.find((item) => item.id === Number(id)) || products[0];
}

module.exports = {
  getHomeData,
  getCategories,
  getProductsByCategory,
  getProductById
};
