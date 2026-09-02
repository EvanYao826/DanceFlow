<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getActivities, type Activity } from '@/api/activities'
import BrandLogo from '@/components/BrandLogo.vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const records = ref<Activity[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const typeFilter = ref('全部')
const visibleRecords = computed(() => typeFilter.value === '全部' ? records.value : records.value.filter(item => item.activityType === typeFilter.value))
const typeOptions = computed(() => ['全部', ...new Set(records.value.map(item => item.activityType))])
async function load() { loading.value = true; try { const result = await getActivities(page.value); records.value = result.data.records; total.value = result.data.total } finally { loading.value = false } }
onMounted(load)
function formatTime(value: string) { return value?.replace('T', ' ').slice(0, 16) }
</script>

<template>
  <main class="activities-page"><header class="topbar"><BrandLogo variant="purple" :height="34" to="/" /><nav><el-link @click="router.push('/club')">社团中心</el-link><el-link v-if="auth.isLoggedIn" @click="router.push('/profile/applications')">我的报名</el-link><el-link v-if="auth.isLoggedIn" @click="auth.logout(); router.push('/login')">退出登录</el-link><el-link v-else @click="router.push('/login')">登录</el-link></nav></header>
    <section class="heading"><p class="eyebrow">UPCOMING EVENTS</p><h1>社团活动</h1><p>训练、交流和舞台，总有一场活动适合你。</p><el-segmented v-model="typeFilter" :options="typeOptions" class="filter" /></section>
    <section class="activity-grid" v-loading="loading"><el-empty v-if="!loading && !visibleRecords.length" description="暂无已发布活动" /><el-card v-for="item in visibleRecords" :key="item.id" class="activity-card" shadow="hover" @click="router.push(`/activities/${item.id}`)"><div class="cover"><span>{{ item.activityType }}</span></div><div class="card-body"><div class="card-meta">{{ formatTime(item.startTime) }} · {{ item.location }}</div><h2>{{ item.title }}</h2><p>{{ item.description || '期待在活动现场与你相见。' }}</p><div class="card-footer"><el-tag effect="plain">剩余 {{ item.remainingCapacity ?? item.capacity }} 人</el-tag><el-button link type="primary">查看详情 →</el-button></div></div></el-card></section>
    <el-pagination v-if="total" v-model:current-page="page" layout="prev, pager, next" :page-size="12" :total="total" @current-change="load" />
  </main>
</template>

<style scoped>
.activities-page { min-height: 100vh; background: #fbfbfd; }.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; background: #fff; }.topbar nav { display: flex; gap: 22px; }.heading { padding: 56px 24px 40px; max-width: 1120px; margin: auto; }.eyebrow { color: var(--df-primary-700); letter-spacing: 2px; font-size: 12px; }.heading h1 { margin: 10px 0; font-size: 42px; color: var(--df-primary-950); }.heading p:last-child { color: #77717e; }.filter { margin-top: 24px; }.activity-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; max-width: 1120px; min-height: 260px; margin: auto; padding: 0 24px 36px; }.activity-card { cursor: pointer; overflow: hidden; }.cover { height: 120px; display: flex; align-items: flex-end; padding: 16px; background: linear-gradient(135deg, var(--df-primary-800), var(--df-primary-500)); color: #fff; }.card-body { padding: 18px; }.card-meta { color: var(--df-primary-700); font-size: 12px; }.card-body h2 { margin: 10px 0; font-size: 20px; }.card-body p { height: 42px; overflow: hidden; color: #77717e; line-height: 1.5; }.card-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 16px; }.el-pagination { justify-content: center; padding: 20px 0 48px; }
@media (max-width: 820px) { .activity-grid { grid-template-columns: repeat(2, 1fr); } } @media (max-width: 560px) { .activity-grid { grid-template-columns: 1fr; } .heading h1 { font-size: 34px; } }
</style>
