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
    <BrandLogo variant="purple" :height="34" to="/club" />
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
.user-topbar { min-height: 68px; display: grid; grid-template-columns: 1fr auto 1fr; align-items: center; gap: 24px; padding: 0 clamp(18px, 5vw, 64px); background: linear-gradient(105deg, rgba(255,255,255,.94), rgba(246,226,251,.96) 48%, rgba(255,255,255,.92)); border-bottom: 1px solid rgba(130,80,150,.2); box-shadow: 0 7px 22px rgba(93,38,112,.1); backdrop-filter: blur(16px); }.main-nav { display: flex; align-items: center; justify-content: center; gap: 26px; }.main-nav a { position: relative; padding: 24px 0 21px; color: #66566e; font-size: 14px; text-decoration: none; white-space: nowrap; }.main-nav a:hover, .main-nav a.active { color: var(--df-primary-700); }.main-nav a.active::after { content: ''; position: absolute; right: 0; bottom: 14px; left: 0; height: 2px; border-radius: 2px; background: var(--df-primary-500); }.account-area { display: flex; justify-content: flex-end; }.account-button { display: inline-flex; align-items: center; gap: 8px; border: 0; background: transparent; color: #4c4053; cursor: pointer; }.avatar { display: grid; place-items: center; width: 30px; height: 30px; border-radius: 50%; background: linear-gradient(135deg, var(--df-primary-700), var(--df-primary-400)); color: #fff; font-size: 13px; }.account-name { max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 13px; }.chevron { color: #9b8ca2; font-size: 16px; }
@media (max-width: 760px) { .user-topbar { grid-template-columns: auto 1fr auto; gap: 12px; padding-inline: 14px; }.main-nav { gap: 12px; overflow-x: auto; justify-content: flex-start; }.main-nav a { padding: 22px 0 19px; font-size: 12px; }.account-name { display: none; }.main-nav a.active::after { bottom: 12px; } }
</style>
