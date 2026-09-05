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
      meta: { title: '管理端', requiresAuth: true, requiresAdmin: true, portal: 'admin' },
      children: [
        { path: 'dashboard', name: 'admin', component: () => import('@/views/AdminHomeView.vue'), meta: { title: '工作台' } },
        { path: 'members', name: 'admin-members', component: () => import('@/views/MemberManagementView.vue'), meta: { title: '成员审核' } },
        { path: 'activities', name: 'admin-activities', component: () => import('@/views/ActivityManagementView.vue'), meta: { title: '活动管理' } },
        { path: 'activities/new', name: 'admin-activity-new', component: () => import('@/views/ActivityEditorView.vue'), meta: { title: '新增活动' } },
        { path: 'activities/:id/edit', name: 'admin-activity-edit', component: () => import('@/views/ActivityEditorView.vue'), meta: { title: '编辑活动' } },
        { path: 'courses', name: 'admin-courses', component: () => import('@/views/CourseManagementView.vue'), meta: { title: '课程管理' } },
        { path: 'works', name: 'admin-works', component: () => import('@/views/WorkManagementView.vue'), meta: { title: '作品审核' } },
        { path: 'works/:id', name: 'admin-work-preview', component: () => import('@/views/AdminWorkPreviewView.vue'), meta: { title: '作品预览' } },
        { path: 'notices', name: 'admin-notices', component: () => import('@/views/NoticeManagementView.vue'), meta: { title: '公告管理' } },
        { path: 'posts', name: 'admin-posts', component: () => import('@/views/PostManagementView.vue'), meta: { title: '讨论管理' } },
      ],
    },
    {
      path: '/community',
      name: 'community',
      component: () => import('@/views/CommunityView.vue'),
      meta: { title: '社区', portal: 'user' },
    },
    {
      path: '/community/posts/create',
      name: 'community-post-create',
      component: () => import('@/views/CommunityPostEditorView.vue'),
      meta: { title: '发布讨论', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/community/posts/:id',
      name: 'community-post-detail',
      component: () => import('@/views/CommunityPostDetailView.vue'),
      meta: { title: '讨论详情', portal: 'user' },
    },
    {
      path: '/notices/:id',
      name: 'notice-detail',
      component: () => import('@/views/NoticeDetailView.vue'),
      meta: { title: '公告详情', portal: 'user' },
    },
    {
      path: '/works',
      name: 'works',
      component: () => import('@/views/WorksView.vue'),
      meta: { title: '作品社区', portal: 'user' },
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/ProfileView.vue'),
      meta: { title: '个人中心', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/profile/collections',
      name: 'profile-collections',
      component: () => import('@/views/CollectionsView.vue'),
      meta: { title: '我的收藏', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/profile/activities',
      name: 'profile-activities',
      component: () => import('@/views/ProfileActivitiesView.vue'),
      meta: { title: '我的活动', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/profile/courses',
      name: 'profile-courses',
      component: () => import('@/views/MyLearningView.vue'),
      meta: { title: '我的学习', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/profile/points',
      name: 'profile-points',
      component: () => import('@/views/PointsView.vue'),
      meta: { title: '积分明细', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/works/create',
      name: 'work-create',
      component: () => import('@/views/WorkCreateView.vue'),
      meta: { title: '发布作品', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/works/:id',
      name: 'work-detail',
      component: () => import('@/views/WorkDetailView.vue'),
      meta: { title: '作品详情', portal: 'user' },
    },
    {
      path: '/profile/works',
      name: 'my-works',
      component: () => import('@/views/MyWorksView.vue'),
      meta: { title: '我的作品', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/activities',
      name: 'activities',
      component: () => import('@/views/ActivitiesView.vue'),
      meta: { title: '活动列表', portal: 'user' },
    },
    {
      path: '/activities/:id',
      name: 'activity-detail',
      component: () => import('@/views/ActivityDetailView.vue'),
      meta: { title: '活动详情', portal: 'user' },
    },
    {
      path: '/courses',
      name: 'courses',
      component: () => import('@/views/CoursesView.vue'),
      meta: { title: '课程中心', portal: 'user' },
    },
    {
      path: '/courses/my',
      name: 'my-learning',
      component: () => import('@/views/MyLearningView.vue'),
      meta: { title: '我的学习', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/courses/:id/lessons/:lessonId',
      name: 'lesson-learning',
      component: () => import('@/views/LessonLearningView.vue'),
      meta: { title: '课时学习', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/courses/:id',
      name: 'course-detail',
      component: () => import('@/views/CourseDetailView.vue'),
      meta: { title: '课程详情', portal: 'user' },
    },
    {
      path: '/profile/applications',
      name: 'my-applications',
      component: () => import('@/views/MyApplicationsView.vue'),
      meta: { title: '我的报名', requiresAuth: true, portal: 'user' },
    },
    {
      path: '/club',
      name: 'club',
      component: () => import('@/views/ClubView.vue'),
      meta: { title: '社团中心', portal: 'user' },
    },
    {
      path: '/',
      redirect: '/club',
      name: 'home',
      meta: { title: '首页', portal: 'user' },
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
  if (auth.isLoggedIn && to.meta.portal === 'admin' && !auth.isAdmin) return '/403'
  if (auth.isLoggedIn && to.meta.portal === 'user' && auth.isAdmin) return '/admin/dashboard'
  if (to.meta.requiresAuth && !auth.isLoggedIn) return { name: 'login', query: { redirect: to.fullPath } }
  if (to.meta.requiresAdmin && !auth.isAdmin) return '/403'
})

router.afterEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} - DanceFlow` : 'DanceFlow'
})

export default router
