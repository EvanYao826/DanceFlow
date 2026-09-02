import { get, put } from '@/api/request'
import type { Activity, PageResult } from '@/api/activities'
import type { MemberProfile } from '@/api/club'
export const getMembers = (page = 1) => get<PageResult<MemberProfile>>('/members', { page, pageSize: 10 })
export const auditMember = (id: string | number, status: string, reason?: string) => put(`/members/${id}/audit`, { status, reason })
export const getAdminActivities = (page = 1) => get<PageResult<Activity>>('/admin/activities', { page, pageSize: 10 })
export const updateActivityStatus = (id: string | number, status: string) => put(`/admin/activities/${id}/status`, { status })
