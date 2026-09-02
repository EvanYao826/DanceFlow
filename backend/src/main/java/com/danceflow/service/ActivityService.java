package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.ActivityRequest;
import com.danceflow.dto.ActivityStatusRequest;
import com.danceflow.entity.Activity;
import com.danceflow.entity.User;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.ActivityMapper;
import com.danceflow.mapper.ActivityApplyMapper;
import com.danceflow.mapper.UserMapper;
import com.danceflow.vo.ActivityVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ActivityService {
    private final ActivityMapper activityMapper;
    private final UserMapper userMapper;
    private final ActivityApplyMapper applyMapper;

    public ActivityService(ActivityMapper activityMapper, UserMapper userMapper, ActivityApplyMapper applyMapper) {
        this.activityMapper = activityMapper;
        this.userMapper = userMapper;
        this.applyMapper = applyMapper;
    }

    public PageResult<ActivityVO> publicPage(long page, long pageSize) {
        Page<Activity> result = activityMapper.selectPage(new Page<>(Math.max(page, 1), Math.min(Math.max(pageSize, 1), 100)),
                new LambdaQueryWrapper<Activity>().eq(Activity::getStatus, "PUBLISHED").orderByAsc(Activity::getStartTime));
        return new PageResult<>(result.getRecords().stream().map(this::toVO).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public ActivityVO detail(Long id, boolean publicOnly) {
        return detail(id, publicOnly, null);
    }

    public ActivityVO detail(Long id, boolean publicOnly, Long userId) {
        Activity activity = required(id);
        if (publicOnly && !"PUBLISHED".equals(activity.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "活动不存在");
        return toVO(activity, userId);
    }

    @Transactional
    public ActivityVO create(Long publisherId, ActivityRequest request) {
        validateTime(request);
        Activity activity = new Activity();
        copy(activity, request);
        activity.setPublisherId(publisherId);
        activity.setStatus("DRAFT");
        activity.setIsDeleted(0);
        activityMapper.insert(activity);
        return toVO(activity);
    }

    @Transactional
    public ActivityVO update(Long id, ActivityRequest request) {
        validateTime(request);
        Activity activity = required(id);
        if ("FINISHED".equals(activity.getStatus()) || "CANCELLED".equals(activity.getStatus())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "当前状态不能编辑");
        copy(activity, request);
        activityMapper.updateById(activity);
        return toVO(activity);
    }

    @Transactional
    public ActivityVO updateStatus(Long id, ActivityStatusRequest request) {
        Activity activity = required(id);
        if (!Set.of("PUBLISHED", "CLOSED", "CANCELLED", "FINISHED").contains(request.status())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "活动状态不正确");
        if ("PUBLISHED".equals(request.status()) && activity.getApplyDeadline().isBefore(LocalDateTime.now())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "报名截止时间已过，不能发布");
        activity.setStatus(request.status());
        activityMapper.updateById(activity);
        return toVO(activity);
    }

    @Transactional
    public void delete(Long id) {
        Activity activity = required(id);
        if (!Set.of("DRAFT", "CLOSED", "CANCELLED").contains(activity.getStatus())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "当前状态不能删除");
        activityMapper.deleteById(id);
    }

    private Activity required(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "活动不存在");
        return activity;
    }

    private ActivityVO toVO(Activity activity) {
        return toVO(activity, null);
    }

    private ActivityVO toVO(Activity activity, Long userId) {
        User publisher = userMapper.selectById(activity.getPublisherId());
        String name = publisher == null ? "未知用户" : publisher.getNickname();
        long count = applyMapper.selectCount(new LambdaQueryWrapper<com.danceflow.entity.ActivityApply>()
                .eq(com.danceflow.entity.ActivityApply::getActivityId, activity.getId())
                .eq(com.danceflow.entity.ActivityApply::getApplyStatus, "APPLIED"));
        com.danceflow.entity.ActivityApply current = userId == null ? null : applyMapper.selectOne(new LambdaQueryWrapper<com.danceflow.entity.ActivityApply>()
                .eq(com.danceflow.entity.ActivityApply::getActivityId, activity.getId()).eq(com.danceflow.entity.ActivityApply::getUserId, userId).last("LIMIT 1"));
        return new ActivityVO(activity.getId(), activity.getTitle(), activity.getCoverUrl(), activity.getDescription(), activity.getActivityType(),
                activity.getStartTime(), activity.getEndTime(), activity.getLocation(), activity.getCapacity(), activity.getApplyDeadline(),
                activity.getStatus(), activity.getPublisherId(), name, current != null && "APPLIED".equals(current.getApplyStatus()), count,
                Math.max(activity.getCapacity() - (int) count, 0), current == null ? null : current.getApplyStatus());
    }

    private void copy(Activity activity, ActivityRequest request) {
        activity.setTitle(request.title()); activity.setCoverUrl(request.coverUrl()); activity.setDescription(request.description());
        activity.setActivityType(request.activityType()); activity.setStartTime(request.startTime()); activity.setEndTime(request.endTime());
        activity.setLocation(request.location()); activity.setCapacity(request.capacity()); activity.setApplyDeadline(request.applyDeadline());
    }

    private void validateTime(ActivityRequest request) {
        if (!request.startTime().isBefore(request.endTime())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "开始时间必须早于结束时间");
        if (!request.applyDeadline().isBefore(request.startTime())) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "报名截止时间必须早于开始时间");
    }
}
