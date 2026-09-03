<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const links = [
  { label: '社团档案', path: '/club' },
  { label: '活动', path: '/activities' },
  { label: '课程', path: '/courses' },
]

function isActive(path: string) {
  return route.path === path || (path !== '/courses' && route.path.startsWith(`${path}/`))
}

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <header class="user-topbar">
    <BrandLogo variant="white" :height="34" to="/club" />
    <nav class="main-nav" aria-label="用户端导航">
      <router-link v-for="link in links" :key="link.path" :to="link.path" :class="{ active: isActive(link.path) }">{{ link.label }}</router-link>
      <router-link v-if="auth.isLoggedIn" to="/profile/applications" :class="{ active: isActive('/profile/applications') }">我的报名</router-link>
      <router-link v-if="auth.isLoggedIn" to="/courses/my" :class="{ active: isActive('/courses/my') }">我的学习</router-link>
    </nav>
    <div class="account-area">
      <el-dropdown v-if="auth.isLoggedIn" trigger="click" @command="logout">
        <button class="account-button" type="button"><span class="avatar">{{ (auth.user?.nickname || auth.user?.username || '舞').slice(0, 1) }}</span><span class="account-name">{{ auth.user?.nickname || auth.user?.username }}</span><span class="chevron">⌄</span></button>
        <template #dropdown><el-dropdown-menu><el-dropdown-item command="logout">退出登录</el-dropdown-item></el-dropdown-menu></template>
      </el-dropdown>
      <el-link v-else type="primary" @click="router.push('/login')">登录</el-link>
    </div>
  </header>
</template>

<style scoped>
.user-topbar { min-height: 68px; display: grid; grid-template-columns: 1fr auto 1fr; align-items: center; gap: 24px; padding: 0 clamp(18px, 5vw, 64px); background: linear-gradient(105deg, rgba(42,7,58,.94), rgba(91,18,108,.9) 48%, rgba(54,9,76,.94)); border-bottom: 1px solid rgba(239,188,248,.24); box-shadow: 0 8px 24px rgba(24,3,39,.26); backdrop-filter: blur(16px); }.main-nav { display: flex; align-items: center; justify-content: center; gap: 26px; }.main-nav a { position: relative; padding: 24px 0 21px; color: rgba(255,255,255,.72); font-size: 14px; text-decoration: none; white-space: nowrap; }.main-nav a:hover, .main-nav a.active { color: #fff; }.main-nav a.active::after { content: ''; position: absolute; right: 0; bottom: 14px; left: 0; height: 3px; border-radius: 2px; background: #f0a8fb; box-shadow: 0 0 12px rgba(240,168,251,.55); }.account-area { display: flex; justify-content: flex-end; }.account-button { display: inline-flex; align-items: center; gap: 8px; border: 0; background: transparent; color: rgba(255,255,255,.9); cursor: pointer; }.avatar { display: grid; place-items: center; width: 30px; height: 30px; border-radius: 50%; background: linear-gradient(135deg, #b92bd1, #ef9afa); color: #fff; font-size: 13px; }.account-name { max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 13px; }.chevron { color: rgba(255,255,255,.62); font-size: 16px; }
@media (max-width: 760px) { .user-topbar { grid-template-columns: auto 1fr auto; gap: 12px; padding-inline: 14px; }.main-nav { gap: 12px; overflow-x: auto; justify-content: flex-start; }.main-nav a { padding: 22px 0 19px; font-size: 12px; }.account-name { display: none; }.main-nav a.active::after { bottom: 12px; } }
</style>
