import { createRouter, createWebHistory } from 'vue-router';
import PortalLayout from '@/layouts/PortalLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';
import BlankLayout from '@/layouts/BlankLayout.vue';
import { defaultPathForRole, getCurrentUser, hasRoutePermission } from '@/utils/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: PortalLayout,
      redirect: '/portal/home',
      children: [
        { path: 'portal/home', name: 'PortalHome', component: () => import('@/views/portal/Home.vue'), meta: { title: '首页' } },
        { path: 'portal/matches', name: 'PortalMatches', component: () => import('@/views/portal/Matches.vue'), meta: { title: '赛程' } },
        { path: 'portal/matches/:id', name: 'PortalMatchDetail', component: () => import('@/views/portal/MatchDetail.vue'), meta: { title: '比赛详情' } },
        { path: 'portal/teams', name: 'PortalTeams', component: () => import('@/views/portal/Teams.vue'), meta: { title: '球队' } },
        { path: 'portal/teams/:id', name: 'PortalTeamDetail', component: () => import('@/views/portal/TeamDetail.vue'), meta: { title: '球队详情' } },
        { path: 'portal/cities', name: 'PortalCities', component: () => import('@/views/portal/Cities.vue'), meta: { title: '城市场馆' } },
        { path: 'portal/standings', name: 'PortalStandings', component: () => import('@/views/portal/Standings.vue'), meta: { title: '积分榜' } },
        { path: 'portal/bracket', name: 'PortalBracket', component: () => import('@/views/portal/Bracket.vue'), meta: { title: '淘汰赛' } },
        { path: 'portal/favorites', name: 'PortalFavorites', component: () => import('@/views/portal/Favorites.vue'), meta: { title: '我的收藏', requiresAuth: true } },
        { path: 'portal/profile', name: 'PortalProfile', component: () => import('@/views/portal/Profile.vue'), meta: { title: '个人中心', requiresAuth: true } },
      ],
    },
    {
      path: '/admin',
      component: AdminLayout,
      redirect: '/admin/dashboard',
      meta: { requiresAuth: true, roles: ['ADMIN'] },
      children: [
        { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '后台概览' } },
        { path: 'teams', name: 'AdminTeams', component: () => import('@/views/admin/Teams.vue'), meta: { title: '球队管理' } },
        { path: 'matches', name: 'AdminMatches', component: () => import('@/views/admin/Matches.vue'), meta: { title: '赛程管理' } },
        { path: 'standings', name: 'AdminStandings', component: () => import('@/views/admin/Standings.vue'), meta: { title: '积分榜管理' } },
        { path: 'comments', name: 'AdminComments', component: () => import('@/views/admin/Comments.vue'), meta: { title: '评论审核' } },
        { path: 'analytics', name: 'AdminAnalytics', component: () => import('@/views/admin/Analytics.vue'), meta: { title: '图表统计' } },
        { path: 'data-maintenance', name: 'AdminDataMaintenance', component: () => import('@/views/admin/DataMaintenance.vue'), meta: { title: '数据维护' } },
        { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/Users.vue'), meta: { title: '用户管理' } },
      ],
    },
    {
      path: '/login',
      component: BlankLayout,
      children: [{ path: '', name: 'Login', component: () => import('@/views/common/Login.vue'), meta: { title: '登录' } }],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
});

router.beforeEach((to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 世界杯赛事信息系统` : '世界杯赛事信息系统';
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
