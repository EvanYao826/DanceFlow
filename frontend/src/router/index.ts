import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/api/request'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { title: '登录', guestOnly: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { title: '注册', guestOnly: true },
    },
    {
      path: '/403',
      name: 'forbidden',
      component: () => import('@/views/ForbiddenView.vue'),
      meta: { title: '无权限' },
    },
    {
      path: '/admin',
      component: () => import('@/layouts/AdminLayout.vue'),
      redirect: '/admin/dashboard',
      meta: { title: '管理端', requiresAuth: true, requiresAdmin: true },
      children: [
        { path: 'dashboard', name: 'admin', component: () => import('@/views/AdminHomeView.vue'), meta: { title: '工作台' } },
        { path: 'members', name: 'admin-members', component: () => import('@/views/MemberManagementView.vue'), meta: { title: '成员审核' } },
        { path: 'activities', name: 'admin-activities', component: () => import('@/views/AdminActivitiesView.vue'), meta: { title: '活动管理' } },
      ],
    },
    {
      path: '/activities',
      name: 'activities',
      component: () => import('@/views/ActivitiesView.vue'),
      meta: { title: '活动列表' },
    },
    {
      path: '/activities/:id',
      name: 'activity-detail',
      component: () => import('@/views/ActivityDetailView.vue'),
      meta: { title: '活动详情' },
    },
    {
      path: '/profile/applications',
      name: 'my-applications',
      component: () => import('@/views/MyApplicationsView.vue'),
      meta: { title: '我的报名', requiresAuth: true },
    },
    {
      path: '/club',
      name: 'club',
      component: () => import('@/views/ClubView.vue'),
      meta: { title: '社团中心' },
    },
    {
      path: '/',
      redirect: '/club',
      name: 'home',
      meta: { title: '首页' },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue'),
      meta: { title: '页面不存在' },
    },
  ],
})

router.beforeEach(async (to) => {
  const auth = useAuthStore()
  if (getToken() && !auth.user) await auth.restore()
  if (to.meta.guestOnly && auth.isLoggedIn) return '/'
  if (to.meta.requiresAuth && !auth.isLoggedIn) return { name: 'login', query: { redirect: to.fullPath } }
  if (to.meta.requiresAdmin && !auth.isAdmin) return '/403'
})

router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} - DanceFlow` : 'DanceFlow'
})

export default router
