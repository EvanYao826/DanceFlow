<script setup lang="ts">
import { computed } from 'vue'
import logoWhite from '@/assets/logo-flowartist-white.png'
import logoPurple from '@/assets/logo-flowartist-purple.png'

const props = withDefaults(
  defineProps<{
    /** logo 配色：purple 用于浅色背景，white 用于深色背景 */
    variant?: 'purple' | 'white'
    /** 显示高度（px），宽度按原始比例 1063:434 自动计算 */
    height?: number
    /** 点击跳转的路由地址，不传则纯展示 */
    to?: string
  }>(),
  { variant: 'purple', height: 40 },
)

const src = computed(() => (props.variant === 'white' ? logoWhite : logoPurple))
const width = computed(() => Math.round((props.height * 1063) / 434))
</script>

<template>
  <router-link v-if="to" :to="to" class="brand-logo">
    <img :src="src" alt="FlowArtist 街舞社 Logo" :height="height" :width="width" />
  </router-link>
  <span v-else class="brand-logo">
    <img :src="src" alt="FlowArtist 街舞社 Logo" :height="height" :width="width" />
  </span>
</template>

<style scoped>
.brand-logo {
  display: inline-flex;
  align-items: center;
  line-height: 0;
}

img {
  display: block;
}
</style>
