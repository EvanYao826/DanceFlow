<script setup lang="ts">
// 全局布局将在阶段 1 按「用户端 / 管理端」拆分
import UserAmbientBackground from '@/components/UserAmbientBackground.vue'
import UserTopbar from '@/components/UserTopbar.vue'
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
  if (path.startsWith('/community') || path.startsWith('/notices') || path.startsWith('/works')) return 5
  if (path.startsWith('/profile')) return 6
  return 0
}
router.afterEach((to, from) => { transitionName.value = pagePosition(to.path) >= pagePosition(from.path) ? 'slide-left' : 'slide-right' })
</script>

<template>
  <div class="app-shell" :class="{ 'user-shell': $route.meta.portal === 'user' }">
    <UserAmbientBackground v-if="$route.meta.portal === 'user'" />
    <UserTopbar v-if="$route.meta.portal === 'user'" />
    <transition :name="transitionName" :css="$route.meta.portal === 'user'" mode="out-in">
      <router-view :key="$route.meta.portal === 'admin' ? 'admin-shell' : $route.fullPath" />
    </transition>
  </div>
</template>

<style>
.app-shell { min-height: 100vh; position: relative; background: #25123e; }.app-shell > .slide-page-enter-active, .app-shell > .slide-page-leave-active { transition: opacity .28s ease, transform .28s ease; }.slide-page-enter-from { opacity: 0; transform: translateX(34px); }.slide-page-leave-to { opacity: 0; transform: translateX(-34px); }.user-shell > .slide-page-enter-active, .user-shell > .slide-page-leave-active { position: relative; z-index: 1; }
.user-shell .club-page, .user-shell .activities-page, .user-shell .courses-page, .user-shell .detail-page, .user-shell .learn-page, .user-shell .applications-page, .user-shell .my-page { background: transparent !important; }
.user-shell > .user-topbar { position: relative; z-index: 4; }
.user-shell > main > .user-topbar { display: none; }
.user-shell > main { position: relative; z-index: 1; }
.user-shell > main { color: rgba(255,255,255,.9); }
.user-shell > main h1, .user-shell > main h2, .user-shell > main h3 { color: #fff; }
.user-shell > main .hero h1, .user-shell > main .heading h1, .user-shell > main .detail h1, .user-shell > main .detail-content h1 { color: #fff !important; }
.user-shell > main .heading p:last-child, .user-shell > main .hero > p:last-child, .user-shell > main .description, .user-shell > main .intro p, .user-shell > main .card-body p, .user-shell > main .lesson p, .user-shell > main .info p { color: rgba(255,255,255,.7); }
.user-shell > main .eyebrow, .user-shell > main .card-meta, .user-shell > main .teacher, .user-shell > main .stats { color: #f0a8fb; }
.user-shell > main .stats b { color: #f5b5ff; }
.user-shell > main .activity-card, .user-shell > main .course-card, .user-shell > main .apply-card, .user-shell > main .lesson-content { border-color: rgba(244,205,250,.2); background: rgba(54,12,70,.72); box-shadow: 0 14px 34px rgba(25,3,40,.18); backdrop-filter: blur(14px); }
.user-shell > main .el-card__header, .user-shell > main .el-card__body { border-color: rgba(244,205,250,.2); background: transparent; color: rgba(255,255,255,.88); }
.user-shell > main .apply-card { background: rgba(250,244,252,.96); border-color: rgba(255,255,255,.7); }
.user-shell > main .apply-card .el-card__header, .user-shell > main .apply-card .el-card__body { border-color: rgba(102,44,122,.16); color: #321c3b; }
.user-shell > main .apply-card .card-title { color: #321c3b; }
.user-shell > main .el-descriptions__body, .user-shell > main .el-descriptions__table, .user-shell > main .el-descriptions__cell { background: rgba(54,12,70,.72); color: rgba(255,255,255,.88); border-color: rgba(244,205,250,.2); }
.user-shell > main .el-descriptions__label { color: rgba(255,255,255,.62); }
.user-shell > main .apply-card .el-descriptions__body, .user-shell > main .apply-card .el-descriptions__table, .user-shell > main .apply-card .el-descriptions__cell { background: rgba(255,255,255,.72); color: #321c3b; border-color: rgba(102,44,122,.16); }
.user-shell > main .apply-card .el-descriptions__label { color: #76577f; }
.user-shell > main .apply-card .el-form-item__label { color: #563661; }
.user-shell > main .apply-card .el-input__wrapper, .user-shell > main .apply-card .el-select__wrapper { background: rgba(255,255,255,.9); box-shadow: 0 0 0 1px rgba(102,44,122,.18) inset; }
.user-shell > main .apply-card .el-input__inner, .user-shell > main .apply-card .el-select__selected-item { color: #321c3b; }
.user-shell > main .el-button.is-link { color: #f0a8fb; }
.user-shell > main .el-empty__description { color: rgba(255,255,255,.66); }
.user-shell > main .activity-card .card-body, .user-shell > main .course-card .card-body { color: rgba(255,255,255,.9); }
.user-shell > main .lesson-list, .user-shell > main .lesson { border-color: rgba(244,205,250,.2); }
.user-shell > main .lesson:hover h3 { color: #f0a8fb; }
.user-shell > main .el-table, .user-shell > main .el-descriptions { --el-table-bg-color: rgba(54,12,70,.72); --el-table-tr-bg-color: transparent; --el-table-header-bg-color: rgba(255,255,255,.08); --el-table-text-color: rgba(255,255,255,.86); --el-table-header-text-color: rgba(255,255,255,.72); --el-border-color-lighter: rgba(244,205,250,.18); }
.user-shell > main .applications-page .content, .user-shell > main .my-page .content { max-width: 1040px; padding-top: 58px; }
.user-shell > main .applications-page .el-table { overflow: hidden; border: 1px solid rgba(244,205,250,.2); border-radius: 12px; background: rgba(54,12,70,.72); box-shadow: 0 14px 34px rgba(25,3,40,.18); }
.user-shell > main .applications-page .el-table th.el-table__cell { background: rgba(255,255,255,.1); color: rgba(255,255,255,.76); }
.user-shell > main .applications-page .el-table td.el-table__cell { background: transparent; color: rgba(255,255,255,.9); border-color: rgba(244,205,250,.16); }
.user-shell > main .applications-page .el-table tr:hover > td.el-table__cell { background: rgba(240,168,251,.1); }
.user-shell > main .applications-page .el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell { background: rgba(255,255,255,.035); }
.user-shell > main .my-page .list article { margin-bottom: 14px; padding: 18px 20px; border: 1px solid rgba(244,205,250,.2); border-radius: 12px; background: rgba(54,12,70,.72); box-shadow: 0 12px 28px rgba(25,3,40,.16); transition: transform .2s, border-color .2s, background .2s; }
.user-shell > main .my-page .list article:hover { transform: translateY(-3px); border-color: rgba(240,168,251,.48); background: rgba(76,18,91,.82); }
.user-shell > main .my-page .list article h2 { color: #fff; }
.user-shell > main .my-page .list article .el-progress-bar__outer { background: rgba(255,255,255,.14); }
.user-shell > main .applications-page .content h1, .user-shell > main .my-page .content h1 { color: #fff !important; }
.user-shell > main .applications-page .content, .user-shell > main .my-page .content { color: rgba(255,255,255,.9); }
.user-shell > main .my-page .eyebrow { color: #f0a8fb !important; }
.user-shell > main .my-page .info p { color: rgba(255,255,255,.72) !important; }
.user-shell > main .my-page .info h2 { color: #fff !important; }
.user-shell > main .applications-page .el-table td, .user-shell > main .applications-page .el-table th { color: rgba(255,255,255,.88) !important; }
.user-shell > main .applications-page .el-table .cell { color: inherit; }
.user-shell > main .applications-page .el-table .el-button, .user-shell > main .my-page .el-button.is-link { color: #f0a8fb !important; }
.user-shell > main .applications-page .el-table .el-button:hover, .user-shell > main .my-page .el-button.is-link:hover { color: #ffd5ff !important; }
.app-shell { overflow-x: hidden; }
.app-shell > .slide-left-enter-active, .app-shell > .slide-left-leave-active, .app-shell > .slide-right-enter-active, .app-shell > .slide-right-leave-active { transition: opacity .3s ease, transform .3s ease; }
.slide-left-enter-from { opacity: 0; transform: translateX(42px); }.slide-left-leave-to { opacity: 0; transform: translateX(-42px); }.slide-right-enter-from { opacity: 0; transform: translateX(-42px); }.slide-right-leave-to { opacity: 0; transform: translateX(42px); }
</style>
