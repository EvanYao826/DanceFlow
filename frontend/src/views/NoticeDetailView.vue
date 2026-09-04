<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNotice, type Notice } from '@/api/community'
const route=useRoute();const router=useRouter();const notice=ref<Notice>();const loading=ref(true)
onMounted(async()=>{try{notice.value=(await getNotice(String(route.params.id))).data}finally{loading.value=false}})
</script>
<template><main class="notice-detail" v-loading="loading"><article v-if="notice"><el-button link @click="router.push('/community')">返回社区</el-button><p class="eyebrow">{{notice.topFlag?'置顶公告':'社团公告'}}</p><h1>{{notice.title}}</h1><p class="meta">{{notice.publisherName}} · {{notice.publishTime?.replace('T',' ').slice(0,16)}}</p><div class="content">{{notice.content}}</div></article><el-empty v-else-if="!loading" description="公告不存在"/></main></template>
<style scoped>.notice-detail{min-height:100vh;padding:58px 24px;color:#fff}.notice-detail article{max-width:820px;margin:auto}.eyebrow{margin:30px 0 10px;color:#f0a8fb;font-size:12px;letter-spacing:2px}.notice-detail h1{margin:0 0 12px;font-size:42px}.meta{color:rgba(255,255,255,.6);font-size:13px}.content{margin-top:34px;white-space:pre-wrap;color:rgba(255,255,255,.9);line-height:2;font-size:16px}</style>
