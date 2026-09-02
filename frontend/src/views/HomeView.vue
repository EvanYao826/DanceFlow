<script setup lang="ts">
// 占位首页，阶段 1 起按 PlanDocs/01 逐步替换为真实首页
interface PaletteColor {
  step: number
  hex: string
}

// 品牌色阶预览（与 src/styles/index.css 中的 --df-primary-* 保持一致）
const palette: PaletteColor[] = [
  { step: 50, hex: '#FDF3FF' },
  { step: 100, hex: '#FAE7FF' },
  { step: 200, hex: '#F6CEFF' },
  { step: 300, hex: '#F3A7FF' },
  { step: 400, hex: '#EE73FF' },
  { step: 500, hex: '#E13EF7' },
  { step: 600, hex: '#C91EDB' },
  { step: 700, hex: '#AA15B6' },
  { step: 800, hex: '#9915A2' },
  { step: 900, hex: '#751679' },
  { step: 950, hex: '#4F0052' },
]
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
        <el-space :size="12">
          <el-button type="primary">主要按钮</el-button>
          <el-button plain>次要按钮</el-button>
        </el-space>
      </el-space>
      <div class="palette" aria-label="品牌色阶预览">
        <div
          v-for="color in palette"
          :key="color.step"
          class="swatch"
          :class="{ 'swatch-light-text': color.step >= 400 }"
          :style="{ background: color.hex }"
        >
          <span class="step">{{ color.step }}</span>
          <span class="hex">{{ color.hex }}</span>
        </div>
      </div>
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
  color: var(--df-primary-700);
}

.palette {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 20px;
}

.swatch {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 72px;
  height: 56px;
  padding-left: 10px;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 8px;
  font-size: 11px;
  line-height: 1.4;
  color: var(--df-primary-950);
}

.swatch-light-text {
  color: #fff;
}

.step {
  font-weight: 600;
}
</style>
