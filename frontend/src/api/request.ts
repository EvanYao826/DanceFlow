import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

/** 后端统一响应结构：{ code, message, data, timestamp } */
export interface Result<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: string
}

const TOKEN_KEY = 'danceflow_token'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动携带 Token
request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一处理业务码、401、403 和网络异常
request.interceptors.response.use(
  (response) => {
    const res = response.data as Result
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return response
  },
  (error) => {
    const status = error.response?.status
    if (status === 401) {
      if (location.pathname !== '/login') location.href = `/login?redirect=${encodeURIComponent(location.pathname)}`
      ElMessage.error('未登录或登录已过期')
    } else if (status === 403) {
      ElMessage.error('没有访问权限')
    } else {
      ElMessage.error(error.message || '网络异常，请稍后重试')
    }
    return Promise.reject(error)
  },
)

async function unwrap<T>(promise: Promise<AxiosResponse<Result<T>>>): Promise<Result<T>> {
  const response = await promise
  return response.data
}

export const get = <T = unknown>(url: string, params?: object) =>
  unwrap(request.get<Result<T>>(url, { params }))

export const post = <T = unknown>(url: string, data?: object) =>
  unwrap(request.post<Result<T>>(url, data))

export const put = <T = unknown>(url: string, data?: object) =>
  unwrap(request.put<Result<T>>(url, data))

export const del = <T = unknown>(url: string) => unwrap(request.delete<Result<T>>(url))

export default request
