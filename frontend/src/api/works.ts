import request, { del, get, post, put, type Result } from '@/api/request'

export interface Work { id: number | string; userId: number | string; authorName: string; title: string; coverUrl?: string; mediaUrl: string; mediaType: string; description?: string; danceType: string; auditStatus: string; auditReason?: string; likeCount: number; commentCount: number; collectionCount: number; viewCount: number; liked?: boolean; collected?: boolean; publishedTime?: string; createdAt?: string }
export interface WorkComment { id: number | string; workId: number | string; userId: number | string; authorName: string; parentId: number | string; content: string; createdAt: string }
export interface WorkAction { liked: boolean; collected: boolean; likeCount: number; collectionCount: number }
export interface PageResult<T> { records: T[]; total: number; page: number; pageSize: number }
export const getWorks = (params?: object) => get<PageResult<Work>>('/works', params)
export const getWork = (id: string | number) => get<Work>(`/works/${id}`)
export const createWork = (payload: object) => post<Work>('/works', payload)
export const updateWork = (id: string | number, payload: object) => put<Work>(`/works/${id}`, payload)
export const deleteWork = (id: string | number) => del<void>(`/works/${id}`)
export const getMyWorks = (params?: object) => get<PageResult<Work>>('/works/mine', params)
export const toggleLike = (id: string | number) => post<WorkAction>(`/works/${id}/like`)
export const toggleCollection = (id: string | number) => post<WorkAction>(`/works/${id}/collection`)
export const getComments = (id: string | number) => get<WorkComment[]>(`/works/${id}/comments`)
export const addComment = (id: string | number, payload: object) => post<WorkComment>(`/works/${id}/comments`, payload)
export const deleteComment = (id: string | number) => del<void>(`/comments/${id}`)
export const getAdminWorks = (params?: object) => get<PageResult<Work>>('/admin/works', params)
export const auditWork = (id: string | number, status: string, reason?: string) => put<Work>(`/admin/works/${id}/audit`, { status, reason })
export async function uploadFile(file: File) { const response = await request.post<Result<{ originalName: string; url: string; storageKey: string; contentType: string; size: number }>>('/files/upload', (() => { const form = new FormData(); form.append('file', file); return form })(), { headers: { 'Content-Type': 'multipart/form-data' } }); if (response.data.code !== 200) throw new Error(response.data.message); return response.data }
