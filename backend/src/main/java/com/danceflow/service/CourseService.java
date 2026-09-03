package com.danceflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danceflow.common.PageResult;
import com.danceflow.common.ResultCode;
import com.danceflow.dto.CourseRequest;
import com.danceflow.dto.CourseStatusRequest;
import com.danceflow.dto.LessonProgressRequest;
import com.danceflow.dto.LessonRequest;
import com.danceflow.entity.Course;
import com.danceflow.entity.CourseLesson;
import com.danceflow.entity.LearningRecord;
import com.danceflow.exception.BusinessException;
import com.danceflow.mapper.CourseLessonMapper;
import com.danceflow.mapper.CourseMapper;
import com.danceflow.mapper.LearningRecordMapper;
import com.danceflow.vo.CourseLessonVO;
import com.danceflow.vo.CourseVO;
import com.danceflow.vo.LearningCourseVO;
import com.danceflow.vo.LessonLearningVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseLessonMapper lessonMapper;
    private final LearningRecordMapper recordMapper;

    public CourseService(CourseMapper courseMapper, CourseLessonMapper lessonMapper, LearningRecordMapper recordMapper) {
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
        this.recordMapper = recordMapper;
    }

    public PageResult<CourseVO> publicPage(long page, long pageSize, String keyword, String danceType, String difficulty) {
        LambdaQueryWrapper<Course> query = new LambdaQueryWrapper<Course>().eq(Course::getIsDeleted, 0).eq(Course::getStatus, "PUBLISHED")
                .and(keyword != null && !keyword.isBlank(), q -> q.like(Course::getTitle, keyword).or().like(Course::getTeacherName, keyword))
                .eq(danceType != null && !danceType.isBlank(), Course::getDanceType, danceType)
                .eq(difficulty != null && !difficulty.isBlank(), Course::getDifficulty, difficulty)
                .orderByAsc(Course::getSortNo).orderByDesc(Course::getCreatedAt);
        Page<Course> result = courseMapper.selectPage(new Page<>(safePage(page), safePageSize(pageSize)), query);
        return new PageResult<>(result.getRecords().stream().map(CourseVO::summary).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public PageResult<CourseVO> adminPage(long page, long pageSize, String keyword, String status) {
        LambdaQueryWrapper<Course> query = new LambdaQueryWrapper<Course>().eq(Course::getIsDeleted, 0)
                .like(keyword != null && !keyword.isBlank(), Course::getTitle, keyword)
                .eq(status != null && !status.isBlank(), Course::getStatus, status)
                .orderByAsc(Course::getSortNo).orderByDesc(Course::getCreatedAt);
        Page<Course> result = courseMapper.selectPage(new Page<>(safePage(page), safePageSize(pageSize)), query);
        return new PageResult<>(result.getRecords().stream().map(CourseVO::summary).toList(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public CourseVO detail(Long id, Long userId, boolean publicOnly) {
        Course course = requiredCourse(id);
        if (publicOnly && !"PUBLISHED".equals(course.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课程不存在");
        List<CourseLesson> lessons = lessonMapper.selectList(new LambdaQueryWrapper<CourseLesson>().eq(CourseLesson::getCourseId, id).eq(CourseLesson::getIsDeleted, 0)
                .eq(publicOnly, CourseLesson::getStatus, "PUBLISHED").orderByAsc(CourseLesson::getSortNo));
        List<CourseLessonVO> lessonVOs = lessons.stream().map(lesson -> lessonVO(lesson, userId)).toList();
        long completed = userId == null ? 0 : recordMapper.selectCount(new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId, userId)
                .eq(LearningRecord::getCourseId, id).eq(LearningRecord::getIsDeleted, 0).eq(LearningRecord::getCompleted, 1));
        int lessonCount = lessonVOs.size();
        return new CourseVO(course.getId(), course.getTitle(), course.getCoverUrl(), course.getDanceType(), course.getDifficulty(),
                course.getTeacherName(), course.getDescription(), lessonCount, course.getStatus(), course.getSortNo(), completed,
                lessonCount == 0 ? 0 : (int) (completed * 100 / lessonCount), lessonVOs);
    }

    public LessonLearningVO lesson(Long courseId, Long lessonId, Long userId) {
        Course course = requiredCourse(courseId);
        if (!"PUBLISHED".equals(course.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课程不存在");
        CourseLesson lesson = requiredLesson(lessonId);
        if (!courseId.equals(lesson.getCourseId()) || !"PUBLISHED".equals(lesson.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课时不存在");
        return new LessonLearningVO(CourseVO.summary(course), lessonVO(lesson, userId));
    }

    @Transactional
    public CourseLessonVO saveProgress(Long courseId, Long lessonId, Long userId, LessonProgressRequest request) {
        CourseLesson lesson = requiredLesson(lessonId);
        if (!courseId.equals(lesson.getCourseId()) || !"PUBLISHED".equals(lesson.getStatus())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课时不存在");
        int seconds = Math.min(request.progressSeconds(), lesson.getDuration());
        LearningRecord record = recordMapper.selectOne(new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId, userId)
                .eq(LearningRecord::getLessonId, lessonId).last("LIMIT 1"));
        if (record == null) {
            record = new LearningRecord(); record.setUserId(userId); record.setCourseId(courseId); record.setLessonId(lessonId); record.setIsDeleted(0);
        }
        record.setProgressSeconds(seconds); record.setCompleted(Boolean.TRUE.equals(request.completed()) || seconds >= lesson.getDuration()); record.setLastLearnTime(LocalDateTime.now());
        if (record.getId() == null) recordMapper.insert(record); else recordMapper.updateById(record);
        return lessonVO(lesson, userId);
    }

    public List<LearningCourseVO> myLearning(Long userId) {
        List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().eq(Course::getIsDeleted, 0).inSql(Course::getId,
                "SELECT DISTINCT course_id FROM learning_record WHERE user_id = " + userId + " AND is_deleted = 0")
                .orderByDesc(Course::getUpdatedAt));
        return courses.stream().map(course -> {
            long completed = recordMapper.selectCount(new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId, userId).eq(LearningRecord::getCourseId, course.getId()).eq(LearningRecord::getIsDeleted, 0).eq(LearningRecord::getCompleted, 1));
            int count = lessonMapper.selectCount(new LambdaQueryWrapper<CourseLesson>().eq(CourseLesson::getCourseId, course.getId()).eq(CourseLesson::getIsDeleted, 0).eq(CourseLesson::getStatus, "PUBLISHED")).intValue();
            return new LearningCourseVO(course.getId(), course.getTitle(), course.getCoverUrl(), course.getDanceType(), count, completed, count == 0 ? 0 : (int) (completed * 100 / count));
        }).toList();
    }

    @Transactional public CourseVO create(CourseRequest request) { Course c = new Course(); copy(c, request); c.setStatus("DRAFT"); c.setLessonCount(0); c.setIsDeleted(0); courseMapper.insert(c); return CourseVO.summary(c); }
    @Transactional public CourseVO update(Long id, CourseRequest request) { Course c = requiredCourse(id); copy(c, request); refreshLessonCount(c); courseMapper.updateById(c); return CourseVO.summary(c); }
    @Transactional public CourseVO updateStatus(Long id, CourseStatusRequest request) { Course c = requiredCourse(id); if (!Set.of("DRAFT", "PUBLISHED", "OFFLINE").contains(request.status())) throw new BusinessException("课程状态不正确"); c.setStatus(request.status()); courseMapper.updateById(c); return CourseVO.summary(c); }
    @Transactional public void delete(Long id) { Course c = requiredCourse(id); long count = lessonMapper.selectCount(new LambdaQueryWrapper<CourseLesson>().eq(CourseLesson::getCourseId, id).eq(CourseLesson::getIsDeleted, 0)); if (count > 0) throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "课程存在课时，请先删除课时"); c.setIsDeleted(1); courseMapper.updateById(c); }
    @Transactional public CourseLessonVO createLesson(Long courseId, LessonRequest request) { requiredCourse(courseId); CourseLesson l = new CourseLesson(); copy(l, request); l.setCourseId(courseId); l.setIsDeleted(0); lessonMapper.insert(l); Course c = requiredCourse(courseId); refreshLessonCount(c); courseMapper.updateById(c); return lessonVO(l, null); }
    @Transactional public CourseLessonVO updateLesson(Long id, LessonRequest request) { CourseLesson l = requiredLesson(id); copy(l, request); lessonMapper.updateById(l); return lessonVO(l, null); }
    @Transactional public void deleteLesson(Long id) { CourseLesson l = requiredLesson(id); l.setIsDeleted(1); lessonMapper.updateById(l); Course c = requiredCourse(l.getCourseId()); refreshLessonCount(c); courseMapper.updateById(c); }

    private CourseLessonVO lessonVO(CourseLesson lesson, Long userId) { LearningRecord r = userId == null ? null : recordMapper.selectOne(new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId, userId).eq(LearningRecord::getLessonId, lesson.getId()).eq(LearningRecord::getIsDeleted, 0).last("LIMIT 1")); return CourseLessonVO.from(lesson, r == null ? 0 : r.getProgressSeconds(), r != null && Integer.valueOf(1).equals(r.getCompleted())); }
    private Course requiredCourse(Long id) { Course c = courseMapper.selectById(id); if (c == null || Integer.valueOf(1).equals(c.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课程不存在"); return c; }
    private CourseLesson requiredLesson(Long id) { CourseLesson l = lessonMapper.selectById(id); if (l == null || Integer.valueOf(1).equals(l.getIsDeleted())) throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "课时不存在"); return l; }
    private void copy(Course c, CourseRequest r) { c.setTitle(r.title()); c.setCoverUrl(r.coverUrl()); c.setDanceType(r.danceType()); c.setDifficulty(r.difficulty()); c.setTeacherName(r.teacherName()); c.setDescription(r.description()); c.setSortNo(r.sortNo()); }
    private void copy(CourseLesson l, LessonRequest r) { l.setTitle(r.title()); l.setVideoUrl(r.videoUrl()); l.setDuration(r.duration()); l.setContent(r.content()); l.setSortNo(r.sortNo()); l.setStatus(r.status() == null || r.status().isBlank() ? "PUBLISHED" : r.status()); }
    private void refreshLessonCount(Course c) { c.setLessonCount(lessonMapper.selectCount(new LambdaQueryWrapper<CourseLesson>().eq(CourseLesson::getCourseId, c.getId()).eq(CourseLesson::getIsDeleted, 0)).intValue()); }
    private long safePage(long page) { return Math.max(page, 1); }
    private long safePageSize(long pageSize) { return Math.min(Math.max(pageSize, 1), 100); }
}
