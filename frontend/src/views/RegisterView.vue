<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const form = reactive({ username: '', nickname: '', password: '', confirmPassword: '' })

async function submit() {
  if (form.password !== form.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  try {
    await auth.register(form.username, form.password, form.nickname)
    ElMessage.success('注册成功，请登录')
    await router.push('/login')
  } catch {
    // 请求拦截器负责展示后端错误。
  }
}
</script>

<template>
  <main class="auth-page">
    <el-card class="auth-card">
      <BrandLogo variant="purple" :height="42" />
      <h1>注册 DanceFlow</h1>
      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="用户名" required><el-input v-model="form.username" autocomplete="username" /></el-form-item>
        <el-form-item label="昵称" required><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="密码" required><el-input v-model="form.password" type="password" show-password autocomplete="new-password" /></el-form-item>
        <el-form-item label="确认密码" required><el-input v-model="form.confirmPassword" type="password" show-password autocomplete="new-password" /></el-form-item>
        <el-button type="primary" native-type="submit" class="form-button">注册</el-button>
      </el-form>
      <el-link type="primary" @click="router.push('/login')">已有账号？返回登录</el-link>
    </el-card>
  </main>
</template>

<style scoped>
.auth-page { min-height: 100vh; display: grid; place-items: center; padding: 24px; background: var(--df-primary-50); }
.auth-card { width: min(100%, 420px); text-align: center; }
h1 { margin: 24px 0; font-size: 24px; color: var(--df-primary-950); }
.el-form { text-align: left; }
.form-button { width: 100%; margin: 8px 0 20px; }
</style>
