package com.danceflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.danceflow.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
