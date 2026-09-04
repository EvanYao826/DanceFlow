<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getProfileActivities } from '@/api/profile'
import type { Activity } from '@/api/activities'

const router = useRouter()
const records = ref<Activity[]>([])
const loading = ref(true)
const page = ref(1)
const total = ref(0)

async function load(nextPage = page.value) {
  loading.value = true
  page.value = nextPage
  try {
    const { data } = await getProfileActivities({ page: page.value, pageSize: 10 })
    records.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <main class="activities-page">
    <section class="content">
      <div class="page-heading"><p>MY ACTIVITIES</p><h1>我的活动</h1><span>查看你已经报名参与的社团活动。</span></div>
      <div v-loading="loading" class="list">
        <el-empty v-if="!loading && !records.length" description="还没有报名活动" />
        <article v-for="item in records" :key="item.id" @click="router.push(`/activities/${item.id}`)">
          <time>{{ item.startTime?.slice(0, 10) || '待定' }}</time><div><h2>{{ item.title }}</h2><p>{{ item.location || '地点待定' }} · {{ item.activityType || '社团活动' }}</p></div><el-button link type="primary">查看详情</el-button>
        </article>
      </div>
      <el-pagination v-if="total > 10" v-model:current-page="page" layout="prev, pager, next" :page-size="10" :total="total" @current-change="load" />
    </section>
  </main>
</template>

<style scoped>
.activities-page { min-height: 100vh; color: rgba(255,255,255,.9); }.content { max-width: 1040px; margin: auto; padding: 64px 24px; }.page-heading { margin-bottom: 30px; }.page-heading p { margin: 0 0 10px; color: #f0a8fb; letter-spacing: 2px; font-size: 12px; font-weight: 700; }.page-heading h1 { margin: 0 0 10px; color: #fff; font-size: 42px; }.page-heading span { color: rgba(255,255,255,.7); }.list { min-height: 240px; }.list article { display: flex; align-items: center; gap: 20px; margin-bottom: 14px; padding: 18px 20px; border: 1px solid rgba(244,205,250,.24); border-radius: 10px; background: rgba(54,12,70,.8); cursor: pointer; }.list article:hover { border-color: rgba(240,168,251,.55); background: rgba(76,18,91,.9); }.list time { min-width: 92px; color: #f0a8fb; font-weight: 700; }.list article div { flex: 1; }.list h2, .list p { margin: 0; }.list h2 { color: #fff; font-size: 18px; }.list p { margin-top: 7px; color: rgba(255,255,255,.68); font-size: 13px; }.el-pagination { justify-content: center; margin-top: 28px; } @media (max-width: 600px) { .content { padding: 42px 18px; }.list article { align-items: flex-start; flex-wrap: wrap; }.list article .el-button { margin-left: 112px; } }
</style>
