<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import BrandLogo from '@/components/BrandLogo.vue'
const auth = useAuthStore()
</script>

<template>
  <el-container class="admin-page">
    <el-header><BrandLogo variant="purple" :height="30" /><el-button link @click="auth.logout">退出登录</el-button></el-header>
    <el-main><el-result icon="success" title="管理端已就绪" sub-title="选择需要处理的社团事务" /><div class="links"><el-button type="primary" @click="$router.push('/admin/members')">成员审核</el-button><el-button @click="$router.push('/admin/activities')">活动管理</el-button></div></el-main>
  </el-container>
</template>

<style scoped>
.admin-page { min-height: 100vh; background: #f7f8fa; }
.el-header { display: flex; align-items: center; justify-content: space-between; background: #fff; border-bottom: 1px solid #ebeef5; }
.links { display: flex; justify-content: center; gap: 12px; }
</style>
