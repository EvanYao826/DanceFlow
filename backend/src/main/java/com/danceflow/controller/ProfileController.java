package com.danceflow.controller;

import com.danceflow.common.PageResult;
import com.danceflow.common.Result;
import com.danceflow.security.AuthUser;
import com.danceflow.service.CourseService;
import com.danceflow.service.ProfileService;
import com.danceflow.vo.ActivityVO;
import com.danceflow.vo.LearningCourseVO;
import com.danceflow.vo.PointLogVO;
import com.danceflow.vo.UserOverviewVO;
import com.danceflow.vo.WorkVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users/me")
public class ProfileController {
    private final ProfileService profileService; private final CourseService courseService;
    public ProfileController(ProfileService profileService, CourseService courseService) { this.profileService=profileService; this.courseService=courseService; }
    @GetMapping("/overview") public Result<UserOverviewVO> overview(Authentication auth) { return Result.ok(profileService.overview(userId(auth))); }
    @GetMapping("/activities") public Result<PageResult<ActivityVO>> activities(Authentication auth,@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize) { return Result.ok(profileService.activities(userId(auth),page,pageSize)); }
    @GetMapping("/courses") public Result<PageResult<LearningCourseVO>> courses(Authentication auth,@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize) { return Result.ok(courseService.myLearningPage(userId(auth),page,pageSize)); }
    @GetMapping("/collections") public Result<PageResult<WorkVO>> collections(Authentication auth,@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize) { return Result.ok(profileService.collections(userId(auth),page,pageSize)); }
    @GetMapping("/points") public Result<PageResult<PointLogVO>> points(Authentication auth,@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long pageSize) { return Result.ok(profileService.points(userId(auth),page,pageSize)); }
    private Long userId(Authentication auth) { return ((AuthUser)auth.getPrincipal()).id(); }
}
