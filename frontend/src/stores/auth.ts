import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import request, { clearToken, get, getToken, post, put, setToken } from '@/api/request'

export interface UserInfo {
  id: number | string
  username: string
  nickname: string
  avatar?: string
  phone?: string
  email?: string
  role: string
}

interface LoginResponse {
  token: string
  expiresIn: number
  user: UserInfo
  permissions: string[]
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(getToken())
  const user = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])
  const loading = ref(false)
  const isLoggedIn = computed(() => Boolean(token.value && user.value))
  const isAdmin = computed(() => ['ADMIN', 'SUPER_ADMIN'].includes(user.value?.role ?? ''))
  const isSuperAdmin = computed(() => user.value?.role === 'SUPER_ADMIN')

  async function login(username: string, password: string) {
    loading.value = true
    try {
      const result = await post<LoginResponse>('/auth/login', { username, password })
      token.value = result.data.token
      user.value = result.data.user
      permissions.value = result.data.permissions
      setToken(token.value)
    } finally {
      loading.value = false
    }
  }

  async function register(username: string, password: string, nickname: string) {
    await post('/auth/register', { username, password, nickname })
  }

  async function restore() {
    if (!getToken()) return false
    try {
      const result = await get<UserInfo>('/auth/me')
      user.value = result.data
      token.value = getToken()
      return true
    } catch {
      logout()
      return false
    }
  }

  async function updateProfile(payload: Record<string, string>) {
    const result = await put<UserInfo>('/users/me', payload)
    user.value = result.data
  }

  async function logout() {
    const oldToken = getToken()
    token.value = null
    user.value = null
    permissions.value = []
    clearToken()
    if (oldToken) {
      // 通知后端拉黑旧 Token（本地已清除，需显式携带）；失败不阻塞登出
      try {
        await request.post('/auth/logout', null, { headers: { Authorization: `Bearer ${oldToken}` } })
      } catch {
        /* 后端不可达时旧 Token 依赖自然过期兜底 */
      }
    }
  }

  return { token, user, permissions, loading, isLoggedIn, isAdmin, isSuperAdmin, login, register, restore, updateProfile, logout }
})
