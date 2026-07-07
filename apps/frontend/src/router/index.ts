import { createRouter, createWebHistory } from 'vue-router';
import PortalLayout from '@/layouts/PortalLayout.vue';
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
      ],
    },
    {
      path: '/admin',
      component: AdminLayout,
      redirect: '/admin/users',
      meta: { requiresAuth: true, roles: ['ADMIN'] },
      children: [
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('@/views/admin/ManagementView.vue'),
          meta: { title: '用户管理' },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/portal/home',
    },
  ],
});

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title
    ? (typeof to.meta.title === 'string' ? `${to.meta.title} - 业务脚手架` : '业务脚手架')
    : '业务脚手架';

  const currentUser = getCurrentUser();

  // If logged in and going to login/register, redirect to role-appropriate page
  if (currentUser && (to.path === '/login' || to.path === '/register')) {
    if (currentUser.role === 'ADMIN') {
      next('/admin/users');
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

  if (to.path.startsWith('/admin') && currentUser?.role !== 'ADMIN') {
    next('/portal/home');
    return;
  }

  next();
});

export default router;
