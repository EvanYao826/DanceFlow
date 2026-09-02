<script setup lang="ts">
import { computed, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const form = reactive({ username: '', password: '' })
const isAdminLogin = computed(() => route.query.redirect?.toString().startsWith('/admin') || route.query.admin === '1')

async function submit() {
  try {
    await auth.login(form.username, form.password)
    if (isAdminLogin.value !== auth.isAdmin) {
      const message = isAdminLogin.value ? '该账号不是管理员，请使用用户端登录' : '管理员账号请从管理端入口登录'
      auth.logout()
      ElMessage.error(message)
      return
    }
    ElMessage.success('登录成功')
    const redirect = route.query.redirect?.toString()
    await router.push(redirect || (auth.isAdmin && isAdminLogin.value ? '/admin' : '/'))
  } catch {
    // 请求拦截器负责展示后端错误。
  }
}
</script>

<template>
  <main class="auth-page">
    <div class="wave-layer" aria-hidden="true"><span></span><span></span><span></span></div>
    <section class="brand-panel">
      <div class="brand-top"><BrandLogo variant="white" :height="42" to="/" /></div>
      <div class="brand-copy">
        <p class="eyebrow">{{ isAdminLogin ? 'FLOWARTIST ADMIN' : 'FLOWARTIST COMMUNITY' }}</p>
        <h1 v-if="isAdminLogin"><span>管理社团</span><span>每一次跃动</span></h1>
        <h1 v-else><span>让喜欢街舞</span><span>的人相遇</span></h1>
        <p>{{ isAdminLogin ? '从成员审核到活动运营，一处管理社团日常。' : '记录训练、参与活动，把每一次练习变成成长。' }}</p>
        <div class="brand-line" aria-hidden="true"><span></span><span></span><span></span></div>
      </div>
      <div class="brand-foot"><span>DanceFlow</span><span>FlowArtist 街舞社</span></div>
    </section>
    <section class="form-panel">
      <div class="form-wrap">
        <div class="form-heading">
          <p class="login-context">{{ isAdminLogin ? 'ADMIN PORTAL' : 'WELCOME BACK' }}</p>
          <h2>{{ isAdminLogin ? '管理端登录' : '登录 DanceFlow' }}</h2>
          <p class="form-subtitle">{{ isAdminLogin ? '请输入管理员账号继续' : '登录后进入你的街舞社团空间' }}</p>
        </div>
        <el-form :model="form" label-position="top" @submit.prevent="submit">
          <el-form-item label="用户名" required><el-input v-model="form.username" size="large" autocomplete="username" placeholder="请输入用户名" /></el-form-item>
          <el-form-item label="密码" required><el-input v-model="form.password" size="large" type="password" show-password autocomplete="current-password" placeholder="请输入密码" /></el-form-item>
          <el-button type="primary" native-type="submit" :loading="auth.loading" class="form-button" size="large">登录</el-button>
        </el-form>
        <div class="form-links">
          <el-link v-if="!isAdminLogin" type="primary" @click="router.push('/register')">注册新账号</el-link>
          <el-link v-else type="primary" @click="router.push('/login')">用户端登录</el-link>
          <el-link v-if="!isAdminLogin" @click="router.push({ name: 'login', query: { admin: '1', redirect: '/admin' } })">管理员入口</el-link>
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: grid; grid-template-columns: minmax(390px, .92fr) minmax(480px, 1.08fr); position: relative; overflow: hidden; background: linear-gradient(115deg, #25123e 0%, #622d86 37%, #c56fd0 61%, #f6eef7 100%); }
.auth-page::before { content: ''; position: absolute; inset: 0; pointer-events: none; background: linear-gradient(90deg, rgba(22, 10, 39, .34), transparent 58%), repeating-linear-gradient(122deg, transparent 0 110px, rgba(255,255,255,.045) 111px 112px); }
.wave-layer { position: absolute; inset: auto 0 0; z-index: 0; height: 38%; overflow: hidden; pointer-events: none; opacity: .38; }
.wave-layer span { position: absolute; left: -8%; width: 116%; height: 180px; border-radius: 48% 52% 0 0 / 30% 30% 0 0; background: rgba(104, 25, 145, .38); transform: translateX(-2%) rotate(-2deg); animation: wave-drift 11s ease-in-out infinite alternate; }
.wave-layer span:nth-child(1) { bottom: -118px; opacity: .72; }.wave-layer span:nth-child(2) { bottom: -145px; background: rgba(181, 71, 194, .42); transform: translateX(3%) rotate(2deg); animation-duration: 14s; animation-delay: -3s; }.wave-layer span:nth-child(3) { bottom: -168px; background: rgba(68, 18, 108, .3); transform: translateX(-4%) rotate(-1deg); animation-duration: 17s; animation-delay: -6s; }
.brand-panel, .form-panel { position: relative; z-index: 1; }
.brand-panel { display: flex; flex-direction: column; padding: 42px clamp(36px, 6vw, 92px); color: #fff; }
.brand-panel::after { content: 'DF'; position: absolute; right: -28px; bottom: 68px; color: rgba(255,255,255,.08); font-size: clamp(180px, 20vw, 290px); font-weight: 800; line-height: 1; }
.brand-top, .brand-copy, .brand-foot { position: relative; z-index: 1; }
.brand-top { display: flex; align-items: center; }
.brand-copy { max-width: 430px; margin: auto 0; }
.eyebrow { margin: 0 0 20px; color: #f3a7ff; font-size: 11px; letter-spacing: 2.2px; font-weight: 700; }
.brand-copy h1 { display: grid; gap: 3px; margin: 0 0 22px; font-size: clamp(38px, 4.2vw, 58px); line-height: 1.18; letter-spacing: 0; }
.brand-copy h1 span { display: block; white-space: nowrap; }
.brand-copy p:not(.eyebrow) { max-width: 330px; margin: 0; color: rgba(255,255,255,.76); line-height: 1.9; font-size: 14px; }
.brand-line { display: flex; gap: 7px; margin-top: 34px; }
.brand-line span { display: block; width: 38px; height: 3px; background: #f3a7ff; }.brand-line span:nth-child(2) { width: 18px; opacity: .6; }.brand-line span:nth-child(3) { width: 8px; opacity: .32; }
.brand-foot { display: flex; gap: 18px; color: rgba(255,255,255,.54); font-size: 12px; }.brand-foot span + span { padding-left: 18px; border-left: 1px solid rgba(255,255,255,.2); }
.form-panel { display: grid; place-items: center; padding: 56px clamp(28px, 7vw, 105px); }
.form-wrap { width: min(100%, 430px); padding: clamp(28px, 4vw, 44px); border: 1px solid rgba(255,255,255,.7); border-radius: 18px; background: rgba(255,255,255,.78); box-shadow: 0 24px 70px rgba(62, 23, 78, .2); backdrop-filter: blur(22px); }
.form-heading { position: relative; }.portal-badge { display: inline-flex; align-items: center; gap: 8px; padding: 7px 11px; border-radius: 999px; background: rgba(225,62,247,.1); color: var(--df-primary-700); font-size: 12px; font-weight: 600; }.portal-badge i { width: 6px; height: 6px; border-radius: 50%; background: var(--df-primary-500); }
.login-context { margin: 27px 0 10px; color: var(--df-primary-600); font-size: 11px; letter-spacing: 2px; font-weight: 700; }.form-wrap h2 { margin: 0; color: var(--df-primary-950); font-size: 30px; line-height: 1.3; }.form-subtitle { margin: 9px 0 30px; color: #766b7b; font-size: 14px; }
.el-form { text-align: left; }.form-button { width: 100%; height: 46px; margin: 7px 0 20px; border: 0; border-radius: 9px; background: linear-gradient(100deg, #b820d5, #e13ef7); box-shadow: 0 10px 22px rgba(201,30,219,.22); font-weight: 600; }.form-links { display: flex; justify-content: space-between; align-items: center; }
:deep(.el-form-item) { margin-bottom: 19px; }.el-form-item :deep(.el-form-item__label) { margin-bottom: 7px; color: #4f4454; font-size: 13px; font-weight: 600; }.el-form-item :deep(.el-input__wrapper) { min-height: 44px; padding: 1px 14px; border: 1px solid rgba(105,81,111,.16); border-radius: 9px; background: rgba(255,255,255,.82); box-shadow: none; transition: border-color .2s, box-shadow .2s; }.el-form-item :deep(.el-input__wrapper:hover) { border-color: rgba(201,30,219,.45); }.el-form-item :deep(.el-input__wrapper.is-focus) { border-color: var(--df-primary-400); box-shadow: 0 0 0 3px rgba(225,62,247,.1); }.el-form-item :deep(.el-input__inner) { color: #392d3e; caret-color: var(--df-primary-600); }.el-form-item :deep(.el-input__inner:-webkit-autofill), .el-form-item :deep(.el-input__inner:-webkit-autofill:hover), .el-form-item :deep(.el-input__inner:-webkit-autofill:focus) { -webkit-text-fill-color: #392d3e; -webkit-box-shadow: 0 0 0 1000px rgba(255,255,255,.82) inset; box-shadow: 0 0 0 1000px rgba(255,255,255,.82) inset; transition: background-color 9999s ease-out 0s; }
@keyframes wave-drift { from { margin-left: -2%; } to { margin-left: 4%; } }
@media (prefers-reduced-motion: reduce) { .wave-layer span { animation: none; } }
@media (max-width: 820px) { .auth-page { display: block; overflow: auto; background: linear-gradient(160deg, #32174e 0%, #6f328c 35%, #f6eef7 72%); }.brand-panel { min-height: 290px; padding: 28px 24px 34px; }.brand-copy { margin: 54px 0 0; }.brand-copy h1 { font-size: 36px; }.brand-copy p:not(.eyebrow), .brand-foot { display: none; }.brand-panel::after { right: -15px; bottom: -30px; font-size: 170px; }.wave-layer { height: 28%; }.form-panel { padding: 0 18px 40px; }.form-wrap { margin-top: -1px; padding: 30px 24px 34px; border-radius: 0 0 18px 18px; background: rgba(255,255,255,.9); }.login-context { margin-top: 20px; } }
@media (max-width: 420px) { .brand-panel { min-height: 270px; }.brand-copy { margin-top: 42px; }.brand-copy h1 { font-size: 32px; }.form-panel { padding-inline: 12px; }.form-wrap { padding-inline: 20px; } }
</style>
