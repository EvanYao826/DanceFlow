USE danceflow;

INSERT INTO sys_role (role_code, role_name)
VALUES ('USER', '普通用户'), ('ADMIN', '管理员'), ('SUPER_ADMIN', '超级管理员')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), updated_at = CURRENT_TIMESTAMP;

INSERT INTO sys_permission (permission_code, permission_name, type, path)
VALUES
    ('home:view', '首页', 'MENU', '/'),
    ('admin:view', '管理端', 'MENU', '/admin'),
    ('admin:role:view', '角色管理', 'BUTTON', '/api/admin/roles'),
    ('admin:permission:view', '权限管理', 'BUTTON', '/api/admin/permissions')
ON DUPLICATE KEY UPDATE permission_name = VALUES(permission_name), updated_at = CURRENT_TIMESTAMP;

INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM sys_role r CROSS JOIN sys_permission p WHERE r.role_code = 'USER' AND p.permission_code = 'home:view';

INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id FROM sys_role r CROSS JOIN sys_permission p WHERE r.role_code IN ('ADMIN', 'SUPER_ADMIN') AND p.permission_code IN ('home:view', 'admin:view', 'admin:role:view', 'admin:permission:view');

-- 演示账号：密码均为 password，生产环境禁止使用这些账号。
INSERT INTO sys_user (username, password, nickname, email, role, status)
VALUES
    ('admin_demo', '$2a$10$wj6vENfaaG4cudegsK9ome/5nSmb.bSTOYpApRBUTaUDRusc8Iu7a', '社团管理员', 'admin@danceflow.local', 'ADMIN', 1),
    ('dance_demo', '$2a$10$wj6vENfaaG4cudegsK9ome/5nSmb.bSTOYpApRBUTaUDRusc8Iu7a', '街舞同学', 'dance@danceflow.local', 'USER', 1),
    ('member_demo', '$2a$10$wj6vENfaaG4cudegsK9ome/5nSmb.bSTOYpApRBUTaUDRusc8Iu7a', 'Flow 舞者', 'member@danceflow.local', 'USER', 1)
ON DUPLICATE KEY UPDATE nickname = VALUES(nickname), email = VALUES(email), role = VALUES(role), status = 1;

INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT u.id, r.id FROM sys_user u JOIN sys_role r ON r.role_code = u.role
WHERE u.username IN ('admin_demo', 'dance_demo', 'member_demo');

INSERT INTO club_member (user_id, dance_type, skill_level, join_date, member_status, bio)
SELECT u.id, 'Hip-hop', 'INTERMEDIATE', CURRENT_DATE, 'ACTIVE', '喜欢律动和团队训练，期待在舞台上见。'
FROM sys_user u WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM club_member m WHERE m.user_id = u.id);

INSERT INTO club_member (user_id, dance_type, skill_level, member_status, bio)
SELECT u.id, 'Jazz', 'BEGINNER', 'PENDING', '刚开始学习街舞，希望参加基础训练。'
FROM sys_user u WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM club_member m WHERE m.user_id = u.id);

INSERT INTO activity (title, cover_url, description, activity_type, start_time, end_time, location, capacity, apply_deadline, status, publisher_id)
SELECT '周末 Hip-hop 基础训练', NULL, '从律动、基础步伐到一段完整编舞，适合有兴趣的社员参加。', '训练课',
       '2026-09-12 14:00:00', '2026-09-12 16:00:00', '大学生活动中心 301', 30, '2026-09-12 12:00:00', 'PUBLISHED', u.id
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM activity a WHERE a.title = '周末 Hip-hop 基础训练');

INSERT INTO activity (title, cover_url, description, activity_type, start_time, end_time, location, capacity, apply_deadline, status, publisher_id)
SELECT '新生舞者见面会', NULL, '认识新朋友，了解社团训练安排和本学期活动计划。', '社团活动',
       '2026-09-20 19:00:00', '2026-09-20 21:00:00', '大学生活动中心 201', 80, '2026-09-20 18:00:00', 'DRAFT', u.id
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM activity a WHERE a.title = '新生舞者见面会');

INSERT INTO activity (title, cover_url, description, activity_type, start_time, end_time, location, capacity, apply_deadline, status, publisher_id)
SELECT '暑期成果展示', NULL, '本学期训练成果回顾与舞者交流活动。', '成果展示',
       '2026-08-16 18:30:00', '2026-08-16 21:00:00', '大学生活动中心礼堂', 200, '2026-08-16 17:00:00', 'CLOSED', u.id
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM activity a WHERE a.title = '暑期成果展示');

