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

INSERT INTO activity_apply (activity_id, user_id, apply_status, remark)
SELECT a.id, u.id, 'APPLIED', '期待参加训练。'
FROM activity a CROSS JOIN sys_user u
WHERE a.title = '周末 Hip-hop 基础训练' AND u.username = 'member_demo'
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
