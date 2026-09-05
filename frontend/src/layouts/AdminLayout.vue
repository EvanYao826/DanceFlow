<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const items = [
  { label: '工作台', path: '/admin/dashboard' },
  { label: '成员审核', path: '/admin/members' },
  { label: '活动管理', path: '/admin/activities' },
  { label: '课程管理', path: '/admin/courses' },
  { label: '作品审核', path: '/admin/works' },
  { label: '公告管理', path: '/admin/notices' },
  { label: '讨论管理', path: '/admin/posts' },
]
const activePath = computed(() => route.path)
</script>

<template>
  <div class="admin-shell">
    <aside class="sidebar">
      <div class="brand"><BrandLogo variant="white" :height="32" to="/admin" /></div>
      <p class="section-label">社团管理</p>
      <nav><router-link v-for="item in items" :key="item.path" :to="item.path" :class="{ active: activePath === item.path }">{{ item.label }}</router-link></nav>
      <div class="sidebar-bottom"><el-button link @click="auth.logout(); router.push('/login?admin=1&redirect=/admin')">退出管理端</el-button></div>
    </aside>
    <div class="admin-main"><header class="admin-header"><span>{{ route.meta.title || '管理端' }}</span><span class="user-name">{{ auth.user?.nickname || '管理员' }}</span></header><main class="admin-content"><router-view /></main></div>
  </div>
</template>

<style scoped>
.admin-shell { min-height: 100vh; display: flex; background: #f5f6fa; color: #25212c; }.sidebar { width: 232px; flex-shrink: 0; display: flex; flex-direction: column; background: var(--df-primary-950); color: #fff; }.brand { height: 72px; display: flex; align-items: center; padding: 0 24px; border-bottom: 1px solid rgba(255,255,255,.1); }.section-label { padding: 28px 24px 10px; margin: 0; color: var(--df-primary-300); font-size: 11px; letter-spacing: 1.5px; text-transform: uppercase; }.sidebar nav { display: grid; gap: 4px; padding: 0 12px; }.sidebar nav a { padding: 12px; border-radius: 6px; color: #d8d1e1; text-decoration: none; }.sidebar nav a:hover, .sidebar nav a.active { background: var(--df-primary-700); color: #fff; }.sidebar-bottom { display: grid; gap: 4px; margin-top: auto; padding: 16px; border-top: 1px solid rgba(255,255,255,.1); }.sidebar-bottom :deep(.el-button) { justify-content: flex-start; color: #d8d1e1; }.admin-main { min-width: 0; flex: 1; }.admin-header { height: 72px; display: flex; align-items: center; justify-content: space-between; padding: 0 32px; background: #fff; border-bottom: 1px solid #e8e8ef; font-weight: 600; }.user-name { font-size: 13px; color: #77717e; font-weight: 400; }.admin-content { padding: 28px 32px; }@media (max-width: 700px) { .sidebar { width: 72px; }.brand { justify-content: center; padding: 0; }.sidebar nav a { font-size: 0; text-align: center; }.sidebar nav a::before { content: '•'; font-size: 20px; }.section-label, .sidebar-bottom :deep(.el-button) { font-size: 0; padding: 8px; justify-content: center; }.admin-header { padding: 0 18px; }.admin-content { padding: 20px 14px; } }
.admin-content :deep(.admin-page > header) { display: none; }
.admin-content :deep(.admin-page) { min-height: auto; background: transparent; }
.admin-content :deep(.admin-page .content) { padding: 0; }
</style>
