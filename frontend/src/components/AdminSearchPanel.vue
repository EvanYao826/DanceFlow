<script setup lang="ts">
import { reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const route = useRoute(); const router = useRouter()
const draft = reactive({ keyword: '', type: '', status: '', danceType: '' })
const configs: Record<string, { placeholder: string; types?: { label: string; value: string }[]; statuses: { label: string; value: string }[]; dance?: boolean }> = {
  members: { placeholder: '搜索昵称或账号', statuses: [{ label:'审核中',value:'PENDING' },{ label:'已加入',value:'ACTIVE' },{ label:'未通过',value:'REJECTED' },{ label:'已退出',value:'QUIT' }], dance:true },
  activities: { placeholder: '搜索活动名称或地点', types:[{label:'训练课',value:'训练课'},{label:'社团活动',value:'社团活动'},{label:'成果展示',value:'成果展示'},{label:'比赛',value:'比赛'}], statuses:[{label:'草稿',value:'DRAFT'},{label:'已发布',value:'PUBLISHED'},{label:'已关闭',value:'CLOSED'},{label:'已完成',value:'FINISHED'}] },
  courses: { placeholder: '搜索课程名称或教师', statuses:[{label:'草稿',value:'DRAFT'},{label:'已上架',value:'PUBLISHED'},{label:'已下架',value:'OFFLINE'}], dance:true },
  works: { placeholder: '搜索作品名称或作者', statuses:[{label:'待审核',value:'PENDING'},{label:'已发布',value:'PUBLISHED'},{label:'已驳回',value:'REJECTED'},{label:'已下架',value:'OFFLINE'}], dance:true },
}
function kind() { if (route.path.includes('/members')) return 'members'; if (route.path.includes('/activities')) return 'activities'; if (route.path.includes('/courses')) return 'courses'; return 'works' }
function visible() { return ['/admin/members', '/admin/activities', '/admin/courses', '/admin/works'].includes(route.path) }
const config = () => configs[kind()]
function sync() { Object.assign(draft, { keyword:String(route.query.keyword || ''), type:String(route.query.type || ''), status:String(route.query.status || ''), danceType:String(route.query.danceType || '') }) }
function search() { router.replace({ query: Object.fromEntries(Object.entries(draft).filter(([, value]) => value)) }) }
function reset() { Object.assign(draft, { keyword:'', type:'', status:'', danceType:'' }); router.replace({ query: {} }) }
watch(() => route.fullPath, sync, { immediate:true })
</script>
<template><div v-if="visible()" class="admin-search"><el-input v-model="draft.keyword" :placeholder="config().placeholder" clearable @keyup.enter="search"/><el-select v-if="config().types" v-model="draft.type" clearable placeholder="全部类型"><el-option v-for="item in config().types" :key="item.value" v-bind="item"/></el-select><el-select v-if="config().dance" v-model="draft.danceType" clearable placeholder="全部舞种"><el-option label="Hip-hop" value="Hip-hop"/><el-option label="Jazz Funk" value="Jazz Funk"/><el-option label="Breaking" value="Breaking"/></el-select><el-select v-model="draft.status" clearable placeholder="全部状态"><el-option v-for="item in config().statuses" :key="item.value" v-bind="item"/></el-select><div class="search-actions"><el-button @click="reset">重置</el-button><el-button type="primary" @click="search">搜索</el-button></div></div></template>
<style scoped>.admin-search{display:flex;align-items:center;gap:12px;margin-bottom:22px;padding:16px;border:1px solid #e7e1eb;border-radius:10px;background:#fff}.admin-search>.el-input{width:250px}.admin-search>.el-select{width:150px}.search-actions{display:flex;gap:8px;margin-left:auto}@media(max-width:800px){.admin-search{align-items:stretch;flex-wrap:wrap}.admin-search>.el-input,.admin-search>.el-select{width:calc(50% - 6px)}.search-actions{width:100%;justify-content:flex-end}}
</style>
