package com.danceflow.vo;
public record UserOverviewVO(Integer workCount, Integer activityCount, Integer completedLessonCount, Integer receivedLikeCount,
                             Integer collectionCount, Integer totalPoints, String levelName, Integer levelProgress) {}
