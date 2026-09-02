package com.danceflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.danceflow.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    @Select("SELECT * FROM activity WHERE id = #{id} AND is_deleted = 0 LIMIT 1 FOR UPDATE")
    Activity selectForUpdate(Long id);
}
