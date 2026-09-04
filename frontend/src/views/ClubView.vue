<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { applyMember, getMyMember, type MemberProfile } from '@/api/club'
import { useAuthStore } from '@/stores/auth'
import teamImageOne from '@/assets/team.png'
import teamImageTwo from '@/assets/team2.png'
import teamImageThree from '@/assets/team3.png'

const auth = useAuthStore()
const router = useRouter()
const member = ref<MemberProfile | null>(null)
const loading = ref(false)
const submitting = ref(false)
const form = reactive({ danceType: '', skillLevel: '', bio: '' })

onMounted(async () => {
  if (!auth.isLoggedIn) return
  loading.value = true
  try { member.value = (await getMyMember()).data } catch { /* 未申请时显示申请表单。 */ } finally { loading.value = false }
})

async function submit() {
  if (!auth.isLoggedIn) { await router.push({ name: 'login', query: { redirect: '/club' } }); return }
  if (!form.danceType.trim() || !form.skillLevel) { ElMessage.error('请填写舞种和技术等级'); return }
  submitting.value = true
  try { member.value = (await applyMember(form)).data; ElMessage.success('入社申请已提交') } catch { /* 请求拦截器展示错误。 */ } finally { submitting.value = false }
}

const statusText: Record<string, string> = { PENDING: '审核中', ACTIVE: '已加入', REJECTED: '未通过', QUIT: '已退出' }
const teamImages = [teamImageOne, teamImageTwo, teamImageThree]
</script>

<template>
  <main class="club-page">
    <UserTopbar />
    <section class="club-hero"><p class="eyebrow">FLOWARTIST · CLUB</p><h1>你的街舞社团档案</h1><p>记录加入社团后的每一次训练、活动与成长。</p></section>
    <section class="club-content">
      <div class="intro"><h2>关于社团</h2><p>FlowArtist 街舞社面向高校街舞爱好者，提供日常训练、舞种交流和社团活动，让每一次练习都能被记录、被看见。</p><div class="stats"><span><b>6</b> 常设舞种</span><span><b>24</b> 年度活动</span><span><b>∞</b> 舞者连接</span></div></div>
      <el-card class="apply-card" shadow="never" v-loading="loading">
        <template #header><div class="card-title"><span>成员档案</span><el-tag v-if="member" effect="plain">{{ statusText[member.memberStatus] }}</el-tag></div></template>
        <template v-if="member"><el-descriptions :column="1" border><el-descriptions-item label="昵称">{{ member.nickname }}</el-descriptions-item><el-descriptions-item label="舞种">{{ member.danceType }}</el-descriptions-item><el-descriptions-item label="等级">{{ member.skillLevel }}</el-descriptions-item><el-descriptions-item v-if="member.auditReason" label="审核说明">{{ member.auditReason }}</el-descriptions-item></el-descriptions></template>
        <template v-else><el-empty v-if="!auth.isLoggedIn" description="登录后提交入社申请" /><el-form v-else :model="form" label-position="top" @submit.prevent="submit"><el-form-item label="主修舞种" required><el-input v-model="form.danceType" placeholder="例如：Hip-hop" /></el-form-item><el-form-item label="技术等级" required><el-select v-model="form.skillLevel" placeholder="请选择" style="width: 100%"><el-option label="入门" value="BEGINNER" /><el-option label="进阶" value="INTERMEDIATE" /><el-option label="高手" value="ADVANCED" /></el-select></el-form-item><el-form-item label="个人简介"><el-input v-model="form.bio" type="textarea" :rows="3" maxlength="1000" show-word-limit /></el-form-item><el-button type="primary" native-type="submit" :loading="submitting">提交申请</el-button></el-form></template>
      </el-card>
    </section>
    <section class="club-gallery"><div><p class="eyebrow">FLOWARTIST MOMENTS</p><h2>一起跳过的日常</h2></div><div class="gallery-grid"><img v-for="(image, index) in teamImages" :key="image" :src="image" :alt="`FlowArtist 社团活动瞬间 ${index + 1}`" /></div></section>
  </main>
</template>

<style scoped>
.club-page { min-height: 100vh; background: linear-gradient(145deg, #fbf7ff 0%, #f5eef9 48%, #fff 100%); color: #24212a; }.topbar { height: 68px; display: flex; align-items: center; justify-content: space-between; max-width: 1160px; margin: auto; padding: 0 24px; background: #fff; }.topbar nav { display: flex; gap: 24px; }.club-hero { padding: 84px 24px 76px; background: linear-gradient(120deg, #4f0052, #751679 52%, #b84cc2); color: #fff; text-align: center; }.eyebrow { color: var(--df-primary-300); letter-spacing: 2px; font-size: 12px; }.club-hero h1 { margin: 14px 0; font-size: clamp(36px, 6vw, 64px); }.club-hero p:last-child { color: var(--df-primary-100); }.club-content { display: grid; grid-template-columns: 1.2fr .8fr; gap: 48px; max-width: 1100px; margin: 0 auto; padding: 56px 24px; }.intro h2, .club-gallery h2 { font-size: 32px; }.intro p { max-width: 560px; line-height: 1.9; color: #68636f; }.stats { display: flex; gap: 34px; margin-top: 42px; color: #77717e; }.stats b { display: block; color: var(--df-primary-700); font-size: 28px; }.apply-card { align-self: start; }.card-title { display: flex; justify-content: space-between; align-items: center; font-weight: 600; }.club-gallery { max-width: 1100px; margin: 0 auto; padding: 0 24px 72px; }.club-gallery h2 { margin: 8px 0 24px; color: #fff; }.gallery-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }.gallery-grid img { width: 100%; aspect-ratio: 4 / 3; object-fit: cover; border: 1px solid rgba(244,205,250,.28); border-radius: 10px; box-shadow: 0 14px 30px rgba(25,3,40,.2); }@media (max-width: 700px) { .club-content { grid-template-columns: 1fr; gap: 24px; padding-top: 32px; } .club-hero { padding: 56px 24px; }.stats { gap: 18px; }.gallery-grid { grid-template-columns: 1fr; } }
</style>
