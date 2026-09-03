<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getMyApplications, type ActivityApply } from '@/api/activities'
import UserTopbar from '@/components/UserTopbar.vue'

const records = ref<ActivityApply[]>([]); const loading = ref(true)
onMounted(async () => { try { records.value = (await getMyApplications()).data } finally { loading.value = false } })
const labels: Record<string, string> = { APPLIED: '已报名', CANCELLED: '已取消' }
</script>

<template><main class="applications-page"><UserTopbar /><section class="content"><h1>我的报名</h1><el-table v-loading="loading" :data="records" stripe><el-table-column prop="activityId" label="活动编号" width="120" /><el-table-column prop="applyTime" label="报名时间" /><el-table-column prop="remark" label="备注" /><el-table-column label="状态" width="110"><template #default="scope"><el-tag :type="scope.row.applyStatus === 'APPLIED' ? 'success' : 'info'">{{ labels[scope.row.applyStatus] || scope.row.applyStatus }}</el-tag></template></el-table-column></el-table><el-empty v-if="!loading && !records.length" description="还没有报名记录" /></section></main></template>

<style scoped>.applications-page { min-height: 100vh; background: #fbfbfd; }.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; background: #fff; }.content { max-width: 1000px; margin: auto; padding: 56px 24px; }.content h1 { color: var(--df-primary-950); }</style>