INSERT INTO activity (title, cover_url, description, activity_type, start_time, end_time, location, capacity, apply_deadline, status, publisher_id)
SELECT '舞台表现力小组训练', NULL, '围绕走位、表情和队形完成舞台表现力专项训练。', '专项训练',
       '2026-09-18 19:00:00', '2026-09-18 21:00:00', '大学生活动中心 302', 24, '2026-09-18 17:00:00', 'PUBLISHED', u.id
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM activity a WHERE a.title = '舞台表现力小组训练');

INSERT INTO activity (title, cover_url, description, activity_type, start_time, end_time, location, capacity, apply_deadline, status, publisher_id)
SELECT '秋季招新体验课', NULL, '面向新社员的舞蹈体验课，包含热身、律动和基础组合。', '体验课',
       '2026-09-26 14:30:00', '2026-09-26 16:30:00', '大学生活动中心 105', 40, '2026-09-26 12:00:00', 'PUBLISHED', u.id
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM activity a WHERE a.title = '秋季招新体验课');

INSERT INTO activity_apply (activity_id, user_id, apply_status, remark)
SELECT a.id, u.id, 'APPLIED', '期待参加训练。'
FROM activity a CROSS JOIN sys_user u
WHERE a.title = '周末 Hip-hop 基础训练' AND u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM activity_apply x WHERE x.activity_id = a.id AND x.user_id = u.id);

INSERT INTO activity_apply (activity_id, user_id, apply_status, remark, apply_time)
SELECT a.id, u.id, 'APPLIED', '已安排时间参加基础训练。', '2026-09-04 10:10:00'
FROM activity a CROSS JOIN sys_user u
WHERE a.title = '周末 Hip-hop 基础训练' AND u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM activity_apply x WHERE x.activity_id = a.id AND x.user_id = u.id);

INSERT INTO activity_apply (activity_id, user_id, apply_status, remark, apply_time)
SELECT a.id, u.id, 'APPLIED', '希望提升舞台表现力。', '2026-09-04 10:25:00'
FROM activity a CROSS JOIN sys_user u
WHERE a.title = '舞台表现力小组训练' AND u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM activity_apply x WHERE x.activity_id = a.id AND x.user_id = u.id);

INSERT INTO activity_apply (activity_id, user_id, apply_status, remark, apply_time)
SELECT a.id, u.id, 'APPLIED', '第一次参加体验课，请多关照。', '2026-09-04 10:40:00'
FROM activity a CROSS JOIN sys_user u
WHERE a.title = '秋季招新体验课' AND u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM activity_apply x WHERE x.activity_id = a.id AND x.user_id = u.id);

INSERT INTO course (title, cover_url, dance_type, difficulty, teacher_name, description, lesson_count, status, sort_no)
SELECT 'Hip-hop 基础律动', NULL, 'Hip-hop', 'BEGINNER', 'Flow 老师', '从音乐律动、基础步伐开始，建立街舞入门必备的节奏感。', 4, 'PUBLISHED', 1
WHERE NOT EXISTS (SELECT 1 FROM course c WHERE c.title = 'Hip-hop 基础律动');

INSERT INTO course (title, cover_url, dance_type, difficulty, teacher_name, description, lesson_count, status, sort_no)
SELECT 'Jazz Funk 编舞入门', NULL, 'Jazz Funk', 'INTERMEDIATE', 'Mia 老师', '学习身体线条、重心转换和一段完整的 Jazz Funk 编舞。', 3, 'PUBLISHED', 2
WHERE NOT EXISTS (SELECT 1 FROM course c WHERE c.title = 'Jazz Funk 编舞入门');

INSERT INTO course (title, cover_url, dance_type, difficulty, teacher_name, description, lesson_count, status, sort_no)
SELECT '舞台表演训练', NULL, 'Performance', 'ADVANCED', 'Leo 老师', '针对舞台表现力和团队配合的进阶训练课程。', 2, 'DRAFT', 3
WHERE NOT EXISTS (SELECT 1 FROM course c WHERE c.title = '舞台表演训练');

