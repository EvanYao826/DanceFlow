package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.danceflow.entity.UserPointLog;
import com.danceflow.mapper.UserPointLogMapper;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final UserPointLogMapper pointLogMapper;
    public PointService(UserPointLogMapper pointLogMapper) { this.pointLogMapper = pointLogMapper; }
    public void grantOnce(Long userId, String pointType, int value, String sourceType, Long sourceId, String remark) {
        long exists = pointLogMapper.selectCount(new LambdaQueryWrapper<UserPointLog>().eq(UserPointLog::getUserId, userId)
                .eq(UserPointLog::getPointType, pointType).eq(UserPointLog::getSourceType, sourceType).eq(UserPointLog::getSourceId, sourceId).eq(UserPointLog::getIsDeleted, 0));
        if (exists > 0) return;
        UserPointLog log = new UserPointLog(); log.setUserId(userId); log.setPointType(pointType); log.setPointValue(value); log.setSourceType(sourceType); log.setSourceId(sourceId); log.setRemark(remark); log.setIsDeleted(0); pointLogMapper.insert(log);
    }
}
