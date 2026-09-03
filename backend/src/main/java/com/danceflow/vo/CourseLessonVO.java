package com.danceflow.vo;

import com.danceflow.entity.CourseLesson;

public record CourseLessonVO(Long id, Long courseId, String title, String videoUrl, Integer duration,
                             String content, Integer sortNo, String status, Integer progressSeconds,
                             Boolean completed) {
    public static CourseLessonVO from(CourseLesson lesson, Integer progressSeconds, Boolean completed) {
        return new CourseLessonVO(lesson.getId(), lesson.getCourseId(), lesson.getTitle(), lesson.getVideoUrl(),
                lesson.getDuration(), lesson.getContent(), lesson.getSortNo(), lesson.getStatus(), progressSeconds, completed);
    }
}