INSERT INTO course (title, cover_url, dance_type, difficulty, teacher_name, description, lesson_count, status, sort_no)
SELECT 'K-pop 编舞基础', NULL, 'K-pop', 'BEGINNER', 'Yuna 老师', '从动作记忆到队形切换，完成一段 K-pop 入门组合。', 3, 'PUBLISHED', 3
WHERE NOT EXISTS (SELECT 1 FROM course c WHERE c.title = 'K-pop 编舞基础');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '认识节拍与律动', NULL, 420, '认识八拍结构，练习身体随音乐自然摆动。', 1, 'PUBLISHED'
FROM course c WHERE c.title = 'Hip-hop 基础律动'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '认识节拍与律动');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '基础 Bounce', NULL, 510, '掌握膝盖弹动和身体重心，完成基础 Bounce 组合。', 2, 'PUBLISHED'
FROM course c WHERE c.title = 'Hip-hop 基础律动'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '基础 Bounce');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, 'Groove 步伐练习', NULL, 600, '结合左右移动和律动，练习基础 Groove 步伐。', 3, 'PUBLISHED'
FROM course c WHERE c.title = 'Hip-hop 基础律动'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = 'Groove 步伐练习');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '入门组合', NULL, 720, '将前面学到的内容串联成一段完整入门组合。', 4, 'PUBLISHED'
FROM course c WHERE c.title = 'Hip-hop 基础律动'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '入门组合');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, 'Jazz Funk 身体线条', NULL, 540, '练习手臂延展、胸腔控制和身体线条。', 1, 'PUBLISHED'
FROM course c WHERE c.title = 'Jazz Funk 编舞入门'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = 'Jazz Funk 身体线条');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '重心与方向', NULL, 600, '训练重心转换和舞蹈方向变化。', 2, 'PUBLISHED'
FROM course c WHERE c.title = 'Jazz Funk 编舞入门'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '重心与方向');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '完整编舞练习', NULL, 780, '完成一段 Jazz Funk 入门编舞并进行复习。', 3, 'PUBLISHED'
FROM course c WHERE c.title = 'Jazz Funk 编舞入门'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '完整编舞练习');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, 'K-pop 节拍认识', NULL, 480, '跟随音乐认识常见 K-pop 节拍和动作重拍。', 1, 'PUBLISHED'
FROM course c WHERE c.title = 'K-pop 编舞基础'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = 'K-pop 节拍认识');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '副歌动作练习', NULL, 660, '拆分练习副歌动作，注意手脚配合和发力方向。', 2, 'PUBLISHED'
FROM course c WHERE c.title = 'K-pop 编舞基础'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '副歌动作练习');

INSERT INTO course_lesson (course_id, title, video_url, duration, content, sort_no, status)
SELECT c.id, '队形与完整串联', NULL, 720, '完成队形切换并串联整段入门编舞。', 3, 'PUBLISHED'
FROM course c WHERE c.title = 'K-pop 编舞基础'
  AND NOT EXISTS (SELECT 1 FROM course_lesson l WHERE l.course_id = c.id AND l.title = '队形与完整串联');

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 420, 1, '2026-09-02 20:30:00'
FROM sys_user u
         JOIN course c ON c.title = 'Hip-hop 基础律动'
         JOIN course_lesson l ON l.course_id = c.id AND l.title = '认识节拍与律动'
WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 510, 1, '2026-09-02 21:00:00'
FROM sys_user u
         JOIN course c ON c.title = 'Hip-hop 基础律动'
         JOIN course_lesson l ON l.course_id = c.id AND l.title = '基础 Bounce'
WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 260, 0, '2026-09-03 09:15:00'
FROM sys_user u
         JOIN course c ON c.title = 'Jazz Funk 编舞入门'
         JOIN course_lesson l ON l.course_id = c.id AND l.title = 'Jazz Funk 身体线条'
WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 420, 1, '2026-09-03 19:20:00'
FROM sys_user u JOIN course c ON c.title = 'Hip-hop 基础律动'
                JOIN course_lesson l ON l.course_id = c.id AND l.title = '认识节拍与律动'
WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 330, 0, '2026-09-04 09:30:00'
FROM sys_user u JOIN course c ON c.title = 'Hip-hop 基础律动'
                JOIN course_lesson l ON l.course_id = c.id AND l.title = '基础 Bounce'
WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 480, 1, '2026-09-04 11:00:00'
FROM sys_user u JOIN course c ON c.title = 'K-pop 编舞基础'
                JOIN course_lesson l ON l.course_id = c.id AND l.title = 'K-pop 节拍认识'
WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

INSERT INTO learning_record (user_id, course_id, lesson_id, progress_seconds, completed, last_learn_time)
SELECT u.id, c.id, l.id, 260, 0, '2026-09-04 11:30:00'
FROM sys_user u JOIN course c ON c.title = 'K-pop 编舞基础'
                JOIN course_lesson l ON l.course_id = c.id AND l.title = '副歌动作练习'
WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM learning_record r WHERE r.user_id = u.id AND r.lesson_id = l.id);

-- 阶段四作品测试数据：媒体地址使用前端开发服务器可直接访问的资源，便于本地验收。
INSERT INTO dance_work (user_id, title, cover_url, media_url, media_type, description, dance_type, audit_status, like_count, comment_count, collection_count, view_count, published_time)
SELECT u.id, '紫色律动练习', '/src/assets/dance1.png', '/src/assets/dance1.png', 'IMAGE', '记录一次训练中的基础律动和身体控制。', 'Hip-hop', 'PUBLISHED', 2, 1, 1, 18, '2026-09-03 10:00:00'
FROM sys_user u WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM dance_work w WHERE w.title = '紫色律动练习');

