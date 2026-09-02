import { get, post } from '@/api/request'

export interface MemberProfile {
  id: number | string
  userId: number | string
  username: string
  nickname: string
  avatar?: string
  danceType: string
  skillLevel: string
  joinDate?: string
  memberStatus: 'PENDING' | 'ACTIVE' | 'REJECTED' | 'QUIT'
  bio?: string
  auditReason?: string
}

export const getMyMember = () => get<MemberProfile>('/members/me')
export const applyMember = (payload: { danceType: string; skillLevel: string; bio?: string }) => post<MemberProfile>('/members/apply', payload)
