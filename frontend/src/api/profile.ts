import { get } from '@/api/request'
import type { Activity, PageResult } from '@/api/activities'
import type { LearningCourse } from '@/api/courses'
import type { Work } from '@/api/works'
export interface Overview { workCount: number; activityCount: number; completedLessonCount: number; receivedLikeCount: number; collectionCount: number; totalPoints: number; levelName: string; levelProgress: number }
export interface PointLog { id: number; pointType: string; pointValue: number; sourceType: string; sourceId: number; remark?: string; createdAt: string }
export const getOverview = () => get<Overview>('/users/me/overview')
export const getProfileActivities = (params?: object) => get<PageResult<Activity>>('/users/me/activities', params)
export const getProfileCourses = (params?: object) => get<PageResult<LearningCourse>>('/users/me/courses', params)
export const getCollections = (params?: object) => get<PageResult<Work>>('/users/me/collections', params)
export const getPoints = (params?: object) => get<PageResult<PointLog>>('/users/me/points', params)
