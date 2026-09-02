package com.danceflow.vo;

import com.danceflow.entity.ActivityApply;

import java.time.LocalDateTime;

public record ActivityApplyVO(Long id, Long activityId, Long userId, String username, String nickname,
                              String applyStatus, String remark, LocalDateTime applyTime) {
    public static ActivityApplyVO from(ActivityApply apply, UserVO user) {
        return new ActivityApplyVO(apply.getId(), apply.getActivityId(), apply.getUserId(), user.username(), user.nickname(),
                apply.getApplyStatus(), apply.getRemark(), apply.getApplyTime());
    }
}
