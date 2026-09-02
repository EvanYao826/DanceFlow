import { del, get, post } from '@/api/request'

export interface Activity {
  id: number | string
  title: string
  coverUrl?: string
  description?: string
  activityType: string
  startTime: string
  endTime: string
  location: string
  capacity: number
  applyDeadline: string
  status: string
  publisherName: string
  applied?: boolean
  appliedCount?: number
  remainingCapacity?: number
  applyStatus?: string
}

export interface ActivityApply {
  id: number | string
  activityId: number | string
  applyStatus: string
  remark?: string
  applyTime: string
  nickname: string
}

export interface PageResult<T> { records: T[]; total: number; page: number; pageSize: number }
export const getActivities = (page = 1, pageSize = 12) => get<PageResult<Activity>>('/activities', { page, pageSize })
export const getActivity = (id: string) => get<Activity>(`/activities/${id}`)
export const applyActivity = (id: string, remark?: string) => post<ActivityApply>(`/activities/${id}/apply`, { remark })
export const cancelActivity = (id: string) => del<void>(`/activities/${id}/apply`)
export const getMyApplications = () => get<ActivityApply[]>('/activities/my')
