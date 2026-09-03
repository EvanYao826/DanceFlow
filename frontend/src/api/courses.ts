import { get, put } from '@/api/request'

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
