import { get } from '@/api/request'

export interface StatisticsOverview { totalUsers: number; totalMembers: number; totalActivities: number; totalCourses: number; totalWorks: number; totalPosts: number; totalComments: number; totalApplications: number }
export interface StatisticsTrend { date: string; value: number }
export interface StatisticsRanking { id: number; name: string; value: number; extra?: string }
export interface StatisticsRankingResult { works: StatisticsRanking[]; users: StatisticsRanking[] }
export const getStatisticsOverview = () => get<StatisticsOverview>('/admin/statistics/overview')
export const getUserTrend = (params?: object) => get<StatisticsTrend[]>('/admin/statistics/user-trend', params)
export const getActivityStatistics = (params?: object) => get<Record<string, number>>('/admin/statistics/activity', params)
export const getContentStatistics = () => get<Record<string, number>>('/admin/statistics/content')
export const getStatisticsRanking = () => get<StatisticsRankingResult>('/admin/statistics/ranking')
