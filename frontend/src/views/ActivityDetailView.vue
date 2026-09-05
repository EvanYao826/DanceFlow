<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { applyActivity, cancelActivity, getActivity, type Activity } from '@/api/activities'
import { useAuthStore } from '@/stores/auth'

const route = useRoute(); const router = useRouter(); const auth = useAuthStore(); const activity = ref<Activity | null>(null); const loading = ref(true); const actionLoading = ref(false)
onMounted(async () => { try { activity.value = (await getActivity(String(route.params.id))).data } finally { loading.value = false } })
function formatTime(value?: string) { return value?.replace('T', ' ').slice(0, 16) }
async function toggleApply() {
  if (!auth.isLoggedIn) { await router.push({ name: 'login', query: { redirect: route.fullPath } }); return }
  if (!activity.value) return
  actionLoading.value = true
  try {
    if (activity.value.applied) {
      await ElMessageBox.confirm('确定取消报名吗？', '取消报名', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
      await cancelActivity(String(activity.value.id)); activity.value.applied = false; activity.value.applyStatus = 'CANCELLED'; activity.value.appliedCount = Math.max((activity.value.appliedCount || 1) - 1, 0); activity.value.remainingCapacity = (activity.value.remainingCapacity || 0) + 1; ElMessage.success('已取消报名')
    } else {
      await applyActivity(String(activity.value.id)); activity.value.applied = true; activity.value.applyStatus = 'APPLIED'; activity.value.appliedCount = (activity.value.appliedCount || 0) + 1; activity.value.remainingCapacity = Math.max((activity.value.remainingCapacity || 1) - 1, 0); ElMessage.success('报名成功')
    }
  } finally { actionLoading.value = false }
}
</script>

<template><main class="detail-page"><UserTopbar /><section v-loading="loading" class="detail-content"><template v-if="activity"><el-link @click="router.push('/activities')">返回活动列表</el-link><p class="eyebrow">{{ activity.activityType }}</p><h1>{{ activity.title }}</h1><p class="description">{{ activity.description || '期待在活动现场与你相见。' }}</p><el-descriptions :column="1" border><el-descriptions-item label="时间">{{ formatTime(activity.startTime) }} - {{ formatTime(activity.endTime)?.slice(11) }}</el-descriptions-item><el-descriptions-item label="地点">{{ activity.location }}</el-descriptions-item><el-descriptions-item label="发起人">{{ activity.publisherName }}</el-descriptions-item><el-descriptions-item label="名额">{{ activity.appliedCount }} 人已报名 · 剩余 {{ activity.remainingCapacity }} 人</el-descriptions-item><el-descriptions-item label="截止">{{ formatTime(activity.applyDeadline) }}</el-descriptions-item></el-descriptions><el-button class="apply-button" type="primary" :loading="actionLoading" :disabled="!activity.applied && activity.remainingCapacity === 0" @click="toggleApply">{{ activity.applied ? '取消报名' : activity.remainingCapacity === 0 ? '已满员' : '立即报名' }}</el-button></template></section></main></template>

<style scoped>.detail-page { min-height: 100vh; background: linear-gradient(145deg, #fbf7ff 0%, #f6effa 48%, #fff 100%); }.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; background: #fff; }.detail-content { width: min(760px, calc(100% - 32px)); max-width: 760px; margin: 48px auto; padding: 34px 36px 38px; box-sizing: border-box; border: 1px solid rgba(102,44,122,.14); border-radius: 16px; background: rgba(255,255,255,.97); box-shadow: 0 18px 42px rgba(66,28,78,.12); }.eyebrow { color: #7b2d91; letter-spacing: 2px; font-size: 12px; font-weight: 700; }.detail-content h1 { margin: 12px 0; font-size: 42px; color: #321c3b !important; }.description { margin-bottom: 32px; color: #514456 !important; line-height: 1.8; }.detail-content :deep(.el-descriptions__body), .detail-content :deep(.el-descriptions__table), .detail-content :deep(.el-descriptions__cell) { background: #fff; color: #3f3544; border-color: #e1d5e5; }.detail-content :deep(.el-descriptions__label) { color: #684575; background: #faf7fb; font-weight: 600; }.detail-content :deep(.el-descriptions__content) { color: #3f3544; }.apply-button { width: 100%; margin-top: 28px; }@media(max-width:600px){.detail-content{width:calc(100% - 24px);margin:28px auto;padding:26px 20px}.detail-content h1{font-size:32px}}</style>
