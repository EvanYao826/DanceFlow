<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCollections } from '@/api/profile'
import type { Work } from '@/api/works'
const router=useRouter();const records=ref<Work[]>([]);const loading=ref(true)
onMounted(async()=>{try{records.value=(await getCollections({page:1,pageSize:24})).data.records}finally{loading.value=false}})
</script>
<template><main class="collection-page"><section><div class="heading"><div><p class="eyebrow">MY COLLECTIONS</p><h1>我的收藏</h1></div><el-button @click="router.push('/profile')">返回个人中心</el-button></div><div v-loading="loading" class="work-grid"><article v-for="work in records" :key="work.id" @click="router.push(`/works/${work.id}`)"><img v-if="work.mediaType==='IMAGE'" :src="work.mediaUrl" :alt="work.title"/><div><span>{{work.danceType}}</span><h2>{{work.title}}</h2><p>{{work.likeCount}} 赞 · {{work.commentCount}} 评论</p></div></article><el-empty v-if="!loading&&!records.length" description="还没有收藏作品"/></div></section></main></template>
<style scoped>.collection-page{min-height:100vh;padding:58px 24px;color:#fff}.collection-page>section{max-width:1040px;margin:auto}.heading{display:flex;align-items:end;justify-content:space-between;margin-bottom:28px}.eyebrow{margin:0;color:#f0a8fb;font-size:12px;letter-spacing:2px}.heading h1{margin:8px 0 0}.work-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:18px}.work-grid article{overflow:hidden;border:1px solid rgba(244,205,250,.24);border-radius:10px;background:rgba(54,12,70,.8);cursor:pointer}.work-grid img{width:100%;height:180px;object-fit:cover}.work-grid article>div{padding:14px}.work-grid span{color:#f0a8fb;font-size:12px}.work-grid h2{margin:7px 0}.work-grid p{margin:0;color:rgba(255,255,255,.65);font-size:12px}@media(max-width:700px){.work-grid{grid-template-columns:1fr}}</style>