INSERT INTO dance_work (user_id, title, cover_url, media_url, media_type, description, dance_type, audit_status, like_count, comment_count, collection_count, view_count, published_time)
SELECT u.id, 'Jazz Funk 线条训练', '/src/assets/dance2.png', '/src/assets/dance2.png', 'IMAGE', '用身体线条和重心转换完成一段练习记录。', 'Jazz Funk', 'PUBLISHED', 5, 2, 3, 32, '2026-09-03 11:20:00'
FROM sys_user u WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM dance_work w WHERE w.title = 'Jazz Funk 线条训练');

INSERT INTO dance_work (user_id, title, cover_url, media_url, media_type, description, dance_type, audit_status, like_count, comment_count, collection_count, view_count)
SELECT u.id, '新作品待审核', '/src/assets/dance3.png', '/src/assets/dance3.png', 'IMAGE', '用于管理员审核流程验收的待审核作品。', 'Breaking', 'PENDING', 0, 0, 0, 0
FROM sys_user u WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM dance_work w WHERE w.title = '新作品待审核');

INSERT INTO work_comment (work_id, user_id, parent_id, content, status)
SELECT w.id, u.id, 0, '律动很有感觉，继续保持！', 'NORMAL'
FROM dance_work w CROSS JOIN sys_user u
WHERE w.title = '紫色律动练习' AND u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM work_comment c WHERE c.work_id = w.id AND c.content = '律动很有感觉，继续保持！');

INSERT INTO work_like (work_id, user_id, is_deleted)
SELECT w.id, u.id, 0 FROM dance_work w CROSS JOIN sys_user u
WHERE w.title = '紫色律动练习' AND u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM work_like l WHERE l.work_id = w.id AND l.user_id = u.id);

INSERT INTO work_collection (work_id, user_id, is_deleted)
SELECT w.id, u.id, 0 FROM dance_work w CROSS JOIN sys_user u
WHERE w.title = 'Jazz Funk 线条训练' AND u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM work_collection c WHERE c.work_id = w.id AND c.user_id = u.id);

INSERT INTO forum_post (user_id, title, content, category, status, view_count, like_count)
SELECT u.id, '第一次参加社团训练的感受', '今天完成了基础律动训练，大家互相纠正动作的氛围很好。想请教大家平时如何练习音乐感？', '训练交流', 'PUBLISHED', 26, 4
FROM sys_user u WHERE u.username = 'dance_demo'
  AND NOT EXISTS (SELECT 1 FROM forum_post p WHERE p.title = '第一次参加社团训练的感受');

INSERT INTO forum_post (user_id, title, content, category, status, view_count, like_count)
SELECT u.id, '求推荐适合入门的 Jazz Funk 歌单', '最近在练习身体线条和重心转换，希望找一些节奏清晰、适合反复练习的音乐。', '求助问答', 'PUBLISHED', 18, 2
FROM sys_user u WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM forum_post p WHERE p.title = '求推荐适合入门的 Jazz Funk 歌单');

INSERT INTO notice (title, content, publisher_id, publish_status, publish_time, top_flag)
SELECT '本周训练安排', '本周六下午进行 Hip-hop 基础训练，请提前十分钟到场完成签到。', u.id, 'PUBLISHED', '2026-09-04 09:00:00', 1
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM notice n WHERE n.title = '本周训练安排');

INSERT INTO notice (title, content, publisher_id, publish_status, publish_time, top_flag)
SELECT '新生见面会报名开启', '欢迎新成员报名参加新生舞者见面会，现场将介绍本学期活动与课程计划。', u.id, 'PUBLISHED', '2026-09-03 10:00:00', 0
FROM sys_user u WHERE u.username = 'admin_demo'
  AND NOT EXISTS (SELECT 1 FROM notice n WHERE n.title = '新生见面会报名开启');

INSERT INTO user_point_log (user_id, point_type, point_value, source_type, source_id, remark)
SELECT u.id, 'WORK_PUBLISH', 20, 'WORK', w.id, '发布作品：紫色律动练习'
FROM sys_user u JOIN dance_work w ON w.title = '紫色律动练习'
WHERE u.username = 'member_demo'
  AND NOT EXISTS (SELECT 1 FROM user_point_log l WHERE l.user_id = u.id AND l.point_type = 'WORK_PUBLISH' AND l.source_type = 'WORK' AND l.source_id = w.id);
