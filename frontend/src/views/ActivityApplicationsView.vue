<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getActivityApplications, type Activity, type ActivityApply } from '@/api/activities'
import { getAdminActivity } from '@/api/admin'
const route = useRoute(); const router = useRouter()
const activity = ref<Activity | null>(null); const records = ref<ActivityApply[]>([]); const loading = ref(true)
onMounted(async () => {
  try {
    activity.value = (await getAdminActivity(String(route.params.id))).data
    records.value = (await getActivityApplications(String(route.params.id))).data
  } finally { loading.value = false }
})
function formatTime(value?: string) { return value?.replace('T', ' ').slice(0, 16) || '-' }
</script>
<template><section class="content" v-loading="loading"><div class="title"><div><h1>报名名单</h1><p>{{ activity?.title || '加载中…' }}</p></div><el-button @click="router.push('/admin/activities')">返回活动列表</el-button></div><el-table v-loading="loading" :data="records" stripe><el-table-column prop="nickname" label="昵称" min-width="140" /><el-table-column prop="username" label="账号" min-width="140" /><el-table-column label="报名时间" min-width="170"><template #default="scope">{{ formatTime(scope.row.applyTime) }}</template></el-table-column><el-table-column label="备注" min-width="220"><template #default="scope">{{ scope.row.remark || '-' }}</template></el-table-column><el-table-column label="状态"><template #default><el-tag type="success">已报名</el-tag></template></el-table-column></el-table><el-empty v-if="!loading && !records.length" description="暂无报名记录" /></section></template>
<style scoped>.content{padding:0}.title{display:flex;justify-content:space-between;align-items:start;margin-bottom:20px}.title h1{margin:0;color:var(--df-primary-950)}.title p{margin:8px 0 0;color:#77717e}</style>
