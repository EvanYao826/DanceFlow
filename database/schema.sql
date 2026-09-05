CREATE DATABASE IF NOT EXISTS danceflow DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE danceflow;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(30) NOT NULL,
    avatar VARCHAR(500) NULL,
    phone VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status TINYINT NOT NULL DEFAULT 1,
    last_login_time DATETIME NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sys_user_username (username),
    KEY idx_sys_user_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(30) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sys_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(80) NOT NULL,
    permission_name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT 'MENU',
    parent_id BIGINT NOT NULL DEFAULT 0,
    path VARCHAR(200) NULL,
    status TINYINT NOT NULL DEFAULT 1,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sys_permission_code (permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES sys_role(id),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES sys_permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS club_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    dance_type VARCHAR(50) NULL,
    skill_level VARCHAR(30) NULL,
    join_date DATE NULL,
    member_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    bio VARCHAR(1000) NULL,
    audit_reason VARCHAR(500) NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_club_member_user (user_id),
    KEY idx_club_member_status (member_status),
    CONSTRAINT fk_club_member_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    cover_url VARCHAR(500) NULL,
    description TEXT NULL,
    activity_type VARCHAR(30) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    location VARCHAR(200) NOT NULL,
    capacity INT NOT NULL,
    apply_deadline DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    publisher_id BIGINT NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_activity_public (status, start_time),
    CONSTRAINT fk_activity_publisher FOREIGN KEY (publisher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS activity_apply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    apply_status VARCHAR(20) NOT NULL DEFAULT 'APPLIED',
    remark VARCHAR(500) NULL,
    apply_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_activity_apply_user (activity_id, user_id),
    KEY idx_activity_apply_activity (activity_id, apply_status),
    CONSTRAINT fk_activity_apply_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
    CONSTRAINT fk_activity_apply_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    cover_url VARCHAR(500) NULL,
    dance_type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(30) NOT NULL,
    teacher_name VARCHAR(50) NOT NULL,
    description TEXT NULL,
    lesson_count INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    sort_no INT NOT NULL DEFAULT 0,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_course_public (status, sort_no, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS course_lesson (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    video_url VARCHAR(500) NULL,
    duration INT NOT NULL DEFAULT 0,
    content TEXT NULL,
    sort_no INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_course_lesson_order (course_id, status, sort_no),
    CONSTRAINT fk_course_lesson_course FOREIGN KEY (course_id) REFERENCES course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS learning_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    progress_seconds INT NOT NULL DEFAULT 0,
    completed TINYINT NOT NULL DEFAULT 0,
    last_learn_time DATETIME NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_learning_record_user_lesson (user_id, lesson_id),
    KEY idx_learning_record_course (user_id, course_id),
    CONSTRAINT fk_learning_record_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_learning_record_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_learning_record_lesson FOREIGN KEY (lesson_id) REFERENCES course_lesson(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS stored_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_name VARCHAR(255) NOT NULL,
    storage_key VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stored_file_key (storage_key),
    KEY idx_stored_file_type (content_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dance_work (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    cover_url VARCHAR(500) NULL,
    media_url VARCHAR(500) NOT NULL,
    media_type VARCHAR(30) NOT NULL,
    description TEXT NULL,
    dance_type VARCHAR(50) NOT NULL,
    audit_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    audit_reason VARCHAR(500) NULL,
    like_count INT NOT NULL DEFAULT 0,
    comment_count INT NOT NULL DEFAULT 0,
    collection_count INT NOT NULL DEFAULT 0,
    view_count INT NOT NULL DEFAULT 0,
    published_time DATETIME NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_dance_work_public (audit_status, created_at),
    KEY idx_dance_work_user (user_id, created_at),
    CONSTRAINT fk_dance_work_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_like (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_work_like_user (work_id, user_id),
    CONSTRAINT fk_work_like_work FOREIGN KEY (work_id) REFERENCES dance_work(id),
    CONSTRAINT fk_work_like_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_collection (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_work_collection_user (work_id, user_id),
    CONSTRAINT fk_work_collection_work FOREIGN KEY (work_id) REFERENCES dance_work(id),
    CONSTRAINT fk_work_collection_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    work_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT NOT NULL DEFAULT 0,
    content VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_work_comment_work (work_id, status, created_at),
    CONSTRAINT fk_work_comment_work FOREIGN KEY (work_id) REFERENCES dance_work(id),
    CONSTRAINT fk_work_comment_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS forum_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    cover_url VARCHAR(500) NULL,
    category VARCHAR(30) NOT NULL DEFAULT '交流',
    status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
    view_count INT NOT NULL DEFAULT 0,
    like_count INT NOT NULL DEFAULT 0,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_forum_post_public (status, category, created_at),
    KEY idx_forum_post_user (user_id, created_at),
    CONSTRAINT fk_forum_post_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    publisher_id BIGINT NOT NULL,
    publish_status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    publish_time DATETIME NULL,
    top_flag TINYINT NOT NULL DEFAULT 0,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_notice_public (publish_status, top_flag, publish_time),
    CONSTRAINT fk_notice_publisher FOREIGN KEY (publisher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_point_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    point_type VARCHAR(30) NOT NULL,
    point_value INT NOT NULL,
    source_type VARCHAR(30) NOT NULL,
    source_id BIGINT NOT NULL,
    remark VARCHAR(255) NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_point_source (user_id, point_type, source_type, source_id),
    KEY idx_user_point_log_user (user_id, created_at),
    CONSTRAINT fk_user_point_log_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    operator_id BIGINT NULL,
    operator_name VARCHAR(30) NULL,
    action VARCHAR(20) NOT NULL,
    target_type VARCHAR(50) NULL,
    target_id VARCHAR(50) NULL,
    request_path VARCHAR(255) NOT NULL,
    result VARCHAR(20) NOT NULL,
    detail VARCHAR(500) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_operation_log_created (created_at),
    KEY idx_operation_log_operator (operator_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sys_dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dict_type VARCHAR(50) NOT NULL,
    dict_label VARCHAR(100) NOT NULL,
    dict_value VARCHAR(100) NOT NULL,
    sort_no INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sys_dict_value (dict_type, dict_value),
    KEY idx_sys_dict_type (dict_type, status, sort_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
