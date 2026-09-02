package com.danceflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.danceflow.entity.ActivityApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityApplyMapper extends BaseMapper<ActivityApply> {
    @Select("SELECT * FROM activity_apply WHERE activity_id = #{activityId} AND user_id = #{userId} AND is_deleted = 0 LIMIT 1 FOR UPDATE")
    ActivityApply selectForUpdate(Long activityId, Long userId);
}
