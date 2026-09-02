<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import BrandLogo from '@/components/BrandLogo.vue'
const auth = useAuthStore()
</script>

<template>
  <el-container class="admin-page">
    <el-header><BrandLogo variant="purple" :height="30" /><el-button link @click="auth.logout">退出登录</el-button></el-header>
    <el-main><el-result icon="success" title="管理端已就绪" sub-title="业务管理模块将在后续阶段接入" /></el-main>
  </el-container>
</template>

<style scoped>
.admin-page { min-height: 100vh; background: #f7f8fa; }
.el-header { display: flex; align-items: center; justify-content: space-between; background: #fff; border-bottom: 1px solid #ebeef5; }
</style>
