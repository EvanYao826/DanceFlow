package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.ActivityApplyRequest;
import com.danceflow.entity.Activity;
import com.danceflow.entity.ActivityApply;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.ActivityApplyMapper;
import com.danceflow.mapper.ActivityMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.ActivityApplyVO;
import com.danceflow.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityApplyService {
    private final ActivityApplyMapper applyMapper;
    private final ActivityMapper activityMapper;
    private final UserMapper userMapper;

    public ActivityApplyService(ActivityApplyMapper applyMapper, ActivityMapper activityMapper, UserMapper userMapper) {
        this.applyMapper = applyMapper;
        this.activityMapper = activityMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public ActivityApplyVO apply(Long userId, Long activityId, ActivityApplyRequest request) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || !"PUBLISHED".equals(activity.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "活动不存在或未发布");
        if (activity.getApplyDeadline().isBefore(LocalDateTime.now())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "报名已截止");
        ActivityApply current = applyMapper.selectForUpdate(activityId, userId);
        if (current != null && "APPLIED".equals(current.getApplyStatus())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "不能重复报名");
        long count = applyMapper.selectCount(new LambdaQueryWrapper<ActivityApply>().eq(ActivityApply::getActivityId, activityId).eq(ActivityApply::getApplyStatus, "APPLIED"));
        if (count >= activity.getCapacity()) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "活动已满员");
        if (current == null) {
            current = new ActivityApply(); current.setActivityId(activityId); current.setUserId(userId); current.setIsDeleted(0);
        }
        current.setApplyStatus("APPLIED"); current.setRemark(request == null ? null : request.remark()); current.setApplyTime(LocalDateTime.now());
        if (current.getId() == null) applyMapper.insert(current); else applyMapper.updateById(current);
        return toVO(current);
    }

    @Transactional
    public void cancel(Long userId, Long activityId) {
        ActivityApply current = applyMapper.selectForUpdate(activityId, userId);
        if (current == null || !"APPLIED".equals(current.getApplyStatus())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "你尚未报名该活动");
        current.setApplyStatus("CANCELLED"); applyMapper.updateById(current);
    }

    public List<ActivityApplyVO> mine(Long userId) {
        return applyMapper.selectList(new LambdaQueryWrapper<ActivityApply>().eq(ActivityApply::getUserId, userId).orderByDesc(ActivityApply::getApplyTime))
                .stream().map(this::toVO).toList();
    }

    private ActivityApplyVO toVO(ActivityApply apply) {
        User user = userMapper.selectById(apply.getUserId());
        return ActivityApplyVO.from(apply, UserVO.from(user));
    }
}
