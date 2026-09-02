import { del, get, post, put } from '@/api/request'
import type { Activity, PageResult } from '@/api/activities'
import type { MemberProfile } from '@/api/club'
export const getMembers = (page = 1) => get<PageResult<MemberProfile>>('/members', { page, pageSize: 10 })
export const auditMember = (id: string | number, status: string, reason?: string) => put(`/members/${id}/audit`, { status, reason })
export const getMember = (id: string | number) => get<MemberProfile>(`/members/${id}`)
export const updateMember = (id: string | number, payload: Pick<MemberProfile, 'danceType' | 'skillLevel' | 'bio'>) => put<MemberProfile>(`/members/${id}`, payload)
export const updateMemberStatus = (id: string | number, status: string) => put<MemberProfile>(`/members/${id}/status`, { status })
export const getAdminActivities = (page = 1) => get<PageResult<Activity>>('/admin/activities', { page, pageSize: 10 })
export const updateActivityStatus = (id: string | number, status: string) => put(`/admin/activities/${id}/status`, { status })
export const getAdminActivity = (id: string | number) => get<Activity>(`/admin/activities/${id}`)
export const createActivity = (payload: object) => post<Activity>('/admin/activities', payload)
export const updateActivity = (id: string | number, payload: object) => put<Activity>(`/admin/activities/${id}`, payload)
export const deleteActivity = (id: string | number) => del<void>(`/admin/activities/${id}`)
