import { createRouter, createWebHistory } from 'vue-router';
import PortalLayout from '@/layouts/PortalLayout.vue';
import PartnerLayout from '@/layouts/PartnerLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';
import BlankLayout from '@/layouts/BlankLayout.vue';
import { getCurrentUser } from '@/utils/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/portal/home',
    },
    // ===== Auth Routes =====
    {
      path: '/login',
      component: BlankLayout,
      children: [
        {
          path: '',
          name: 'Login',
          component: () => import('@/views/common/Login.vue'),
          meta: { title: '登录' },
        },
      ],
    },
    {
      path: '/register',
      component: BlankLayout,
      children: [
        {
          path: '',
          name: 'Register',
          component: () => import('@/views/common/Register.vue'),
          meta: { title: '注册' },
        },
      ],
    },
    // ===== Portal Routes =====
    {
      path: '/portal',
      component: PortalLayout,
      redirect: '/portal/home',
      children: [
        {
          path: 'home',
          name: 'PortalHome',
          component: () => import('@/views/portal/Home.vue'),
          meta: { title: '首页' },
        },
        {
          path: 'services',
          name: 'PortalServices',
          component: () => import('@/views/portal/Services.vue'),
          meta: { title: '服务大厅' },
        },
        {
          path: 'my-business',
          name: 'PortalMyBusiness',
          component: () => import('@/views/portal/MyBusiness.vue'),
          meta: { title: '我的业务', requiresAuth: true },
        },
        {
          path: 'profile',
          name: 'PortalProfile',
          component: () => import('@/views/portal/Profile.vue'),
          meta: { title: '个人中心', requiresAuth: true },
        },
      ],
    },
    // ===== Partner Routes =====
    {
      path: '/partner',
      component: PartnerLayout,
      redirect: '/partner/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'PartnerDashboard',
          component: () => import('@/views/partner/Dashboard.vue'),
          meta: { title: '总览', requiresAuth: true },
        },
        {
          path: 'tasks',
          name: 'PartnerTasks',
          component: () => import('@/views/partner/Tasks.vue'),
          meta: { title: '工单', requiresAuth: true },
        },
        {
          path: 'resources',
          name: 'PartnerResources',
          component: () => import('@/views/partner/Resources.vue'),
          meta: { title: '资源', requiresAuth: true },
        },
        {
          path: 'reports',
          name: 'PartnerReports',
          component: () => import('@/views/partner/Reports.vue'),
          meta: { title: '报表', requiresAuth: true },
        },
      ],
    },
    // ===== Admin Routes =====
    {
      path: '/admin',
      component: AdminLayout,
      redirect: '/admin/dashboard',
      meta: { requiresAuth: true, roles: ['ADMIN'] },
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/DashboardView.vue'),
          meta: { title: '运营总览' },
        },
        {
          path: 'analytics',
          name: 'AdminAnalytics',
          component: () => import('@/views/admin/AnalyticsView.vue'),
          meta: { title: '数据分析' },
        },
        {
          path: 'datascreen',
          name: 'AdminDataScreen',
          component: () => import('@/views/admin/DataScreenView.vue'),
          meta: { title: '数据大屏' },
        },
        {
          path: 'management',
          name: 'AdminManagement',
          component: () => import('@/views/admin/ManagementView.vue'),
          meta: { title: '客户管理' },
        },
        {
          path: 'visuallist',
          name: 'AdminVisualList',
          component: () => import('@/views/admin/VisualListView.vue'),
          meta: { title: '可视化列表' },
        },
        {
          path: 'settings',
          name: 'AdminSettings',
          component: () => import('@/views/admin/SettingsView.vue'),
          meta: { title: '系统设置' },
        },
      ],
    },
    // ===== Catch-all =====
    {
      path: '/:pathMatch(.*)*',
      redirect: '/portal/home',
    },
  ],
});

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title
    ? (typeof to.meta.title === 'string' ? `${to.meta.title} - 智享门户` : '智享门户')
    : '智享门户';

  const currentUser = getCurrentUser();

  // If logged in and going to login/register, redirect to role-appropriate page
  if (currentUser && (to.path === '/login' || to.path === '/register')) {
    if (currentUser.role === 'ADMIN') {
      next('/admin/dashboard');
    } else if (currentUser.role === 'PARTNER') {
      next('/partner/dashboard');
    } else {
      next('/portal/home');
    }
    return;
  }

  // If route requires auth and not logged in, redirect to login
  if (to.meta.requiresAuth && !currentUser) {
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
    return;
  }

  // If admin route and user is not ADMIN, redirect to home
  if (to.path.startsWith('/admin') && currentUser?.role !== 'ADMIN') {
    next('/portal/home');
    return;
  }

  next();
});

export default router;
