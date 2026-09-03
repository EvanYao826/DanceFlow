package com.danceflow.vo;

import com.danceflow.entity.Course;

import java.util.List;

public record CourseVO(Long id, String title, String coverUrl, String danceType, String difficulty,
                       String teacherName, String description, Integer lessonCount, String status,
                       Integer sortNo, Long completedCount, Integer progressPercent, List<CourseLessonVO> lessons) {
    public static CourseVO summary(Course course) {
        return new CourseVO(course.getId(), course.getTitle(), course.getCoverUrl(), course.getDanceType(),
                course.getDifficulty(), course.getTeacherName(), course.getDescription(), course.getLessonCount(),
                course.getStatus(), course.getSortNo(), null, null, null);
    }
}
