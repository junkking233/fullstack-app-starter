import { createRouter, createWebHistory } from 'vue-router';
import AdminLayout from '@/layouts/AdminLayout.vue';
import { getCurrentUser } from '@/utils/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue'),
      meta: { title: '知问社区' },
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/common/Login.vue'),
      meta: { title: '登录' },
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/common/Register.vue'),
      meta: { title: '注册' },
    },
    {
      path: '/profile',
      name: 'Profile',
      component: () => import('@/views/Profile.vue'),
      meta: { title: '个人中心', requiresAuth: true },
    },
    {
      path: '/question/create',
      name: 'QuestionCreate',
      component: () => import('@/views/QuestionCreate.vue'),
      meta: { title: '发布问题', requiresAuth: true },
    },
    {
      path: '/question/:id',
      name: 'QuestionDetail',
      component: () => import('@/views/QuestionDetail.vue'),
      meta: { title: '问题详情' },
    },
    {
      path: '/search',
      name: 'Search',
      component: () => import('@/views/Search.vue'),
      meta: { title: '搜索' },
    },
    {
      path: '/notifications',
      name: 'Notifications',
      component: () => import('@/views/Notifications.vue'),
      meta: { title: '消息通知', requiresAuth: true },
    },
    {
      path: '/featured',
      name: 'Featured',
      component: () => import('@/views/Featured.vue'),
      meta: { title: '精华区' },
    },
    {
      path: '/hot',
      name: 'Hot',
      component: () => import('@/views/Hot.vue'),
      meta: { title: '热榜' },
    },
    {
      path: '/leaderboard',
      name: 'Leaderboard',
      component: () => import('@/views/Leaderboard.vue'),
      meta: { title: '排行榜' },
    },
    {
      path: '/admin',
      component: AdminLayout,
      redirect: '/admin/dashboard',
      meta: { requiresAuth: true, roles: ['ADMIN'] },
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/Dashboard.vue'),
          meta: { title: '仪表盘', icon: 'DataAnalysis' },
        },
        {
          path: 'categories',
          name: 'AdminCategories',
          component: () => import('@/views/admin/Categories.vue'),
          meta: { title: '分类管理', icon: 'Collection' },
        },
        {
          path: 'tags',
          name: 'AdminTags',
          component: () => import('@/views/admin/Tags.vue'),
          meta: { title: '标签管理', icon: 'PriceTag' },
        },
        {
          path: 'sensitive-words',
          name: 'AdminSensitiveWords',
          component: () => import('@/views/admin/SensitiveWords.vue'),
          meta: { title: '敏感词管理', icon: 'Warning' },
        },
        {
          path: 'review',
          name: 'AdminReview',
          component: () => import('@/views/admin/Review.vue'),
          meta: { title: '内容审核', icon: 'Checked' },
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('@/views/admin/Users.vue'),
          meta: { title: '用户管理', icon: 'UserFilled' },
        },
        {
          path: 'questions',
          name: 'AdminQuestions',
          component: () => import('@/views/admin/Questions.vue'),
          meta: { title: '问题管理', icon: 'QuestionFilled' },
        },
        {
          path: 'reports',
          name: 'AdminReports',
          component: () => import('@/views/admin/Reports.vue'),
          meta: { title: '举报处理', icon: 'Warning' },
        },
        {
          path: 'export',
          name: 'AdminExport',
          component: () => import('@/views/admin/Export.vue'),
          meta: { title: '数据导出', icon: 'Download' },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
});

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 知问社区` : '知问社区';

  const currentUser = getCurrentUser();

  // If logged in and going to login/register, redirect to home
  if (currentUser && (to.path === '/login' || to.path === '/register')) {
    next('/');
    return;
  }

  // If admin route and not admin, redirect to home
  if (to.path.startsWith('/admin') && currentUser?.role !== 'ADMIN') {
    next('/');
    return;
  }

  // If route requires auth and not logged in, redirect to login
  if (to.meta.requiresAuth && !currentUser) {
    next('/login');
    return;
  }

  next();
});

export default router;
