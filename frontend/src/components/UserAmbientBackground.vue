<script setup lang="ts">
import { ref } from 'vue'

const bubbles = ref([
  { id: 1, x: '8%', y: '22%', size: 42, delay: '0s', popped: false },
  { id: 2, x: '23%', y: '76%', size: 24, delay: '-3s', popped: false },
  { id: 3, x: '44%', y: '18%', size: 30, delay: '-7s', popped: false },
  { id: 4, x: '69%', y: '78%', size: 52, delay: '-5s', popped: false },
  { id: 5, x: '88%', y: '27%', size: 22, delay: '-9s', popped: false },
  { id: 6, x: '93%', y: '66%', size: 36, delay: '-1s', popped: false },
])

function popBubble(bubble: (typeof bubbles.value)[number]) {
  if (bubble.popped) return
  bubble.popped = true
  window.setTimeout(() => { bubble.popped = false }, 720)
}
</script>

<template>
  <div class="ambient-background" aria-hidden="true"><div class="ambient-glow glow-one"></div><div class="ambient-glow glow-two"></div></div>
  <div class="bubble-field" aria-hidden="true">
    <button v-for="bubble in bubbles" :key="bubble.id" class="bubble" :class="{ popped: bubble.popped }" type="button" :style="{ left: bubble.x, top: bubble.y, width: `${bubble.size}px`, height: `${bubble.size}px`, animationDelay: bubble.delay }" aria-label="彩色泡泡" @click="popBubble(bubble)"></button>
  </div>
</template>

<style scoped>
.ambient-background { position: fixed; inset: 0; z-index: 0; overflow: hidden; pointer-events: none; background: linear-gradient(135deg, #fdfaff 0%, #f3e8f8 42%, #ead8f2 72%, #fbf7fd 100%); }.ambient-background::before { content: ''; position: absolute; inset: 0; background: linear-gradient(120deg, rgba(79,0,82,.05), transparent 46%), repeating-linear-gradient(125deg, transparent 0 150px, rgba(122,45,145,.035) 151px 152px); }.ambient-glow { position: absolute; width: 38vw; height: 38vw; border-radius: 50%; filter: blur(60px); opacity: .35; }.glow-one { top: -18%; left: 25%; background: rgba(212,126,222,.22); }.glow-two { right: -12%; bottom: -20%; background: rgba(112,45,148,.18); }.bubble-field { position: fixed; inset: 0; z-index: 3; pointer-events: none; }.bubble { position: absolute; padding: 0; border: 1px solid rgba(255,255,255,.78); border-radius: 50%; pointer-events: auto; cursor: pointer; opacity: .62; background: radial-gradient(circle at 30% 25%, rgba(255,255,255,.9), rgba(219,148,231,.34) 32%, rgba(138,45,165,.16) 72%, rgba(255,255,255,.06)); box-shadow: inset -5px -7px 14px rgba(113,28,137,.08), inset 5px 5px 12px rgba(255,255,255,.7), 0 8px 22px rgba(115,46,135,.08); animation: float 8s ease-in-out infinite; transition: transform .22s ease, opacity .22s ease, box-shadow .22s ease; }.bubble::after { content: ''; position: absolute; inset: -9px; border: 2px solid rgba(225,62,247,0); border-radius: 50%; transform: scale(.7); }.bubble:hover { z-index: 2; opacity: .95; transform: scale(1.45); box-shadow: inset -5px -7px 14px rgba(113,28,137,.12), inset 5px 5px 12px rgba(255,255,255,.85), 0 10px 30px rgba(115,46,135,.2); }.bubble.popped { animation: burst .72s ease-out forwards; pointer-events: none; }.bubble.popped::after { animation: ring .72s ease-out forwards; }
@keyframes float { 0%, 100% { margin-top: 0; } 50% { margin-top: -14px; } } @keyframes burst { 0% { opacity: .95; transform: scale(1.45); } 35% { opacity: .9; transform: scale(1.8); } 100% { opacity: 0; transform: scale(.1); } } @keyframes ring { 0% { border-color: rgba(225,62,247,.55); transform: scale(.7); } 100% { border-color: rgba(225,62,247,0); transform: scale(2.6); } } @media (prefers-reduced-motion: reduce) { .bubble { animation: none; } .bubble:hover { transform: scale(1.15); } }
</style>
