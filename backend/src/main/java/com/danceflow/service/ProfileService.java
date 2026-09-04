package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.entity.*;
import com.danceflow.mapper.*;
import com.danceflow.vo.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfileService {
    private final DanceWorkMapper workMapper; private final WorkCollectionMapper collectionMapper; private final ActivityApplyMapper applyMapper;
    private final ActivityMapper activityMapper; private final LearningRecordMapper learningMapper; private final UserPointLogMapper pointMapper;
    public ProfileService(DanceWorkMapper workMapper, WorkCollectionMapper collectionMapper, ActivityApplyMapper applyMapper, ActivityMapper activityMapper, LearningRecordMapper learningMapper, UserPointLogMapper pointMapper) { this.workMapper=workMapper; this.collectionMapper=collectionMapper; this.applyMapper=applyMapper; this.activityMapper=activityMapper; this.learningMapper=learningMapper; this.pointMapper=pointMapper; }
    public UserOverviewVO overview(Long userId) {
        int workCount = workMapper.selectCount(new LambdaQueryWrapper<DanceWork>().eq(DanceWork::getUserId,userId).eq(DanceWork::getIsDeleted,0)).intValue();
        int activityCount = applyMapper.selectCount(new LambdaQueryWrapper<ActivityApply>().eq(ActivityApply::getUserId,userId).eq(ActivityApply::getIsDeleted,0).eq(ActivityApply::getApplyStatus,"APPLIED")).intValue();
        int completed = learningMapper.selectCount(new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId,userId).eq(LearningRecord::getIsDeleted,0).eq(LearningRecord::getCompleted,1)).intValue();
        int likes = workMapper.selectList(new LambdaQueryWrapper<DanceWork>().eq(DanceWork::getUserId,userId).eq(DanceWork::getIsDeleted,0)).stream().mapToInt(w -> w.getLikeCount() == null ? 0 : w.getLikeCount()).sum();
        int collections = collectionMapper.selectCount(new LambdaQueryWrapper<WorkCollection>().eq(WorkCollection::getUserId,userId).eq(WorkCollection::getIsDeleted,0)).intValue();
        int points = pointMapper.selectList(new LambdaQueryWrapper<UserPointLog>().eq(UserPointLog::getUserId,userId).eq(UserPointLog::getIsDeleted,0)).stream().mapToInt(log -> log.getPointValue() == null ? 0 : log.getPointValue()).sum();
        String level = points >= 200 ? "舞台领舞" : points >= 100 ? "进阶舞者" : points >= 40 ? "律动新星" : "练习生";
        int progress = points >= 200 ? 100 : points >= 100 ? points - 100 : points >= 40 ? points - 40 : points;
        return new UserOverviewVO(workCount, activityCount, completed, likes, collections, points, level, Math.min(progress, 100));
    }
    public PageResult<ActivityVO> activities(Long userId, long page, long pageSize) {
        List<Long> ids = applyMapper.selectList(new LambdaQueryWrapper<ActivityApply>().eq(ActivityApply::getUserId,userId).eq(ActivityApply::getIsDeleted,0).orderByDesc(ActivityApply::getApplyTime)).stream().map(ActivityApply::getActivityId).toList();
        if (ids.isEmpty()) return new PageResult<>(List.of(),0,Math.max(page,1),Math.min(Math.max(pageSize,1),100));
        Page<Activity> result = activityMapper.selectPage(new Page<>(Math.max(page,1),Math.min(Math.max(pageSize,1),100)), new LambdaQueryWrapper<Activity>().in(Activity::getId,ids).eq(Activity::getIsDeleted,0).orderByDesc(Activity::getStartTime));
        return new PageResult<>(result.getRecords().stream().map(a -> ActivityVO.from(a, null)).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }
    public PageResult<WorkVO> collections(Long userId, long page, long pageSize) {
        List<Long> ids = collectionMapper.selectList(new LambdaQueryWrapper<WorkCollection>().eq(WorkCollection::getUserId,userId).eq(WorkCollection::getIsDeleted,0).orderByDesc(WorkCollection::getCreatedAt)).stream().map(WorkCollection::getWorkId).toList();
        if (ids.isEmpty()) return new PageResult<>(List.of(),0,Math.max(page,1),Math.min(Math.max(pageSize,1),100));
        Page<DanceWork> result = workMapper.selectPage(new Page<>(Math.max(page,1),Math.min(Math.max(pageSize,1),100)),new LambdaQueryWrapper<DanceWork>().in(DanceWork::getId,ids).eq(DanceWork::getIsDeleted,0).eq(DanceWork::getAuditStatus,"PUBLISHED").orderByDesc(DanceWork::getPublishedTime));
        return new PageResult<>(result.getRecords().stream().map(work -> new WorkVO(work.getId(),work.getUserId(),"",work.getTitle(),work.getCoverUrl(),work.getMediaUrl(),work.getMediaType(),work.getDescription(),work.getDanceType(),work.getAuditStatus(),work.getAuditReason(),work.getLikeCount(),work.getCommentCount(),work.getCollectionCount(),work.getViewCount(),false,true,work.getPublishedTime(),work.getCreatedAt())).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }
    public PageResult<PointLogVO> points(Long userId, long page, long pageSize) { Page<UserPointLog> result = pointMapper.selectPage(new Page<>(Math.max(page,1),Math.min(Math.max(pageSize,1),100)),new LambdaQueryWrapper<UserPointLog>().eq(UserPointLog::getUserId,userId).eq(UserPointLog::getIsDeleted,0).orderByDesc(UserPointLog::getCreatedAt)); return new PageResult<>(result.getRecords().stream().map(PointLogVO::from).toList(),result.getTotal(),result.getCurrent(),result.getSize()); }
}
