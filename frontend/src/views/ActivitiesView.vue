<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getActivities, type Activity } from '@/api/activities'
import { useRouter } from 'vue-router'
import danceImageOne from '@/assets/dance1.png'
import danceImageTwo from '@/assets/dance2.png'
import danceImageThree from '@/assets/dance3.png'

const records = ref<Activity[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const typeFilter = ref('全部')
const typeOptions = ['全部', '训练课', '社团活动', '成果展示', '比赛']
const router = useRouter()
const danceImages = [danceImageOne, danceImageTwo, danceImageThree]
async function load(next = page.value) { loading.value = true; page.value = next; try { const result = await getActivities(page.value, 12, typeFilter.value === '全部' ? '' : typeFilter.value); records.value = result.data.records; total.value = result.data.total } finally { loading.value = false } }
onMounted(load)
function changeType() { load(1) }
function formatTime(value: string) { return value?.replace('T', ' ').slice(0, 16) }
</script>

<template>
  <main class="activities-page"><UserTopbar />
    <section class="heading"><p class="eyebrow">UPCOMING EVENTS</p><h1>社团活动</h1><p>训练、交流和舞台，总有一场活动适合你。</p><el-segmented v-model="typeFilter" :options="typeOptions" class="filter" @change="changeType" /></section>
    <section class="activity-grid" v-loading="loading"><el-empty v-if="!loading && !records.length" description="暂无已发布活动" /><el-card v-for="(item, index) in records" :key="item.id" class="activity-card" shadow="hover" @click="router.push(`/activities/${item.id}`)"><div class="cover"><img :src="danceImages[index % danceImages.length]" alt="" /><span>{{ item.activityType }}</span></div><div class="card-body"><div class="card-meta">{{ formatTime(item.startTime) }} · {{ item.location }}</div><h2>{{ item.title }}</h2><p>{{ item.description || '期待在活动现场与你相见。' }}</p><div class="card-footer"><el-tag effect="plain">剩余 {{ item.remainingCapacity ?? item.capacity }} 人</el-tag><el-button link type="primary">查看详情 →</el-button></div></div></el-card></section>
    <el-pagination v-if="total" v-model:current-page="page" layout="prev, pager, next" :page-size="12" :total="total" @current-change="load" />
  </main>
</template>

<style scoped>
.activities-page { min-height: 100vh; background: linear-gradient(145deg, #fbf7ff 0%, #f6effa 48%, #fff 100%); }.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; background: #fff; }.topbar nav { display: flex; gap: 22px; }.heading { padding: 56px 24px 40px; max-width: 1120px; margin: auto; }.eyebrow { color: var(--df-primary-700); letter-spacing: 2px; font-size: 12px; }.heading h1 { margin: 10px 0; font-size: 42px; color: var(--df-primary-950); }.heading p:last-child { color: #77717e; }.filter { margin-top: 24px; }.activity-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; max-width: 1120px; min-height: 260px; margin: auto; padding: 0 24px 36px; }.activity-card { cursor: pointer; overflow: hidden; }.cover { position: relative; height: 120px; display: flex; align-items: flex-end; padding: 16px; overflow: hidden; background: linear-gradient(135deg, var(--df-primary-800), var(--df-primary-500)); color: #fff; }.cover::after { content: ''; position: absolute; inset: 0; background: linear-gradient(180deg, transparent, rgba(39,4,53,.68)); }.cover img { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; }.cover span { position: relative; z-index: 1; }.card-body { padding: 18px; }.card-meta { color: var(--df-primary-700); font-size: 12px; }.card-body h2 { margin: 10px 0; font-size: 20px; }.card-body p { height: 42px; overflow: hidden; color: #77717e; line-height: 1.5; }.card-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 16px; }.el-pagination { justify-content: center; padding: 20px 0 48px; }
@media (max-width: 820px) { .activity-grid { grid-template-columns: repeat(2, 1fr); } } @media (max-width: 560px) { .activity-grid { grid-template-columns: 1fr; } .heading h1 { font-size: 34px; } }
</style>
