package com.danceflow.vo;

public record LearningCourseVO(Long courseId, String title, String coverUrl, String danceType,
                               Integer lessonCount, Long completedCount, Integer progressPercent) {
}
