<script setup lang="ts">
// 全局布局将在阶段 1 按「用户端 / 管理端」拆分
import UserAmbientBackground from '@/components/UserAmbientBackground.vue'
</script>

<template>
  <div class="app-shell" :class="{ 'user-shell': $route.meta.portal === 'user' }">
    <UserAmbientBackground v-if="$route.meta.portal === 'user'" />
    <transition name="slide-page" mode="out-in">
      <router-view :key="$route.fullPath" />
    </transition>
  </div>
</template>

<style>
.app-shell { min-height: 100vh; position: relative; }.app-shell > .slide-page-enter-active, .app-shell > .slide-page-leave-active { transition: opacity .28s ease, transform .28s ease; }.slide-page-enter-from { opacity: 0; transform: translateX(34px); }.slide-page-leave-to { opacity: 0; transform: translateX(-34px); }.user-shell > .slide-page-enter-active, .user-shell > .slide-page-leave-active { position: relative; z-index: 1; }
.user-shell .club-page, .user-shell .activities-page, .user-shell .courses-page, .user-shell .detail-page, .user-shell .learn-page, .user-shell .applications-page, .user-shell .my-page { background: transparent !important; }
.user-shell > main { position: relative; z-index: 1; }
.app-shell { overflow-x: hidden; }
</style>
