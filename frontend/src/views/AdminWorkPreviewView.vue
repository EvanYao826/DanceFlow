<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAdminWork, type Work } from '@/api/works'

const route = useRoute()
const router = useRouter()
const work = ref<Work | null>(null)
const loading = ref(true)
const statusLabels: Record<string, string> = { PENDING: '待审核', PUBLISHED: '已发布', REJECTED: '已驳回', OFFLINE: '已下架' }

onMounted(async () => {
  try {
    work.value = (await getAdminWork(String(route.params.id))).data
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <section v-loading="loading" class="preview-page">
    <el-button text @click="router.push('/admin/works')">返回作品审核</el-button>
    <template v-if="work">
      <div class="heading"><div><p>{{ work.danceType }} · {{ work.authorName }}</p><h1>{{ work.title }}</h1></div><el-tag :type="work.auditStatus === 'PENDING' ? 'warning' : work.auditStatus === 'PUBLISHED' ? 'success' : 'info'">{{ statusLabels[work.auditStatus] }}</el-tag></div>
      <div class="media"><img v-if="work.mediaType === 'IMAGE'" :src="work.mediaUrl" :alt="work.title" /><video v-else controls :src="work.mediaUrl" /></div>
      <section class="info"><p>{{ work.description || '作者没有填写作品说明。' }}</p><div><span>点赞 {{ work.likeCount }}</span><span>评论 {{ work.commentCount }}</span><span>收藏 {{ work.collectionCount }}</span><span>浏览 {{ work.viewCount }}</span></div><p v-if="work.auditReason" class="reason">审核说明：{{ work.auditReason }}</p></section>
    </template>
    <el-empty v-else-if="!loading" description="作品不存在" />
  </section>
</template>

<style scoped>
.preview-page { max-width: 980px; padding: 4px 0 30px; }.heading { display: flex; align-items: flex-start; justify-content: space-between; margin: 18px 0; }.heading p { margin: 0 0 8px; color: #8b5a99; font-size: 13px; }.heading h1 { margin: 0; color: var(--df-primary-950); font-size: 28px; }.media { display: grid; min-height: 420px; place-items: center; overflow: hidden; border: 1px solid #eee4f1; border-radius: 8px; background: #f6f1f7; }.media img, .media video { display: block; max-width: 100%; max-height: 640px; }.info { margin-top: 18px; padding: 20px; border: 1px solid #eee4f1; border-radius: 8px; background: #fff; color: #5f5264; line-height: 1.75; }.info > p { margin: 0 0 14px; }.info > div { display: flex; flex-wrap: wrap; gap: 18px; color: #896e91; font-size: 13px; }.info .reason { margin: 16px 0 0; color: #bc4d63; } @media (max-width: 700px) { .media { min-height: 260px; }.heading h1 { font-size: 24px; } }
</style>
