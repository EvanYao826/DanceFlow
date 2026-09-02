<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminActivities, updateActivityStatus } from '@/api/admin'
import type { Activity } from '@/api/activities'
import BrandLogo from '@/components/BrandLogo.vue'
import { useRouter } from 'vue-router'
const router = useRouter(); const records = ref<Activity[]>([]); const loading = ref(true)
async function load() { loading.value = true; try { records.value = (await getAdminActivities()).data.records } finally { loading.value = false } }
onMounted(load)
async function changeStatus(row: Activity, status: string) { await updateActivityStatus(row.id, status); ElMessage.success('状态已更新'); await load() }
const labels: Record<string, string> = { DRAFT: '草稿', PUBLISHED: '已发布', CLOSED: '已关闭', FINISHED: '已完成', CANCELLED: '已取消' }
</script>
<template><main class="admin-page"><header><BrandLogo variant="purple" :height="30" to="/" /><el-button link @click="router.push('/admin')">返回管理端</el-button></header><section class="content"><div class="title"><h1>活动管理</h1><el-button @click="load">刷新</el-button></div><el-table v-loading="loading" :data="records" stripe><el-table-column prop="title" label="活动名称" min-width="220" /><el-table-column prop="activityType" label="类型" /><el-table-column prop="location" label="地点" /><el-table-column label="状态"><template #default="scope"><el-tag>{{ labels[scope.row.status] }}</el-tag></template></el-table-column><el-table-column label="操作" width="220"><template #default="scope"><el-button v-if="scope.row.status === 'DRAFT'" type="success" link @click="changeStatus(scope.row, 'PUBLISHED')">发布</el-button><el-button v-if="scope.row.status === 'PUBLISHED'" type="warning" link @click="changeStatus(scope.row, 'CLOSED')">关闭</el-button><el-button v-if="scope.row.status === 'CLOSED'" type="success" link @click="changeStatus(scope.row, 'PUBLISHED')">重新发布</el-button></template></el-table-column></el-table><el-empty v-if="!loading && !records.length" description="暂无活动" /></section></main></template>
<style scoped>.admin-page { min-height: 100vh; background: #f7f8fa; }header { height: 68px; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; background: #fff; }.content { max-width: 1100px; margin: auto; padding: 40px 24px; }.title { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }h1 { color: var(--df-primary-950); }</style>
