package com.danceflow.vo;

import java.io.Serial;
import java.io.Serializable;

/** 统计概览 VO；实现 Serializable 以便直接作为 Redis 缓存值。 */
public record StatisticsOverviewVO(long totalUsers, long totalMembers, long totalActivities, long totalCourses,
                                   long totalWorks, long totalPosts, long totalComments, long totalApplications)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
