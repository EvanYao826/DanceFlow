<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const form = reactive({ username: '', password: '' })

async function submit() {
  try {
    await auth.login(form.username, form.password)
    ElMessage.success('登录成功')
    await router.push('/')
  } catch {
    // 请求拦截器负责展示后端错误。
  }
}
</script>

<template>
  <main class="auth-page">
    <el-card class="auth-card">
      <BrandLogo variant="purple" :height="42" />
      <h1>登录 DanceFlow</h1>
      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="用户名" required><el-input v-model="form.username" autocomplete="username" /></el-form-item>
        <el-form-item label="密码" required><el-input v-model="form.password" type="password" show-password autocomplete="current-password" /></el-form-item>
        <el-button type="primary" native-type="submit" :loading="auth.loading" class="form-button">登录</el-button>
      </el-form>
      <el-link type="primary" @click="router.push('/register')">还没有账号？立即注册</el-link>
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
