<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { createPost } from '@/api/community'
const router = useRouter(); const saving = ref(false); const form = reactive({ title: '', content: '', coverUrl: '', category: '训练交流' })
async function save() { if (!form.title.trim() || !form.content.trim()) { ElMessage.error('请填写标题和正文'); return } saving.value = true; try { const post = await createPost(form); ElMessage.success('讨论已发布'); router.push(`/community/posts/${post.data.id}`) } finally { saving.value = false } }
</script>
<template><main class="post-editor"><section><p class="eyebrow">START A DISCUSSION</p><h1>发布讨论</h1><el-form label-position="top"><el-form-item label="标题" required><el-input v-model="form.title" maxlength="120" show-word-limit /></el-form-item><el-form-item label="分类"><el-select v-model="form.category" style="width:100%"><el-option label="训练交流" value="训练交流"/><el-option label="求助问答" value="求助问答"/><el-option label="活动分享" value="活动分享"/></el-select></el-form-item><el-form-item label="正文" required><el-input v-model="form.content" type="textarea" :rows="10" maxlength="10000" show-word-limit /></el-form-item><div class="actions"><el-button @click="router.back()">取消</el-button><el-button type="primary" :loading="saving" @click="save">发布讨论</el-button></div></el-form></section></main></template>
<style scoped>.post-editor{min-height:100vh;padding:58px 24px;color:#fff}.post-editor section{max-width:760px;margin:auto;padding:30px;border:1px solid rgba(244,205,250,.25);border-radius:10px;background:rgba(54,12,70,.82)}.eyebrow{color:#f0a8fb;font-size:12px;letter-spacing:2px}.post-editor h1{margin:8px 0 26px}.actions{display:flex;justify-content:flex-end;gap:10px}</style>
