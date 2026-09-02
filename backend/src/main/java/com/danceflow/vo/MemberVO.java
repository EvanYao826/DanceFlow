package com.danceflow.vo;

import com.danceflow.entity.ClubMember;

import java.time.LocalDate;

public record MemberVO(Long id, Long userId, String username, String nickname, String avatar,
                       String danceType, String skillLevel, LocalDate joinDate, String memberStatus,
                       String bio, String auditReason) {
    public static MemberVO from(ClubMember member, UserVO user) {
        return new MemberVO(member.getId(), member.getUserId(), user.username(), user.nickname(), user.avatar(),
                member.getDanceType(), member.getSkillLevel(), member.getJoinDate(), member.getMemberStatus(),
                member.getBio(), member.getAuditReason());
    }
}
