<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { getAdminPosts, updatePostStatus, type ForumPost } from '@/api/community'
import AdminSearchPanel from '@/components/AdminSearchPanel.vue'
const route=useRoute();const records=ref<ForumPost[]>([]);const loading=ref(true);const total=ref(0);const page=ref(1);const labels:Record<string,string>={PUBLISHED:'已发布',OFFLINE:'已下线'}
async function load(next=page.value){loading.value=true;page.value=next;try{const {data}=await getAdminPosts({page:page.value,pageSize:10,keyword:route.query.keyword,status:route.query.status});records.value=data.records;total.value=data.total}finally{loading.value=false}}onMounted(load);watch(()=>route.fullPath,()=>load())
async function status(post:ForumPost,value:string){await updatePostStatus(post.id,value);ElMessage.success('讨论状态已更新');await load()}
</script>
<template><section class="content"><div class="title"><div><h1>讨论管理</h1><p>查看社区讨论，及时下线违规内容。</p></div><el-button @click="load()">刷新</el-button></div><AdminSearchPanel /><el-table v-loading="loading" :data="records"><el-table-column prop="title" label="讨论标题" min-width="230"/><el-table-column prop="authorName" label="作者"/><el-table-column prop="category" label="分类"/><el-table-column label="浏览"><template #default="scope">{{scope.row.viewCount}}</template></el-table-column><el-table-column label="状态"><template #default="scope"><el-tag>{{labels[scope.row.status]}}</el-tag></template></el-table-column><el-table-column label="操作" width="180"><template #default="scope"><el-button v-if="scope.row.status==='PUBLISHED'" link type="warning" @click="status(scope.row as ForumPost,'OFFLINE')">下线</el-button><el-button v-else link type="success" @click="status(scope.row as ForumPost,'PUBLISHED')">恢复</el-button></template></el-table-column></el-table><el-empty v-if="!loading&&!records.length" description="暂无讨论"/><el-pagination v-if="total>10" v-model:current-page="page" layout="prev, pager, next" :page-size="10" :total="total" @current-change="load"/></section></template>
<style scoped>.content{padding:0}.title{display:flex;justify-content:space-between;align-items:start;margin-bottom:20px}.title h1{margin:0;color:var(--df-primary-950)}.title p{margin:8px 0 0;color:#77717e}.el-pagination{justify-content:center;margin-top:22px}</style>
