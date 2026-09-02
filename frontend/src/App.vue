<script setup lang="ts">
// 全局布局将在阶段 1 按「用户端 / 管理端」拆分
</script>

<template>
  <router-view :key="$route.fullPath" />
</template>
