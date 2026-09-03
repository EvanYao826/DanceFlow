import { del, get, post, put } from '@/api/request'

export interface PageResult<T> { records: T[]; total: number; page: number; pageSize: number }
export interface CourseLesson { id: number | string; courseId: number | string; title: string; videoUrl?: string; duration: number; content?: string; sortNo: number; status: string; progressSeconds?: number; completed?: boolean }
export interface Course { id: number | string; title: string; coverUrl?: string; danceType: string; difficulty: string; teacherName: string; description?: string; lessonCount: number; status: string; completedCount?: number; progressPercent?: number; lessons?: CourseLesson[] }
export interface LearningCourse { courseId: number | string; title: string; coverUrl?: string; danceType: string; lessonCount: number; completedCount: number; progressPercent: number }
export interface LessonLearning { course: Course; lesson: CourseLesson }
export const getCourses = (params?: object) => get<PageResult<Course>>('/courses', params)
export const getCourse = (id: string) => get<Course>(`/courses/${id}`)
export const getLesson = (courseId: string, lessonId: string) => get<LessonLearning>(`/courses/${courseId}/lessons/${lessonId}`)
export const saveLessonProgress = (courseId: string, lessonId: string, data: { progressSeconds: number; completed?: boolean }) => put<CourseLesson>(`/courses/${courseId}/lessons/${lessonId}/progress`, data)
export const getMyLearning = () => get<LearningCourse[]>('/courses/my')
export const getAdminCourses = (params?: object) => get<PageResult<Course>>('/admin/courses', params)
export const getAdminCourse = (id: string | number) => get<Course>(`/admin/courses/${id}`)
export const createCourse = (payload: object) => post<Course>('/admin/courses', payload)
export const updateCourse = (id: string | number, payload: object) => put<Course>(`/admin/courses/${id}`, payload)
export const updateCourseStatus = (id: string | number, status: string) => put<Course>(`/admin/courses/${id}/status`, { status })
export const deleteCourse = (id: string | number) => del<void>(`/admin/courses/${id}`)
export const createLesson = (courseId: string | number, payload: object) => post<CourseLesson>(`/admin/courses/${courseId}/lessons`, payload)
export const updateLesson = (id: string | number, payload: object) => put<CourseLesson>(`/admin/lessons/${id}`, payload)
export const deleteLesson = (id: string | number) => del<void>(`/admin/lessons/${id}`)
