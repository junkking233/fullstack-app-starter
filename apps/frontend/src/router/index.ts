import { createRouter, createWebHistory } from 'vue-router';
import PortalLayout from '@/layouts/PortalLayout.vue';
import PartnerLayout from '@/layouts/PartnerLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';
import BlankLayout from '@/layouts/BlankLayout.vue';
import { defaultPathForRole, getCurrentUser, hasRoutePermission } from '@/utils/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // 普通用户门户
    {
      path: '/',
      component: PortalLayout,
      redirect: '/portal/home',
      children: [
        {
          path: 'portal/home',
          name: 'PortalHome',
          component: () => import('@/views/portal/Home.vue'),
          meta: { title: '首页' },
        },
        {
          path: 'portal/services',
          name: 'PortalServices',
          component: () => import('@/views/portal/Services.vue'),
          meta: { title: '服务大厅' },
        },
        {
          path: 'portal/my-business',
          name: 'PortalMyBusiness',
          component: () => import('@/views/portal/MyBusiness.vue'),
          meta: { title: '我的业务', requiresAuth: true },
        },
        {
          path: 'portal/profile',
          name: 'PortalProfile',
          component: () => import('@/views/portal/Profile.vue'),
          meta: { title: '个人中心', requiresAuth: true },
        },
      ],
    },
    // 服务方门户
    {
      path: '/partner',
      component: PartnerLayout,
      redirect: '/partner/dashboard',
      meta: { requiresAuth: true, roles: ['ADMIN', 'PARTNER'] },
      children: [
        {
          path: 'dashboard',
          name: 'PartnerDashboard',
          component: () => import('@/views/partner/Dashboard.vue'),
          meta: { title: '总览' },
        },
        {
          path: 'tasks',
          name: 'PartnerTasks',
          component: () => import('@/views/partner/Tasks.vue'),
          meta: { title: '工单管理' },
        },
        {
          path: 'resources',
          name: 'PartnerResources',
          component: () => import('@/views/partner/Resources.vue'),
          meta: { title: '资源管理' },
        },
        {
          path: 'reports',
          name: 'PartnerReports',
          component: () => import('@/views/partner/Reports.vue'),
          meta: { title: '报表中心' },
        },
      ],
    },
    // 管理后台
    {
      path: '/admin/screen',
      name: 'AdminScreen',
      component: () => import('@/views/admin/Screen.vue'),
      meta: { title: '数据大屏', requiresAuth: true, roles: ['ADMIN'] },
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
          component: () => import('@/admin-platform/views/DashboardView.vue'),
          meta: { title: '运营概览', icon: 'DataAnalysis' },
        },
        {
          path: 'management',
          name: 'AdminManagement',
          component: () => import('@/admin-platform/views/ManagementView.vue'),
          meta: { title: '数据管理', icon: 'Tickets' },
        },
        {
          path: 'visual-list',
          name: 'AdminVisualList',
          component: () => import('@/admin-platform/views/VisualListView.vue'),
          meta: { title: '可视化列表', icon: 'List' },
        },
        {
          path: 'analytics',
          name: 'AdminAnalytics',
          component: () => import('@/admin-platform/views/AnalyticsView.vue'),
          meta: { title: '数据可视化', icon: 'Histogram' },
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('@/views/admin/Users.vue'),
          meta: { title: '用户管理', icon: 'UserFilled' },
        },
        {
          path: 'chat',
          name: 'AdminChat',
          component: () => import('@/views/admin/Chat.vue'),
          meta: { title: 'AI 助手', icon: 'ChatDotRound' },
        },
        {
          path: 'settings',
          name: 'AdminSettings',
          component: () => import('@/admin-platform/views/SettingsView.vue'),
          meta: { title: '系统配置', icon: 'Setting' },
        },
      ],
    },
    // 公共页面
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
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
});

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 平台名称` : '平台名称';

  const currentUser = getCurrentUser();
  if (to.path === '/login' && currentUser) {
    next(defaultPathForRole(currentUser.role));
    return;
  }

  if (to.meta.requiresAuth && !currentUser) {
    next({ path: '/login', query: { redirect: to.fullPath } });
    return;
  }

  const roles = to.meta.roles as string[] | undefined;
  if (currentUser && roles && !roles.includes(currentUser.role)) {
    next(defaultPathForRole(currentUser.role));
    return;
  }

  if (currentUser && !hasRoutePermission(to.path, currentUser.role)) {
    next(defaultPathForRole(currentUser.role));
    return;
  }

  next();
});

export default router;
