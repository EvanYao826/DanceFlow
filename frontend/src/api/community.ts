import { del, get, post, put } from '@/api/request'

export interface PageResult<T> { records: T[]; total: number; page: number; pageSize: number }
export interface ForumPost { id: number; userId: number; authorName: string; title: string; content: string; coverUrl?: string; category: string; status: string; viewCount: number; likeCount: number; createdAt: string }
export interface Notice { id: number; title: string; content: string; publisherId: number; publisherName: string; publishStatus: string; publishTime?: string; topFlag: boolean; createdAt: string }
export const getPosts = (params?: object) => get<PageResult<ForumPost>>('/community/posts', params)
export const getPost = (id: string | number) => get<ForumPost>(`/community/posts/${id}`)
export const createPost = (payload: object) => post<ForumPost>('/community/posts', payload)
export const updatePost = (id: string | number, payload: object) => put<ForumPost>(`/community/posts/${id}`, payload)
export const deletePost = (id: string | number) => del<void>(`/community/posts/${id}`)
export const getNotices = () => get<Notice[]>('/notices')
export const getNotice = (id: string | number) => get<Notice>(`/notices/${id}`)
export const getAdminPosts = (params?: object) => get<PageResult<ForumPost>>('/admin/posts', params)
export const updatePostStatus = (id: string | number, status: string) => put<ForumPost>(`/admin/posts/${id}/status`, { status })
export const getAdminNotices = (params?: object) => get<PageResult<Notice>>('/admin/notices', params)
export const createNotice = (payload: object) => post<Notice>('/admin/notices', payload)
export const updateNotice = (id: string | number, payload: object) => put<Notice>(`/admin/notices/${id}`, payload)
export const updateNoticeStatus = (id: string | number, status: string) => put<Notice>(`/admin/notices/${id}/status`, { status })
