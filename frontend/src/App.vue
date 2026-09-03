<script setup lang="ts">
// 全局布局将在阶段 1 按「用户端 / 管理端」拆分
import UserAmbientBackground from '@/components/UserAmbientBackground.vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const transitionName = ref('slide-left')
function pagePosition(path: string) {
  if (path === '/' || path.startsWith('/club')) return 0
  if (path.startsWith('/activities')) return 1
  if (path.startsWith('/courses/my')) return 4
  if (path.startsWith('/courses')) return 2
  if (path.startsWith('/profile/applications')) return 3
  return -1
}
router.afterEach((to, from) => { transitionName.value = pagePosition(to.path) >= pagePosition(from.path) ? 'slide-left' : 'slide-right' })
</script>

<template>
  <div class="app-shell" :class="{ 'user-shell': $route.meta.portal === 'user' }">
    <UserAmbientBackground v-if="$route.meta.portal === 'user'" />
    <transition :name="transitionName" mode="out-in">
      <router-view :key="$route.fullPath" />
    </transition>
  </div>
</template>

<style>
.app-shell { min-height: 100vh; position: relative; }.app-shell > .slide-page-enter-active, .app-shell > .slide-page-leave-active { transition: opacity .28s ease, transform .28s ease; }.slide-page-enter-from { opacity: 0; transform: translateX(34px); }.slide-page-leave-to { opacity: 0; transform: translateX(-34px); }.user-shell > .slide-page-enter-active, .user-shell > .slide-page-leave-active { position: relative; z-index: 1; }
.user-shell .club-page, .user-shell .activities-page, .user-shell .courses-page, .user-shell .detail-page, .user-shell .learn-page, .user-shell .applications-page, .user-shell .my-page { background: transparent !important; }
.user-shell > main { position: relative; z-index: 1; }
.app-shell { overflow-x: hidden; }
.app-shell > .slide-left-enter-active, .app-shell > .slide-left-leave-active, .app-shell > .slide-right-enter-active, .app-shell > .slide-right-leave-active { transition: opacity .3s ease, transform .3s ease; }
.slide-left-enter-from { opacity: 0; transform: translateX(42px); }.slide-left-leave-to { opacity: 0; transform: translateX(-42px); }.slide-right-enter-from { opacity: 0; transform: translateX(-42px); }.slide-right-leave-to { opacity: 0; transform: translateX(42px); }
</style>
