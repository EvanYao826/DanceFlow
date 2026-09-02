<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminActivities, updateActivityStatus, deleteActivity } from '@/api/admin'
import type { Activity } from '@/api/activities'
import { useRouter } from 'vue-router'
const router = useRouter(); const records = ref<Activity[]>([]); const loading = ref(true); const filter = ref('ALL')
const filtered = computed(() => filter.value === 'ALL' ? records.value : records.value.filter(item => item.status === filter.value))
async function load() { loading.value = true; try { records.value = (await getAdminActivities()).data.records } finally { loading.value = false } }
onMounted(load)
async function changeStatus(row: Activity, status: string) { await updateActivityStatus(row.id, status); ElMessage.success('状态已更新'); await load() }
async function remove(row: Activity) { await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '删除活动'); await deleteActivity(row.id); ElMessage.success('活动已删除'); await load() }
const labels: Record<string, string> = { DRAFT: '草稿', PUBLISHED: '已发布', CLOSED: '已关闭', FINISHED: '已完成', CANCELLED: '已取消' }
</script>
<template><section class="content"><div class="title"><div><h1>活动管理</h1><p>创建活动、调整状态并维护报名入口。</p></div><div><el-button @click="load">刷新</el-button><el-button type="primary" @click="router.push('/admin/activities/new')">新增活动</el-button></div></div><el-segmented v-model="filter" :options="[{label:'全部',value:'ALL'},{label:'草稿',value:'DRAFT'},{label:'已发布',value:'PUBLISHED'},{label:'已关闭',value:'CLOSED'}]" class="filter" /><el-table v-loading="loading" :data="filtered" stripe><el-table-column prop="title" label="活动名称" min-width="220" /><el-table-column prop="activityType" label="类型" /><el-table-column prop="location" label="地点" /><el-table-column label="时间" min-width="160"><template #default="scope">{{ scope.row.startTime?.replace('T', ' ').slice(0, 16) }}</template></el-table-column><el-table-column label="状态"><template #default="scope"><el-tag>{{ labels[scope.row.status] }}</el-tag></template></el-table-column><el-table-column label="操作" width="290"><template #default="scope"><el-button link @click="router.push(`/admin/activities/${scope.row.id}/edit`)">编辑</el-button><el-button v-if="scope.row.status === 'DRAFT'" type="success" link @click="changeStatus(scope.row, 'PUBLISHED')">发布</el-button><el-button v-if="scope.row.status === 'PUBLISHED'" type="warning" link @click="changeStatus(scope.row, 'CLOSED')">关闭</el-button><el-button v-if="['DRAFT', 'CLOSED', 'CANCELLED'].includes(scope.row.status)" type="danger" link @click="remove(scope.row)">删除</el-button></template></el-table-column></el-table><el-empty v-if="!loading && !filtered.length" description="暂无活动" /></section></template>
<style scoped>.content { padding: 0; }.title { display:flex; justify-content:space-between; align-items:start; margin-bottom:20px; }.title h1 { margin:0; color:var(--df-primary-950); }.title p { margin:8px 0 0; color:#77717e; }.filter { margin-bottom:20px; }</style>
