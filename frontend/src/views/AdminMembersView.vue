<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMembers, auditMember } from '@/api/admin'
import type { MemberProfile } from '@/api/club'
import BrandLogo from '@/components/BrandLogo.vue'
import { useRouter } from 'vue-router'
const router = useRouter(); const records = ref<MemberProfile[]>([]); const loading = ref(true)
async function load() { loading.value = true; try { records.value = (await getMembers()).data.records } finally { loading.value = false } }
onMounted(load)
async function audit(row: MemberProfile, status: string) { const reason = await ElMessageBox.prompt('可填写审核说明', '审核申请', { inputPlaceholder: '审核说明' }).then(r => r.value).catch(() => null); if (reason === null) return; await auditMember(row.id, status, reason); ElMessage.success('审核完成'); await load() }
const labels: Record<string, string> = { PENDING: '审核中', ACTIVE: '已加入', REJECTED: '未通过', QUIT: '已退出' }
</script>
<template><main class="admin-page"><header><BrandLogo variant="purple" :height="30" to="/" /><el-button link @click="router.push('/admin')">返回管理端</el-button></header><section class="content"><div class="title"><h1>成员审核</h1><el-button @click="load">刷新</el-button></div><el-table v-loading="loading" :data="records" stripe><el-table-column prop="nickname" label="昵称" /><el-table-column prop="danceType" label="舞种" /><el-table-column prop="skillLevel" label="等级" /><el-table-column label="状态"><template #default="scope"><el-tag>{{ labels[scope.row.memberStatus] }}</el-tag></template></el-table-column><el-table-column label="操作"><template #default="scope"><el-button v-if="scope.row.memberStatus === 'PENDING'" type="success" link @click="audit(scope.row, 'ACTIVE')">通过</el-button><el-button v-if="scope.row.memberStatus === 'PENDING'" type="danger" link @click="audit(scope.row, 'REJECTED')">拒绝</el-button></template></el-table-column></el-table><el-empty v-if="!loading && !records.length" description="暂无成员记录" /></section></main></template>
<style scoped>.admin-page { min-height: 100vh; background: #f7f8fa; }header { height: 68px; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; background: #fff; }.content { max-width: 1100px; margin: auto; padding: 40px 24px; }.title { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }h1 { color: var(--df-primary-950); }</style>
