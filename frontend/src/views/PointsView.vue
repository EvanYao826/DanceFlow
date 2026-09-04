<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getPoints, type PointLog } from '@/api/profile'
const router=useRouter();const records=ref<PointLog[]>([]);const loading=ref(true)
onMounted(async()=>{try{records.value=(await getPoints({page:1,pageSize:30})).data.records}finally{loading.value=false}})
</script>
<template><main class="points-page"><section><div class="heading"><div><p class="eyebrow">POINT HISTORY</p><h1>积分明细</h1></div><el-button @click="router.push('/profile')">返回个人中心</el-button></div><div v-loading="loading" class="point-list"><article v-for="item in records" :key="item.id"><div><b>{{item.remark||item.pointType}}</b><small>{{item.createdAt?.replace('T',' ').slice(0,16)}}</small></div><strong>+{{item.pointValue}}</strong></article><el-empty v-if="!loading&&!records.length" description="暂无积分记录"/></div></section></main></template>
<style scoped>.points-page{min-height:100vh;padding:58px 24px;color:#fff}.points-page>section{max-width:780px;margin:auto}.heading{display:flex;align-items:end;justify-content:space-between;margin-bottom:28px}.eyebrow{margin:0;color:#f0a8fb;font-size:12px;letter-spacing:2px}.heading h1{margin:8px 0 0}.point-list{border:1px solid rgba(244,205,250,.24);border-radius:10px;overflow:hidden;background:rgba(54,12,70,.8)}.point-list article{display:flex;justify-content:space-between;align-items:center;padding:18px 20px;border-bottom:1px solid rgba(244,205,250,.16)}.point-list article:last-child{border:0}.point-list b,.point-list small{display:block}.point-list small{margin-top:6px;color:rgba(255,255,255,.56)}.point-list strong{color:#f0a8fb;font-size:20px}</style>
