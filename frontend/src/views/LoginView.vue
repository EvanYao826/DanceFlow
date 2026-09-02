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
    <section class="brand-panel"><BrandLogo variant="white" :height="42" to="/" /><div class="brand-copy"><p class="eyebrow">{{ isAdminLogin ? 'FLOWARTIST ADMIN' : 'FLOWARTIST COMMUNITY' }}</p><h1 v-if="isAdminLogin"><span>管理社团</span><span>每一次跃动</span></h1><h1 v-else><span>让喜欢街舞</span><span>的人相遇</span></h1><p>{{ isAdminLogin ? '从成员审核到活动运营，一处管理社团日常。' : '记录训练、参与活动，把每一次练习变成成长。' }}</p></div><div class="brand-foot">DanceFlow · FlowArtist 街舞社</div></section>
    <section class="form-panel"><div class="form-wrap"><BrandLogo variant="purple" :height="34" /><p class="login-context">{{ isAdminLogin ? 'ADMIN PORTAL' : 'WELCOME BACK' }}</p><h2>{{ isAdminLogin ? '管理端登录' : '登录 DanceFlow' }}</h2><p class="form-subtitle">{{ isAdminLogin ? '请输入管理员账号继续' : '登录后进入你的街舞社团空间' }}</p><el-form :model="form" label-position="top" @submit.prevent="submit"><el-form-item label="用户名" required><el-input v-model="form.username" size="large" autocomplete="username" placeholder="请输入用户名" /></el-form-item><el-form-item label="密码" required><el-input v-model="form.password" size="large" type="password" show-password autocomplete="current-password" placeholder="请输入密码" /></el-form-item><el-button type="primary" native-type="submit" :loading="auth.loading" class="form-button" size="large">登录</el-button></el-form><div class="form-links"><el-link v-if="!isAdminLogin" type="primary" @click="router.push('/register')">注册新账号</el-link><el-link v-else type="primary" @click="router.push('/login')">用户端登录</el-link><el-link v-if="!isAdminLogin" @click="router.push({ name: 'login', query: { admin: '1', redirect: '/admin' } })">管理员入口</el-link></div><el-link class="home-link" @click="router.push('/')">返回用户端首页</el-link></div></section>
  </main>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: grid; grid-template-columns: minmax(360px, .9fr) minmax(420px, 1.1fr); background: linear-gradient(120deg, #25133f 0%, #5c2a87 43%, #f1eafa 100%); }.brand-panel { position: relative; display: flex; flex-direction: column; padding: 48px clamp(32px, 6vw, 88px); background: linear-gradient(145deg, rgba(31,16,55,.96), rgba(108,48,151,.82)); color: #fff; overflow: hidden; }.brand-panel::after { content: 'DF'; position: absolute; right: -40px; bottom: 80px; color: rgba(255,255,255,.07); font-size: 240px; font-weight: 800; line-height: 1; }.brand-copy { position: relative; z-index: 1; max-width: 420px; margin: auto 0; }.eyebrow { color: var(--df-primary-300); font-size: 11px; letter-spacing: 2px; }.brand-copy h1 { display: grid; gap: 4px; margin: 18px 0; font-size: clamp(32px, 4vw, 52px); line-height: 1.18; letter-spacing: 0; }.brand-copy h1 span { display: block; white-space: nowrap; }.brand-copy p:not(.eyebrow) { color: #e3d9ed; line-height: 1.8; }.brand-foot { position: relative; z-index: 1; color: #bcaaca; font-size: 12px; }.form-panel { display: grid; place-items: center; padding: 40px 24px; background: rgba(255,255,255,.84); backdrop-filter: blur(18px); }.form-wrap { width: min(100%, 420px); }.form-wrap > .brand-logo { display: none; }.login-context { margin: 38px 0 14px; color: var(--df-primary-600); font-size: 11px; letter-spacing: 2px; }.form-wrap h2 { margin: 0; color: var(--df-primary-950); font-size: 30px; }.form-subtitle { margin: 10px 0 32px; color: #77717e; }.el-form { text-align: left; }.form-button { width: 100%; margin: 8px 0 22px; }.form-links { display: flex; justify-content: space-between; }.home-link { display: block; margin-top: 64px; text-align: center; }@media (max-width: 720px) { .auth-page { display: block; background: linear-gradient(160deg, #32174e 0 30%, #f1eafa 72%); }.brand-panel { min-height: 260px; padding: 28px 24px; background: linear-gradient(145deg, rgba(31,16,55,.96), rgba(108,48,151,.82)); }.brand-copy { margin: 48px 0 0; }.brand-copy h1 { font-size: 32px; margin: 12px 0; }.brand-copy p:not(.eyebrow), .brand-foot { display: none; }.brand-panel::after { bottom: -30px; font-size: 160px; }.form-panel { padding: 38px 24px 48px; background: rgba(255,255,255,.92); }.login-context { margin-top: 0; }.home-link { margin-top: 36px; } }
</style>
