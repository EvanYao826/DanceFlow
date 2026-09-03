package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.dto.CourseRequest;
import com.danceflow.dto.CourseStatusRequest;
import com.danceflow.dto.LessonProgressRequest;
import com.danceflow.dto.LessonRequest;
import com.danceflow.security.AuthUser;
import com.danceflow.service.CourseService;
import com.danceflow.vo.CourseLessonVO;
import com.danceflow.vo.CourseVO;
import com.danceflow.vo.LearningCourseVO;
import com.danceflow.vo.LessonLearningVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @GetMapping("/courses")
    public Result<PageResult<CourseVO>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize,
                                             @RequestParam(required = false) String keyword, @RequestParam(required = false) String danceType,
                                             @RequestParam(required = false) String difficulty) {
        return Result.ok(courseService.publicPage(page, pageSize, keyword, danceType, difficulty));
    }

    @GetMapping("/courses/{id}")
    public Result<CourseVO> detail(Authentication authentication, @PathVariable Long id) {
        Long userId = currentUserId(authentication);
        return Result.ok(courseService.detail(id, userId, true));
    }

    @GetMapping("/courses/{id}/lessons/{lessonId}")
    public Result<LessonLearningVO> lesson(Authentication authentication, @PathVariable Long id, @PathVariable Long lessonId) {
        return Result.ok(courseService.lesson(id, lessonId, currentUserId(authentication)));
    }

    @PutMapping("/courses/{id}/lessons/{lessonId}/progress")
    public Result<CourseLessonVO> progress(Authentication authentication, @PathVariable Long id, @PathVariable Long lessonId,
                                            @Valid @RequestBody LessonProgressRequest request) {
        return Result.ok(courseService.saveProgress(id, lessonId, currentUserId(authentication), request));
    }

    @GetMapping("/courses/my")
    public Result<List<LearningCourseVO>> my(Authentication authentication) {
        return Result.ok(courseService.myLearning(currentUserId(authentication)));
    }

    @GetMapping("/admin/courses")
    public Result<PageResult<CourseVO>> adminPage(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pageSize,
                                                  @RequestParam(required = false) String keyword, @RequestParam(required = false) String status) {
        return Result.ok(courseService.adminPage(page, pageSize, keyword, status));
    }

    @GetMapping("/admin/courses/{id}")
    public Result<CourseVO> adminDetail(@PathVariable Long id) { return Result.ok(courseService.detail(id, null, false)); }

    @PostMapping("/admin/courses")
    public Result<CourseVO> create(@Valid @RequestBody CourseRequest request) { return Result.ok(courseService.create(request)); }

    @PutMapping("/admin/courses/{id}")
    public Result<CourseVO> update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) { return Result.ok(courseService.update(id, request)); }

    @DeleteMapping("/admin/courses/{id}")
    public Result<Void> delete(@PathVariable Long id) { courseService.delete(id); return Result.ok(); }

    @PutMapping("/admin/courses/{id}/status")
    public Result<CourseVO> status(@PathVariable Long id, @Valid @RequestBody CourseStatusRequest request) { return Result.ok(courseService.updateStatus(id, request)); }

    @PostMapping("/admin/courses/{id}/lessons")
    public Result<CourseLessonVO> createLesson(@PathVariable Long id, @Valid @RequestBody LessonRequest request) { return Result.ok(courseService.createLesson(id, request)); }

    @PutMapping("/admin/lessons/{id}")
    public Result<CourseLessonVO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonRequest request) { return Result.ok(courseService.updateLesson(id, request)); }

    @DeleteMapping("/admin/lessons/{id}")
    public Result<Void> deleteLesson(@PathVariable Long id) { courseService.deleteLesson(id); return Result.ok(); }

    private Long currentUserId(Authentication authentication) { return authentication != null && authentication.getPrincipal() instanceof AuthUser user ? user.id() : null; }
}
