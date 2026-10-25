import { del, get, post, put } from '@/api/request'
import type { Activity, PageResult } from '@/api/activities'
import type { MemberProfile } from '@/api/club'
export interface AdminUser { id: number; username: string; nickname: string; avatar?: string; phone?: string; email?: string; role: string; status: number; lastLoginTime?: string; createdAt?: string }
export interface OperationLog { id: number; operatorName?: string; action: string; targetType?: string; targetId?: string; requestPath: string; result: string; detail?: string; createdAt?: string }
export interface RoleItem { id: number; roleCode: string; roleName: string; status: number }
export interface PermissionItem { id: number; permissionCode: string; permissionName: string; roleCode: string; type: string; path?: string; status: number }
export const getRoles = () => get<RoleItem[]>('/admin/roles')
export const getPermissions = () => get<PermissionItem[]>('/admin/permissions')
export const getAdminUsers = (params?: object) => get<PageResult<AdminUser>>('/admin/users', params)
export const updateAdminUserStatus = (id: string | number, status: number) => put<AdminUser>(`/admin/users/${id}/status`, { status })
export const updateAdminUserRole = (id: string | number, role: string) => put<AdminUser>(`/admin/users/${id}/roles`, { role })
export const resetAdminUserPassword = (id: string | number, newPassword: string) => put<void>(`/admin/users/${id}/reset-password`, { newPassword })
export const getOperationLogs = (params?: object) => get<PageResult<OperationLog>>('/admin/logs', params)
export const getMembers = (params?: object) => get<PageResult<MemberProfile>>('/members', params)
export const auditMember = (id: string | number, status: string, reason?: string) => put(`/members/${id}/audit`, { status, reason })
export const getMember = (id: string | number) => get<MemberProfile>(`/members/${id}`)
export const updateMember = (id: string | number, payload: Pick<MemberProfile, 'danceType' | 'skillLevel' | 'bio'>) => put<MemberProfile>(`/members/${id}`, payload)
export const updateMemberStatus = (id: string | number, status: string) => put<MemberProfile>(`/members/${id}/status`, { status })
export const getAdminActivities = (params?: object) => get<PageResult<Activity>>('/admin/activities', params)
export const updateActivityStatus = (id: string | number, status: string) => put(`/admin/activities/${id}/status`, { status })
export const getAdminActivity = (id: string | number) => get<Activity>(`/admin/activities/${id}`)
export const createActivity = (payload: object) => post<Activity>('/admin/activities', payload)
export const updateActivity = (id: string | number, payload: object) => put<Activity>(`/admin/activities/${id}`, payload)
export const deleteActivity = (id: string | number) => del<void>(`/admin/activities/${id}`)
