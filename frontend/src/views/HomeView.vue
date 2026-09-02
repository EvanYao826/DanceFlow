<script setup lang="ts">
// 占位首页，阶段 1 起按 PlanDocs/01 逐步替换为真实首页
</script>

<template>
  <div class="home">
    <el-card class="welcome-card" shadow="hover">
      <template #header>
        <span class="title">DanceFlow · 高校街舞社团综合服务平台</span>
      </template>
      <el-space direction="vertical" alignment="flex-start" :size="12">
        <el-text>项目骨架已就绪：Spring Boot 3 后端 + Vue 3 前端，开发计划见 PlanDocs 目录。</el-text>
        <el-text>
          后端健康检查：<el-tag size="small">GET /api/health</el-tag>
          接口文档（后端启动后）：<el-tag size="small">/swagger-ui.html</el-tag>
        </el-text>
      </el-space>
    </el-card>
  </div>
</template>

<style scoped>
.home {
  display: flex;
  justify-content: center;
  padding: 48px 16px;
}

.welcome-card {
  width: 100%;
  max-width: 720px;
}

.title {
  font-size: 18px;
  font-weight: 600;
}
</style>
